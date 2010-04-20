package com.mindflakes.TeamRED.menuClasses;

public class FoodItem {
	private String name;
	boolean vegan;
	boolean vegetarian;
	public FoodItem(String name, boolean vegan, boolean vegetarian) {
		this.name = name;
		this.vegan = vegan;
		this.vegetarian = vegetarian;
	}
	
	@SuppressWarnings("unused")
	private FoodItem() {
		
	}
	
	public String getName() {
		return name;
	}
	public boolean isVegan() {
		return vegan;
	}
	public boolean isVegetarian() {
		return vegetarian;
	}
}
