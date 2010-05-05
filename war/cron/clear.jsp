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
Queue queue = QueueFactory.getDefaultQueue();
String str = request.getParameter("count");
int i = 0;
try{
	i = Integer.parseInt(str);
} catch(Exception e){
	queue.add(TaskOptions.Builder.url("/cron/clear.jsp").param("count",""+1));
	queue.add(TaskOptions.Builder.url("/cron/clear.jsp").param("count",""+2));
}
DatastoreService service = DatastoreServiceFactory.getDatastoreService();
AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
boolean shouldContinue = false;
switch(i){
case 0:
	{
	Iterator<MealMenu> it = datastore.find().type(MealMenu.class).fetchResultsBy(5).returnResultsNow();
	if(it.hasNext()) shouldContinue=true;
	MealMenu mm;
	while(it.hasNext()){
		mm = it.next();
		datastore.deleteAll(mm.getVenues());
		datastore.delete(mm);
	}
	}
	break;
case 1:
	{
	Iterator<Venue> it = datastore.find().type(Venue.class).fetchResultsBy(20).returnResultsNow();
	if(it.hasNext()) shouldContinue=true;
	while(it.hasNext()){
		datastore.delete(it.next());
	}
	}
	break;
case 2:
	{
	Iterator<FoodItem> it = datastore.find().type(FoodItem.class).fetchResultsBy(20).returnResultsNow();
	if(it.hasNext()) shouldContinue=true;
	while(it.hasNext()){
		datastore.delete(it.next());
	}
	}
	break;
}
if(shouldContinue) queue.add(TaskOptions.Builder.url("/cron/clear.jsp").param("count",""+i));


%>