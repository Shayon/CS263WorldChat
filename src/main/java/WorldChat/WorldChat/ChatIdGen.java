package WorldChat.WorldChat;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;


public class ChatIdGen 
{

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
		
		int inc=1;
		Key chatKey = KeyFactory.createKey("chat", retstr+inc);
		try
		{	
			while(true)
			{
				datastore.get(chatKey);
				inc++;
				chatKey = KeyFactory.createKey("chat", retstr+inc);
			}
		}
		catch(EntityNotFoundException e)
		{
			//We found an unused chatId
		}
		System.err.println("key is"+chatKey.toString()+"*******************************************************");
	    Date date = new Date();
	    Entity chatId = new Entity(chatKey);
	    chatId.setProperty("date", date);
	    ChannelService channelService = ChannelServiceFactory.getChannelService();
	    String token = channelService.createChannel(retstr+inc);
	    chatId.setProperty("channelToken",token);
	    
	    datastore.put(chatId);
		return retstr+inc;
	}
}
