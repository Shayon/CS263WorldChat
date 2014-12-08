package WorldChat.WorldChat;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class ChatIdGen 
{

	public static String generateId(HttpServletRequest req)
	{
		String country=req.getHeader("X-AppEngine-Country");
		String city=req.getHeader("X-AppEngine-City");
		String retstr;
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
		Key chatKey = KeyFactory.createKey("chat", retstr);
		int inc=1;
		try
		{	
			while(true)
			{
				datastore.get(chatKey);
				chatKey = KeyFactory.createKey("chat", retstr+inc);
			}
		}
		catch(EntityNotFoundException e)
		{
			//We found an unused chatId
		}
	    Date date = new Date();
	    Entity chatId = new Entity("chatId", chatKey);
	    chatId.setProperty("date", date);

	    
	    datastore.put(chatId);
		return retstr+inc;
	}
}
