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
	
	public static String mealMenuSimpleHTML(MealMenu menu) {
		String append = "";
		append += "<h2>" + menu.getCommonsName() + "</h2>\n";
		append += "<h3>" + menu.getMealName() + "</h3>\n";
		append += "<p>" + menu.getMealInterval() + "</p>\n";
		for (Venue v : menu.getVenues()) {
			append += "<h4>" + v.getName() + "</h4>\n";
			append += "<ul>\n";
			for (FoodItem f: v.getFoodItems()) {
				append += "<li>" + f.getName() + "</li>\n";
			}
			append += "</ul>\n";
		}
		return append;
	}
}