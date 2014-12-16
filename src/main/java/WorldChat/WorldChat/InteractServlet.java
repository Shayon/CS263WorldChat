package WorldChat.WorldChat;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class InteractServlet extends HttpServlet {
   

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	resp.setContentType("text/plain");
    	Gson gson = new Gson();
    	gson.toJson(this);
        resp.getWriter().print(gson.toJson(ChatManager.getChatHistory()));
        
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
    {
    	Gson gson = new Gson();
    	String message=gson.fromJson(req.getParameter("message"), String.class);
    	ChatManager.sendMessage(message, "API User");
    }
}