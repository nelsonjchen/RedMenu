package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.mindflakes.TeamRED.UCSBScrape.UCSBJMenuScraper;
import com.mindflakes.TeamRED.menuClasses.MealMenu;


public class UCSBMenuScraperTest {
	UCSBJMenuScraper scraper;
	ArrayList<MealMenu> menus;

	@Before
	public void setUp() {
		// Ortega Menu
		scraper = new UCSBJMenuScraper("docs/viewer.xml", 1);
		menus = scraper.getMenus();
	}

	@Test
	public void checkName() {
		assertEquals("Common Name Matches","Ortega",menus.get(0).getCommonsName());
	}
	
	@Test
	public void checkMealName() {
		assertEquals("Meal Name Matches","Breakfast",menus.get(0).getMealName());
	}

	@Test
	public void ensureVenuesPresent() {
		assertTrue("Venues are present", menus.get(0).getVenues().size() > 0);
	}
	
	@Test
	public void checkStartTimeForBreakfast() {
		DateTime common_open = new DateTime(2010, 4, 19, 7, 15, 0, 0);
		assertEquals("Start time is correct", common_open.getMillis(),
				menus.get(0).getMealInterval().getStartMillis());
	}

	@Test
	public void checkEndTimeForBreakfast() {
		DateTime common_close = new DateTime(2010, 4, 19, 10, 45, 0, 0);
		assertEquals("End time is correct", common_close.getMillis(),
				menus.get(0).getMealInterval().getEndMillis());
	}
	
}
