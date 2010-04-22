package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;


import org.joda.time.DateTime;
import org.junit.Test;

import com.mindflakes.TeamRED.UCSBScrape.UCSBMenuScraper;
import com.mindflakes.TeamRED.menuClasses.MealMenu;


public class UCSBMenuScraperTest {
	ArrayList<MealMenu> menus;
	
	public void setUp() {
//		Carrillo
        
	}
	
	@Test
	public void checkName() {
		new UCSBMenuScraper("docs/viewer.xml", 1);
        menus = UCSBMenuScraper.getMealMenu();
		assertEquals("Common Name Matches","Carrillo",menus.get(0).getCommonsName());
	}
}
