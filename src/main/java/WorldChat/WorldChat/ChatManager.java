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
	
	/**
	 * Helper method that keeps chatHistory's char limit to historySize
	 * 
	 * @param addStr the string we will be adding to chatHistory
	 */
	private static void addToHistory(String addStr)
	{
		if(addStr.length() >= historySize)
		{
			chatHistory=addStr.substring(addStr.length()-historySize);
		}
		else if(chatHistory.length() + addStr.length() <= historySize)
			chatHistory+=addStr;
		else
		{
			int toRemove = chatHistory.length() + addStr.length() - historySize;
			chatHistory=chatHistory.substring(toRemove, chatHistory.length()) + addStr;
		}
	}
	
	/**
	 * Accessor function for chatHistory
	 * 
	 * @return the value of chatHistory
	 */
	public static String getChatHistory()
	{
		return chatHistory;
	}
	
	/**
	 * Checks if a client is subscribed
	 * 
	 * @param sub string of the ClientId
	 * @return true if client is subscribed, false if not
	 */
	public static boolean isSubscribed(String sub) 
	{
		return subs.contains(sub);
	}
	
	/**
	 * Removes a client from being subscribed
	 * 
	 * @param sub string of the ClientId that is subscribed
	 */
	public static void removeSub(String sub) 
	{
		subs.remove(sub);
	}
	
	/**
	 * Adds a client to the chat subscription
	 * 
	 * @param sub string of the ClientId that wants to be added
	 */
	public static void addSub(String sub) 
	{
		System.err.println("In adding a sub*******************************************************");
		subs.add(sub);
	}

	/**
	 * Sends the message to all clients subscribed
	 * 
	 * @param body body of the message to be sent out
	 * @param source the source of where the message is from
	 */
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
