<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.format.DateTimeFormat"%>
<%@page import="com.mindflakes.TeamRED.menuClasses.MealMenu"%>
<%@page import="com.mindflakes.TeamRED.utils.MealMenuUtil"%>
<%@page import="com.mindflakes.TeamRED.tests.MealMenuTestUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.google.appengine.api.datastore.*"%>
<%@page import="com.vercer.engine.persist.annotation.AnnotationObjectDatastore" %>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upcoming Meals</title>
</head>

<body>
<script src="https://www.google.com/jsapi"></script>
<script>
google.load('jquery', '1.3.1');
</script>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p><%= user.getNickname() %> |
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a>.</p>
<%
    } else {
%>
<p>Not logged in |
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></p>
<%
    }
%>

<% 
DateTime time = new DateTime(); 
%>
Current Time is <%= DateTimeFormat.mediumDateTime().print(time) %> </br>

<h1>10 Upcoming Meals</h1>

<%

DatastoreService service = DatastoreServiceFactory.getDatastoreService();
AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);

Iterator<MealMenu> future_menu = datastore.find()
.type(MealMenu.class)
.addFilter("endMillis",
		com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, time.getMillis())
.addSort("endMillis")
.returnResultsNow();
%>

<% int count = 0; %>
<% while(future_menu.hasNext() && (count <= 8) ){ %>
<%= MealMenuUtil.mealMenuSimpleHTML(future_menu.next()) %>
<% count++; } %>
<h1>Search</h1>

<form action="search.jsp" method="get">

	<p>Search:</p>
	<p><input type="text" name="search" value="" /></p>

</form>

<a href="/feed/Ortega">Vegan Items</a>  <br/>

<h1>RSS Feeds</h1>

<a href="/feed/Ortega">Feed for Ortega</a>  <br/>

<a href="/feed/Carrillo">Feed for Carrillo</a>  <br/>

<a href="/feed/De La Guerra">Feed for DLG</a>  <br/>

<a href="/feed/Portola">Feed for Portola</a>  <br/>


</body>
</html>