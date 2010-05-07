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
<%@page import="java.util.ArrayList"%>
<%@page import="com.mindflakes.TeamRED.server.MealMenuSearchQuery"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Results</title>
</head>
<body>

<h1>Search Results:</h1>

<%

/*
Note about searching! start and stop must be the Millis from the date range. 
vegan and vegetarian must be true if you only want those items.
commons ignores case, but is an exact match only
the search string must use %1 instead of every & in the query
Spaces must also be replaced by +.
*/
String vegan = request.getParameter("vegan");
String vegetarian = request.getParameter("vegetarian");
String search = request.getParameter("search");
String start = request.getParameter("start");
String stop = request.getParameter("stop");
String commons = request.getParameter("commons");

try{
ArrayList<MealMenu> results = null;
long startMillis = (start!=null) ? Long.parseLong(start) : new DateTime().getMillis();
long endMillis = (stop!=null) ? Long.parseLong(stop) : new DateTime().plusWeeks(2).getMillis();
boolean veganBool = (vegan!=null)? Boolean.parseBoolean(vegan) : false;
boolean vgtBool = (vegan!=null) ? Boolean.parseBoolean(vegetarian) : false;
%>
<br><%= "vegan: "+veganBool+". vegetarian: "+vgtBool+". search: "+search+". start: "+start+". stop: "+stop %>
<br>
<%

MealMenuSearchQuery query = new MealMenuSearchQuery(new DateTime(startMillis), new DateTime(endMillis));
if(vgtBool) query = query.findVegetarian();
else if(veganBool) query = query.findVegan();
if(commons!=null && commons.length()>0) query=query.findCommons(commons);
if(search!=null && search.length()>0) query=query.findFoodItem(search.replace("%1","&").replace("+"," "));
results = query.returnResults();

for(MealMenu menu : results){
%>
<%= MealMenuUtil.mealMenuSimpleHTML(menu) %>
<br>
<%
}

}catch(NumberFormatException e){
	%>
	<br>
	<h1>Invalid Search Results! - Number Format Exception</h1>
	<%= "vegan: "+vegan+". vegetarian: "+vegetarian+". search: "+search+". start: "+start+". stop: "+stop %>
	<%
}

%>

</body>
</html>