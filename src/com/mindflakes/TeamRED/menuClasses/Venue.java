package com.mindflakes.TeamRED.menuClasses;
import java.util.ArrayList;

public class Venue {
	private String name;
	private ArrayList<FoodItem> foodItems;
	public Venue(String name, ArrayList<FoodItem> foodItems) {
		this.name = name;
		this.foodItems = foodItems;
	}
	public String getName() {
		return name;
	}
	public ArrayList<FoodItem> getFoodItems() {
		return foodItems;
	}
	public void addToItems(FoodItem toAdd){
		foodItems.add(toAdd);
	}
}
