package WorldChat.WorldChat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Processes the task queue, posts each message from the API User's to the chat.
 */
public class Worker extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
 {
     String message = request.getParameter("message");
     ChatManager.sendMessage(message, "API User");
 }
}