package com.mindflakes.TeamRED.server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MealMenuFeedRouter extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	throws IOException{
	final String path = req.getPathInfo();
	Pattern MEAL_COMMON_PATTERN = Pattern.compile("/feed/(.*?)");
	Matcher m = null;
	
	m = MEAL_COMMON_PATTERN.matcher(path);
	if (m.matches()) {
		final String common = m.group(1);
		resp.getWriter().print("Matched common: " + common);
	} else {
		resp.getWriter().print("No common matched.");
	}
	
	}
}
