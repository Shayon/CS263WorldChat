package WorldChat.WorldChat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    /**
     * Uploads the file the user selected into the blobstore and posts link to chat
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");
        
        String clientId=ChatIdGen.getClientId(req);
        if(clientId == "")
        	clientId = "Unkown User";
        if (blobKey == null) 
        {
        	res.sendRedirect("/map");
            return;
        } 
        else 
        {
        	System.err.println("about to upload and post message**********************************");
        	res.sendRedirect("/map");
        	String message="<a href=\"/serve?blob-key=" + blobKey.getKeyString() + "\" target=\"_blank\">" + clientId+" has uploaded an file to share, click here to get it!</a>";
            ChatManager.sendMessage(message,"SERVER");
            return;
        }
    }
}