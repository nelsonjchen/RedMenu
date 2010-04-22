package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.mindflakes.TeamRED.UCSBScrape.UCSBMenuScraper;
import com.mindflakes.TeamRED.menuClasses.MealMenu;


public class UCSBMenuScraperTest {
	ArrayList<MealMenu> menus;

	@Before
	public void setUp() {
		// Ortega Menu
		new UCSBMenuScraper("docs/viewer.xml", 1);
		menus = UCSBMenuScraper.getMealMenu();
	}

	@Test
	public void checkName() {
		assertEquals("Common Name Matches","Ortega",menus.get(0).getCommonsName());
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
