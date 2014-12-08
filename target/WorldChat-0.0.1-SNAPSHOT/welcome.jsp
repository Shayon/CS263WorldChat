<%@ page import="java.util.*"%>
<html>
<head>
<link href="/stylesheets/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
	<div class="jumbotron" align="center">
		<h1>Welcome to World Chat!</h1>
		<h2>Hello Visitor from...</h2>
		<h3>Country: <%=request.getHeader("X-AppEngine-Country")%></h3>
		<!-- <h3>Region: <%=request.getHeader("X-AppEngine-Region")%></h3> -->
		<h3>City: <%=request.getHeader("X-AppEngine-City")%></h3>
		<!-- <h3>Latitude,Longitude: <%=request.getHeader("X-AppEngine-CityLatLong")%></h3> -->
	
		<br />
		<img src="http://maps.googleapis.com/maps/api/staticmap?markers=color:blue%7Clabel:X%7C<%=request.getHeader("X-AppEngine-CityLatLong")%>&zoom=12&size=400x400&sensor=false" class="img-circle"/>
		<br />
		<p>Did we find you correctly? If we did, then why don't you...</p>
		<button type="button" class="btn btn-info btn-lg" onclick="location.href='/worldmap';">Get Talking!</button>
	</div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="/stylesheets/dist/js/bootstrap.min.js"></script>

</body>
<html>