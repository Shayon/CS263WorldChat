package WorldChat.WorldChat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;

public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    /**
     * Uploads the file the user selected into the blobstore and posts link to chat
     * <p>
     * If the blobKey is nul or if the Filename passed in is blank or if the clientId could not be found, we redirect to /map
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    	String fileName = req.getParameter("fileName");
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        Map<String, List<FileInfo>> fileInfos = blobstoreService.getFileInfos(req);
        List<BlobKey> keyList = blobs.get(fileName);
        List<FileInfo> infoList = fileInfos.get(fileName);
        BlobKey blobKey = keyList.get(0);
        FileInfo info = infoList.get(0);
        
        
        String clientId=ChatIdGen.getClientId(req);
        
        if (blobKey == null || info.getFilename().equals("") || clientId == "") 
        {
        	res.sendRedirect("/map");
            return;
        } 
        else 
        {
        	res.sendRedirect("/map");
        	String message="<a href=\"/serve?blob-key=" + blobKey.getKeyString() + "\" target=\"_blank\">" + clientId+" has uploaded an file to share, click here to get it!</a>";
            ChatManager.sendMessage(message,"SERVER");
            return;
        }
    }
}