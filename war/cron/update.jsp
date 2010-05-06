<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.mindflakes.TeamRED.server.DatastoreUpdater" %>
<%@page import="com.google.appengine.api.labs.taskqueue.Queue"%>
<%@page import="com.google.appengine.api.labs.taskqueue.QueueFactory"%>
<%@page import="com.google.appengine.api.labs.taskqueue.TaskOptions"%>

<%
//To update on a local version of the server: Run /cron/update.jsp?count=-1
//If that is taking too long and timing out, can call each menu individually with
// /cron/update.jsp?count=-1&menu= : 
// 11/21/31/41 or 12/22/32/42 for Carrillo/DLG/Ortega/Portla this week or (same order) next week
String str = request.getParameter("count");
int count = 9001;
try{
	count = Integer.parseInt(str);
} catch(Exception e){
}

int menu = -1;
try{
	menu = Integer.parseInt(request.getParameter("menu"));
} catch(Exception e){
	
}

Queue queue = QueueFactory.getDefaultQueue();
if(count==9001){
	if(menu==-1){
	//If no paramaters, start update for remote setting on all menus starting at the first menu
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","11"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","12"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","21"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","22"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","31"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","32"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","41"));
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu","42"));
	} else{
		//Othewrise, if no count parameter, but there is a menu parameter, start just that menu updating
		queue.add(TaskOptions.Builder.url("/cron/update.jsp").param("count","0").param("menu",""+menu));
	}
} else if(count!=-1){
	//If there is a count parameter that is not -1...
	
	//And there is a menu parameter telling us what that count is working on..
	if(menu != -1){
		//work on that menu!
		DatastoreUpdater.updateDatastore(count,menu);
	}
} else{
	DatastoreUpdater.updateDatastoreLocal();
}
%>

Updating!