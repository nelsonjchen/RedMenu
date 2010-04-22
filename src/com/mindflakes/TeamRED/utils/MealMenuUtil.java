package com.mindflakes.TeamRED.utils;

import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.menuClasses.Venue;

public  class MealMenuUtil {
	
	public static String mealMenuString(MealMenu menu) {
		String append = "*";
		append += menu.getCommonsName() + "*\n";
		for (Venue v : menu.getVenues()) {
			append += "\\" + v.getName() + "\n";
			for (FoodItem f: v.getFoodItems()) {
				append += "*" + f.getName() + "\n";
			}
		}
		return append;
	}
}
