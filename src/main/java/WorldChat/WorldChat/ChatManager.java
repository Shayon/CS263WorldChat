package WorldChat.WorldChat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;


public class ChatManager 
{
	private static HashSet<String> subs = new HashSet<String>();
	private static String chatHistory="Start the Conversation!";
	private static final int historySize=1500;
	
	private static void addToHistory(String addStr)
	{
		if(chatHistory.length() + addStr.length() <= historySize)
			chatHistory+=addStr;
		else
		{
			int toRemove = chatHistory.length() + addStr.length() - historySize;
			chatHistory=chatHistory.substring(toRemove, chatHistory.length()) + addStr;
		}
	}
	
	public static String getChatHistory()
	{
		return chatHistory;
	}
	
	//Check subscription
	public static boolean isSubscribed(String sub) 
	{
		return subs.contains(sub);
	}
	
	//Remove subscription
	public static void removeSub(String sub) 
	{
		subs.remove(sub);
	}
	
	//Add a new subscription
	public static void addSub(String sub) 
	{
		System.err.println("In adding a sub*******************************************************");
		subs.add(sub);
	}

	// Send out a given message to all subscribers
	public static void sendMessage(String body, String source) 
	{
		Iterator<String> it = subs.iterator();
		System.err.println("In sendMessage*******************************************************");
		System.err.println("subs info "+subs.size()+"*******************************************");
		addToHistory("\n"+source + ": " + body);
		while (it.hasNext()) 
		{	
			String sub = it.next();
			System.err.println("Sending message "+sub+"*******************************************************");
			String messageBody = source + ": " + body;

			ChannelService channelService = ChannelServiceFactory.getChannelService();
			channelService.sendMessage(new ChannelMessage(sub,messageBody));			
		}
	}
}
