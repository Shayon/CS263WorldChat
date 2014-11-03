package com.google.appengine.demos.guestbook;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;


//The Worker servlet should be mapped to the "/worker" URL.
public class Worker extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     String key = request.getParameter("keyname");
     String value = request.getParameter("value");
     System.err.println("********************************Value is "+value); 
     if(!value.equals(""))
     {
    	 MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    	 syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    	 if(!value.equals(syncCache.get(key)))
    	 {
    		 syncCache.put(key, value);
    		 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        	 Key tqKey = KeyFactory.createKey("taskData", key);
        	 Entity ent = new Entity(tqKey);
        	 ent.setProperty("value", value);
        	 datastore.put(ent);
    	 }
    	 
     }  
 }
}