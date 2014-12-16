package WorldChat.WorldChat;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	
	/**
	 * Sets cookie and sets up client for chatting
	 * <p>
	 * Checks if client already has our cookie, if client does not, we give them a cookie.
	 * Then we generate a clientId if no cookie was found, and subscribe them to the chat.
	 * </p>
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		//ChannelService channelService = ChannelServiceFactory.getChannelService();
		System.err.println("In GET*******************************************************");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//Do we already have this user recognized?
		//REPLACE WITH ChatIdGen.getClientId(req) WHEN DONE TESTING
		Cookie[] cookies = req.getCookies();
		if(cookies!=null)
		{
			for(int x=0;x<cookies.length;x++)
			{
				if(cookies[x].getName().equals("WorldChat"))
				{
					System.err.println("In cookie found*******************************************************");
					String clientId=cookies[x].getValue();
					ChatManager.removeSub(clientId);
					ChatManager.addSub(clientId);
					//Already set up, get channelToken from datastore
					Key chatKey = KeyFactory.createKey("chat", clientId);
					String token="";
					try {
						token = (datastore.get(chatKey)).getProperty("channelToken").toString();
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println("token is "+token+"*******************************************************");
					res.setContentType("text/plain");
					res.getWriter().print(token);
					return;
				}
			}
		}
		//we did not find the Cookie so set one
		//first we need to get the clientId to put into the cookie
		String clientId = ChatIdGen.generateIdAndCreateChannel(req);
		//now we build the cookie
		System.err.println("In buildning new cookie*******************************************************");
		Cookie cookie1 = new Cookie("WorldChat", clientId);
        cookie1.setMaxAge(1*60); //1 min for texting, should increase later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        res.addCookie(cookie1);
		
		//The token is based on the unique clientId that we created
        //***Channel is now created in ChatIdGen
	    //String token = channelService.createChannel(clientId);
        Key chatKey = KeyFactory.createKey("chat", clientId);
        System.err.println("key is chat "+clientId+"*******************************************************");
		String token="";
		try {
			Entity ent= datastore.get(chatKey);
			token = ent.getProperty("channelToken").toString();
			System.err.println("token is "+token+"*******************************************************");
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    //Subscribe this client
	    ChatManager.addSub(clientId);
	    
	    //Reply with the token
		res.setContentType("text/plain");
		res.getWriter().print(token);
	}

	/**
	 * Publish a chat message to all subscribers, if this subscriber exists
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException 
	{
		System.err.println("In POST*******************************************************");
		String clientId=ChatIdGen.getClientId(req);
		System.err.println("client Id "+clientId+"*******************************************************");
		if(clientId=="")
		{
			//Something went wrong!
			System.err.println("couldn't find cookie*******************************************************");
			res.setContentType("text/plain");
			res.getWriter().print("Error: Could not find Cookie!");
		}
		System.err.println("above send message*******************************************************");
		ChatManager.sendMessage(StringEscapeUtils.escapeHtml(req.getParameter("message")), clientId);
		res.setContentType("text/plain");
		res.getWriter().print("Success");
	}
}