<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.mindflakes.TeamRED.server.DatastoreUpdater" %>
<%@page import="com.google.appengine.api.datastore.*"%>
<%@page import="com.vercer.engine.persist.annotation.AnnotationObjectDatastore" %>
<%@page import="com.mindflakes.TeamRED.menuClasses.*" %>
<%@page import="java.util.Iterator"%>
<%@page import="com.google.appengine.api.labs.taskqueue.Queue"%>
<%@page import="com.google.appengine.api.labs.taskqueue.QueueFactory"%>
<%@page import="com.google.appengine.api.labs.taskqueue.TaskOptions"%>


<%
DatastoreUpdater.deleteOldMealMenus();
%>