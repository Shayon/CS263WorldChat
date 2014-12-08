<%@ page import="java.util.*"%>
<html>
<head>
	<link href="/stylesheets/dist/css/bootstrap.min.css" rel="stylesheet">
	
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	
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
	<div id="container">
		<header>
			<div id="header">
				<h1 class="section-heading">App Engine Chat</h1>
			</div>
		</header>
		<section>
			<div id="content">
				<h2 class="section-heading">Chat via this web page.</h2>
				<!-- Chat inputs/output -->
				<textarea readonly>Awaiting messages...</textarea>
				<input placeholder="enter a message..."/>
				<button class="button">Send Message</button>
			</div>
		</section>
		<footer>
			<p class="footer">
				Source on <a href="https://github.com/Shayon/CS263WorldChat">Github</a>.
			</p>
		</footer>
	</div>
	
</body>


</html>