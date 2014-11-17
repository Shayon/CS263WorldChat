<%@ page import="java.util.*" %>
<html>
<head>
    <link href="/stylesheets/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="jumbotron">
<h1> Welcome to World Chat!! </h1>
<h2>Location Information </h2>
<h3> Country: <%=request.getHeader("X-AppEngine-Country") %></h3>
<h3> Region: <%=request.getHeader("X-AppEngine-Region") %></h3>
<h3> City: <%=request.getHeader("X-AppEngine-City") %></h3>
<h3> Latitude,Longitude: <%=request.getHeader("X-AppEngine-CityLatLong") %></h3>
</div>
<br/>
<img src="http://maps.googleapis.com/maps/api/staticmap?markers=color:blue%7Clabel:X%7C<%=request.getHeader("X-AppEngine-CityLatLong")%>&zoom=12&size=400x400&sensor=false"/>
<br/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="/stylesheets/dist/js/bootstrap.min.js"></script>

</body>

<html>