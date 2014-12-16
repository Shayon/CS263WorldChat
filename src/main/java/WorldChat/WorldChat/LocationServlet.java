package WorldChat.WorldChat;

import java.io.IOException;

import javax.servlet.http.*;

public class LocationServlet extends HttpServlet
{
	/**
	 * simply serves to redirect to worldmap.jsp
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.sendRedirect("/worldmap.jsp");
	}
	
}
