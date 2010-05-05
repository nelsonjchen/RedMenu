<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.mindflakes.TeamRED.server.DatastoreUpdater" %>

<%
String str = request.getParameter("count");
int i = 0;
try{
	i = Integer.parseInt(str);
} catch(Exception e){
	
}
DatastoreUpdater.updateDatastore(i);

%>