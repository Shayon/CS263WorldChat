package WorldChat.WorldChat;

import javax.servlet.http.HttpServletRequest;

public class ChatIdGen 
{

	public static String generateId(HttpServletRequest req)
	{
		String country=req.getHeader("X-AppEngine-Country");
		String city=req.getHeader("X-AppEngine-City");
		//ONLY WORKS IF ONLY 1 PERSON FROM EACH CITY!!!
		//Should handle if either are nulls!
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
		return retstr;
	}
}
