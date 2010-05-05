<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.mindflakes.TeamRED.server.DatastoreUpdater" %>
<%@page import="com.google.appengine.api.datastore.*"%>
<%@page import="com.vercer.engine.persist.annotation.AnnotationObjectDatastore" %>

<%

String str = request.getParameter("count");
int i = 0;
try{
	i = Integer.parseInt(str);
} catch(Exception e){
	
}
DatastoreService service = DatastoreServiceFactory.getDatastoreService();
AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);

%>