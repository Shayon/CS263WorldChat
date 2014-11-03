package com.google.appengine.demos.guestbook;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class Enqueue extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
	 //System.err.println("Enqueue engaged");
     String name = request.getParameter("keyname");
     String value = request.getParameter("value");
     // Add the task to the default queue.
     Queue queue = QueueFactory.getDefaultQueue();
     queue.add(withUrl("/worker").param("keyname", name).param("value", value));

     response.sendRedirect("/tqueue.jsp?keyname="+name);
 }
}