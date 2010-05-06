package com.mindflakes.TeamRED.utils;

import java.util.ArrayList;

import org.joda.time.format.DateTimeFormat;

import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.menuClasses.Venue;

public  class MealMenuUtil {
	
    /** Prints each <code>MealMenu</code> in this Scraper to <code>System.out</code> in a clean format. 
     * 
     */
    public static void printAll(ArrayList<MealMenu> menus){
    	for(MealMenu menu : menus){
    		System.out.println("Commons: " + menu.getCommonsName());
    		System.out.println("Start Time: " + DateTimeFormat.mediumDateTime().print(menu.getMealInterval().getStart()));
    		System.out.println("End Time: " + DateTimeFormat.mediumDateTime().print(menu.getMealInterval().getEnd()));
    		System.out.println("Mod Time: " + DateTimeFormat.mediumDateTime().print(menu.getModDate()));
    		System.out.println("Meal Name: " + menu.getMealName());
    		System.out.println("Venues: ");
    		for(Venue ven : menu.getVenues()){
    			System.out.println("    Venue Name: " + ven.getName());
    			System.out.println("    Food Items:");
    			for(FoodItem food : ven.getFoodItems()){
    				System.out.println("        " + food.getName()+" " + food.isVegan() +" "+ food.isVegetarian());
    			}
    		}
    	}
    }

	
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
		append += "<p>" + DateTimeFormat.mediumDateTime()
				.print(menu.getMealInterval().getStartMillis())
				+ " to "
				+ DateTimeFormat.shortTime()
				.print(menu.getMealInterval().getEndMillis()) 
				
				+ "</p>\n";
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
	
	public static String mealMenuSimpleRSSHTML(MealMenu menu) {
		String append = "";

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
