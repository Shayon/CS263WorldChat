package WorldChat.WorldChat;

import java.util.Date;
import java.util.logging.Level;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;


public class ChatIdGen 
{
	/**
	 * Generates a unique ClientId creates a Channel with it
	 * <p>
	 * We reuse old clientId's if we can assure that their cookie has expired
	 * 
	 * @param req request where we get the country and city from the header
	 * @return a string that is the unique ClientId that was created
	 */
	public static String generateIdAndCreateChannel(HttpServletRequest req)
	{
		String country=req.getHeader("X-AppEngine-Country");
		String city=req.getHeader("X-AppEngine-City");
		String retstr="";
		if(country == null)
		{
			retstr = "Error: No Country";
			return retstr;
		}
		if(city == null)
		{
			retstr = country;
		}
		else
		{
			retstr = city+ ", "+country;
		}
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
   	 	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		
		int inc=1;
		Key chatKey = KeyFactory.createKey("chat", retstr+inc);
		try
		{	
			while(true)
			{	
				Entity ent = (Entity) syncCache.get(chatKey);
				if(ent==null)
				{
					ent = datastore.get(chatKey);
				}
				//Reuse old id's if their cookie is expired
				Date recorded= (Date) ent.getProperty("date");
				Date current = new Date();
				long hourDifference = (current.getTime() - recorded.getTime())/(60 * 60 * 1000);
				if(hourDifference >= 2)
				{
					break;
				}
				
				inc++;
				chatKey = KeyFactory.createKey("chat", retstr+inc);
			}
		}
		catch(EntityNotFoundException e)
		{
			//We found an unused chatId
		}
	    Date date = new Date();
	    Entity chatId = new Entity(chatKey);
	    chatId.setProperty("date", date);
	    ChannelService channelService = ChannelServiceFactory.getChannelService();
	    String token = channelService.createChannel(retstr+inc);
	    chatId.setProperty("channelToken",token);
	    
	    datastore.put(chatId);
	    syncCache.put(chatKey, chatId);
		return retstr+inc;
	}
	
	/**
	 * Gets the ClientId from the Users cookies
	 * 
	 * @param req request from which we get the cookies
	 * @return the ClientId on the cookie, if we don't find the cookie, empty string is returned
	 */
	public static String getClientId(HttpServletRequest req)
	{
		Cookie[] cookies = req.getCookies();
		String clientId="";
		for(int x=0;cookies!= null && x<cookies.length;x++)
		{
			if(cookies[x].getName().equals("WorldChat"))
			{
				clientId=cookies[x].getValue();
			}
		}
		return clientId;
	}
}
