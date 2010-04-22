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
//		Carrillo
		new UCSBMenuScraper("docs/viewer.xml", 1);
        menus = UCSBMenuScraper.getMealMenu();
	}
	
	@Test
	public void checkName() {
		
		assertEquals("Common Name Matches","Ortega",menus.get(0).getCommonsName());
	}
	
	@Test
	public void checkStartTimeForBreakfast() {
		
	}
}
