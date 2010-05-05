package com.mindflakes.TeamRED.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MealMenuUpcomingFeedServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

		String common = (String) req.getAttribute("common");
		resp.getWriter().print("got passed " + common);
		
	}
}
