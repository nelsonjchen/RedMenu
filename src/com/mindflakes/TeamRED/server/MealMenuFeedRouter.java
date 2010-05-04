package com.mindflakes.TeamRED.server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apphosting.utils.remoteapi.RemoteApiPb.Request;

@SuppressWarnings("serial")
public class MealMenuFeedRouter extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	throws IOException, ServletException{
	final String path = req.getPathInfo();
	Pattern MEAL_COMMON_PATTERN = Pattern.compile("/(\\w+)");
	
	Matcher m = null;
	m = MEAL_COMMON_PATTERN.matcher(path);
	resp.getWriter().print("current path: " + path + "\n");
	if (m.matches()) {
		final String common = m.group(1);
		req.setAttribute("common", common);
		getServletContext()
		.getNamedDispatcher("MealMenuUpcomingFeedServlet")
		.forward(req, resp);
	} else {
		resp.getWriter().print("Common not matched.");
	}
	
	}
}
