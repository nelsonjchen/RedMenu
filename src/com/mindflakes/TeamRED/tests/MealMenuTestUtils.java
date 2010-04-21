package com.mindflakes.TeamRED.tests;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.menuClasses.Venue;


public class MealMenuTestUtils {

	public static MealMenu createTestMenu(){
		ArrayList<Venue> vens = new ArrayList<Venue>();
		ArrayList<FoodItem> food = new ArrayList<FoodItem>();
		food.add(new FoodItem(
				"Vegan Oatmeal", true, true
		));
		food.add(new FoodItem(
				"Breakfast Burrito", false, false
		));
		food.add(new FoodItem(
				"Canadian Bacon", false, false
		));
		food.add(new FoodItem(
				"Make Your Own Waffles", false, true
		));
		food.add(new FoodItem(
				"Fresh Scrambled Eggs", false, true
		));
		food.add(new FoodItem(
				"Hash Browns", true, true
		));
		vens.add(new Venue(
				"Grill (Cafe)", food
		));
		food = new ArrayList<FoodItem>();
		food.add(new FoodItem(
				"Bagels and Cream Cheeze", true, true
		));
		food.add(new FoodItem(
				"Choc Chip Pecan Coffeecake (contains Nuts)", false, true
		));
		vens.add(new Venue(
				"Bakery", food
		));
	
		MealMenu menu = new MealMenu(
				"Carrillo", 
				(new DateTime(
						2010, 4, 12, 7, 15, 00, 00)).getMillis(),
						(new DateTime(
								2010, 4, 12, 10, 00, 00, 00)).getMillis(),
								(new DateTime(
										2010, 4, 8, 22, 15, 13, 00)).getMillis(),
										vens,
										"Breakfast"
		);
		return menu;
	}

}
