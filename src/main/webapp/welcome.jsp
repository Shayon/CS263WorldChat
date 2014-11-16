<%@ page import="java.util.*" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<h1> Welcome to World Chat! </h1>

<pre>request.getHeader("X-AppEngine-Country")</pre>
<pre>request.getHeader("X-AppEngine-Region")</pre>
<pre>request.getHeader("X-AppEngine-City")</pre>
<pre>request.getHeader("X-AppEngine-CityLatLong")</pre>
<h2>Location Information </h2>
<h3> Country: <%=request.getHeader("X-AppEngine-Country") %></h3>
<h3> Region: <%=request.getHeader("X-AppEngine-Region") %></h3>
<h3> City: <%=request.getHeader("X-AppEngine-City") %></h3>
<h3> Latitude,Longitude: <%=request.getHeader("X-AppEngine-CityLatLong") %></h3>
<br/>
<img src="http://maps.googleapis.com/maps/api/staticmap?markers=color:blue%7Clabel:O%7C<%=request.getHeader("X-AppEngine-CityLatLong")%>&zoom=12&size=400x400&sensor=false"/>
<br/>
</body>
<html>