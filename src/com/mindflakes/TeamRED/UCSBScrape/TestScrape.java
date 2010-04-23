package com.mindflakes.TeamRED.UCSBScrape;

import java.util.ArrayList;

import com.mindflakes.TeamRED.menuClasses.MealMenu;

public class TestScrape {

	public static void main(String[] args) {
            new UCSBMenuScraper("http://localhost/carrillo.xml", 0);
            //UCSBMenuScraper menu = new UCSBMenuScraper("https://docs.google.com/viewer?url=http://www.housing.ucsb.edu/dining/menu/ortega/ThisWeekMenu.pdf&a=gt", 0);
            ArrayList<MealMenu> menus = UCSBMenuScraper.getMealMenu();
            menus.toString();
	}

}
