package WorldChat.WorldChat;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	
	//Generate a client token for the GAE Channel API
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		System.err.println("In GET*******************************************************");
		//Do we already have this user recognized?
		Cookie[] cookies = req.getCookies();
		if(cookies!=null)
		{
			for(int x=0;x<cookies.length;x++)
			{
				if(cookies[x].getName().equals("WorldChat"))
				{
					System.err.println("In cookie found*******************************************************");
					ChatManager.removeSub(cookies[x].getValue());
					ChatManager.addSub(cookies[x].getValue());
					//Already set up, reply with empty text
					res.setContentType("text/plain");
					res.getWriter().print("");
					return;
				}
			}
		}
		//we did not find the Cookie so set one
		//first we need to get the clientId to put into the cookie
		String clientId = ChatIdGen.generateId(req);
		//now we build the cookie
		System.err.println("In buildning new cookie*******************************************************");
		Cookie cookie1 = new Cookie("WorldChat", clientId);
        cookie1.setMaxAge(2*60*60); //2 hours
        res.addCookie(cookie1);
		
		//The token is based on the unique clientId that we created
	    String token = channelService.createChannel(clientId);
	    
	    //Subscribe this client
	    ChatManager.addSub(clientId);
	    
	    //Reply with the token
		res.setContentType("text/plain");
		res.getWriter().print(token);
	}

	//Publish a chat message to all subscribers, if this subscriber exists
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException 
	{
		System.err.println("In POST*******************************************************");
		Cookie[] cookies = req.getCookies();
		String clientId="";
		for(int x=0;x<cookies.length;x++)
		{
			if(cookies[x].getName().equals("WorldChat"))
			{
				clientId=cookies[x].getValue();
			}
		}
		System.err.println("client Id "+clientId+"*******************************************************");
		if(clientId=="")
		{
			//Something went wrong!
			System.err.println("couldn't find cookie*******************************************************");
			res.setContentType("text/plain");
			res.getWriter().print("Error: Could not find Cookie!");
		}
		System.err.println("above send message*******************************************************");
		ChatManager.sendMessage(req.getParameter("message"), clientId);
		res.setContentType("text/plain");
		res.getWriter().print("Success");
	}
}