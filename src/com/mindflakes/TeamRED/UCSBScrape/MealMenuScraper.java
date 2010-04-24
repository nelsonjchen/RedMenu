package com.mindflakes.TeamRED.UCSBScrape;

import java.io.InputStream;
import java.util.ArrayList;

import com.mindflakes.TeamRED.menuClasses.MealMenu;

public interface MealMenuScraper {
	ArrayList<MealMenu> parse(InputStream s);
}
