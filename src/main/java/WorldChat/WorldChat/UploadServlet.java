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

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myImage");
        
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
        	res.sendRedirect("/map");
        	String message=clientId+" has uploaded an file to share, get it at http://worldchat263.appspot.com/serve?blob-key="+ blobKey.getKeyString();
            ChatManager.sendMessage(message,"SERVER");
            return;
        }
    }
}