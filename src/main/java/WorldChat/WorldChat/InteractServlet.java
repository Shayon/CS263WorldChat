package WorldChat.WorldChat;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.gson.Gson;

public class InteractServlet extends HttpServlet {
   

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	resp.setContentType("text/plain");
    	Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(ChatManager.getChatHistory()));
        
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
    {
    	Gson gson = new Gson();
    	String message=gson.fromJson(req.getParameter("message"), String.class);
    	message=StringEscapeUtils.escapeHtml(message);
    	Queue queue = QueueFactory.getDefaultQueue();
        queue.add(withUrl("/worker").param("message", message).countdownMillis(500));
    	
    	
    }
}