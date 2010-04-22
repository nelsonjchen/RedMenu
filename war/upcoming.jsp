<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.joda.time.DateTime"%>
<%@page import="com.mindflakes.TeamRED.menuClasses.MealMenu"%>
<%@page import="com.mindflakes.TeamRED.utils.MealMenuUtil"%>
<%@page import="com.mindflakes.TeamRED.tests.MealMenuTestUtils"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upcoming Meals</title>
</head>
<body>
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
DateTime time = new DateTime(2010, 4, 12, 7, 15, 00, 00); 
%>
Current (Fake) Time is <%= time %> </br>

<h1>Upcoming Menu</h1>

<%= MealMenuUtil.mealMenuSimpleHTML((MealMenuTestUtils.createTestMenu())) %>


</body>
</html>