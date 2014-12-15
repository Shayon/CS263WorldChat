<%@ page import="java.util.*"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="WorldChat.WorldChat.ChatManager" %>
<html>
<head>
	<link href="/stylesheets/dist/css/bootstrap.min.css" rel="stylesheet">
	
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	
	<script type="text/javascript" charset="utf-8">
		document.getElementById("textarea").scrollTop = document.getElementById("textarea").scrollHeight 
	</script>
	 <script type="text/javascript" charset="utf-8">
		//handle incoming chat messages, pushed by App Engine
		function onMessage(message) {
			var current = $('textarea').val();
			message && $('textarea').val(current + '\n' + message.data);
		}
	
		//Initialize our little chat application
		$(function() {
			//Get a client token to use with the channel API
			$.ajax('/chat',{
				method:'GET',
				dataType:'text',
				success: function(token) {
					console.log(token);
					var channel = new goog.appengine.Channel(token);
					var socket = channel.open();
					
					//Assign our handler function to the open socket
					socket.onmessage = onMessage;
				}
			});
			
			//handle submitting chat message
			function submitMessage() {
				$.ajax('/chat', {
					method:'POST',
					dataType:'text',
					data: {
						message:$('input').val()
					},
					success:function(response) {
						$('input').val('').focus();
						console.log(response);
					}
				});
			}
			
			//Attach event handlers
			$('button').on('click', submitMessage);
			$('input').keypress(function (e) {
				if (e.which == 13) {
					submitMessage();
				}
			});
		});
	</script> 
</head>

<body>
	<div class="jumbotron" align="center"> 
		<h1>Welcome to World Chat, The Second page!</h1>
	</div>
	<div id="container" align="center">
		<header>
			<div id="header">
				<h1 class="section-heading">App Engine Chat</h1>
			</div>
		</header>
		<section>
			<div>
				<h2 class="section-heading">Chat via this web page.</h2>
				<!-- Chat inputs/output -->
				<textarea readonly rows="9" cols="60"  ><%= ChatManager.getChatHistory() %></textarea>
				<input placeholder="enter a message..."/>
				<button class="button">Send Message</button>
			</div>
		</section>
	</div>
	
	<%
    	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	%>
	<p>Share a picture</p>
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            <input type="text" name="foo">
            <input type="file" name="myImage">
            <input type="submit" value="Submit">
        </form>
	
</body>


</html>