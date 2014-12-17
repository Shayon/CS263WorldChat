<%@page import="WorldChat.WorldChat.ChatIdGen"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="WorldChat.WorldChat.ChatManager" %>
<html>
<!-- CODE ON THIS PAGE WAS SUPPLEMENTED BY https://github.com/kwhinnery/gae-chat -->
<head>
	<link href="/stylesheets/dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="/stylesheets/specialtext.css" rel="stylesheet">
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	
	 <script type="text/javascript" charset="utf-8">
		//handle incoming chat messages, pushed by App Engine
		function onMessage(message) {
			var current = $('#specialtextarea').html();
			$('#specialtextarea').html(current + '\n' + message.data);
			$("#specialtextarea").scrollTop($("#specialtextarea")[0].scrollHeight);
			
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
					$("#specialtextarea").scrollTop($("#specialtextarea")[0].scrollHeight);
				}
			});
			
			//handle submitting chat message
			function submitMessage() {
				$.ajax('/chat', {
					method:'POST',
					dataType:'text',
					data: {
						message:$('#messageInput').val()
					},
					success:function(response) {
						$('#messageInput').val('').focus();
						console.log(response);
					}
				});
			}
			
			//Attach event handlers
			$('#messageButton').on('click', submitMessage);
			$('#messageInput').keypress(function (e) {
				if (e.which == 13) {
					submitMessage();
				}
			});
		});
	</script> 
</head>

<body>
	<div class="jumbotron" align="center"> 
		<h1>Jump In And Chat!</h1>
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
				<div align="left" id="specialtextarea"><%= ChatManager.getChatHistory() %></div>
				<input id="messageInput" placeholder="enter a message..."/>
				<button id="messageButton" class="button">Send Message</button>
			</div>
		</section>
	</div>
	
	<%
    	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String fileName = String.valueOf(System.currentTimeMillis());
	%>
	<p>Share a file with the world!</p>
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            <input hidden="true" name="fileName" value="<%= fileName %>">
            <input type="file" name="<%= fileName %>">
            <button type="submit">Submit</button>
        </form>
	
</body>


</html>