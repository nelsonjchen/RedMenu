package com.mindflakes.TeamRED.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.utils.MealMenuUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

@SuppressWarnings("serial")


public class MealMenuUpcomingFeedServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MealMenu.class.getName());

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		String common = (String) req.getAttribute("common");
		
		resp.setContentType("application/xml; charset="
                + "UTF-8");
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");

        feed.setTitle("Upcoming meals for " + common);
        feed.setLink("http://csproj.mindflakes.com");
        feed.setDescription("This is a feed of current and upcoming meals " +
        		"for a dining common: + " + common + ".");

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);

        int count = 0;
        
        DateTime time = new DateTime();
        
        Iterator<MealMenu> future_menus = datastore.find()
        .type(MealMenu.class)
        .addFilter("endMillis",
        		com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, time.getMillis())
        .addSort("endMillis")
        .returnResultsNow();
        
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        
        while (future_menus.hasNext() && count < 3) {
        	MealMenu menu = future_menus.next();
        	SyndEntry entry;
        	SyndContent description;
        	entry = new SyndEntryImpl();
        	entry.setTitle(menu.getCommonsName() + " " + menu.getMealName() + " "+
        			DateTimeFormat.mediumDateTime()
    				.print(menu.getMealInterval().getStartMillis()));
        	entry.setPublishedDate(new Date());
        	description = new SyndContentImpl();
        	description.setType("text/html");
        	//description.setValue(MealMenuUtil.mealMenuSimpleRSSHTML(menu));
        	entry.setDescription(description);
        	entries.add(entry);
        	count++;
        }

        feed.setEntries(entries);

        SyndFeedOutput output = new SyndFeedOutput();
        try {
            output.output(feed, resp.getWriter());
        } catch (FeedException ex) {
        	resp.getWriter().print("failed to produce RSS");
        	log.severe("Unable to produce RSS feed.");
        }
	}
}
