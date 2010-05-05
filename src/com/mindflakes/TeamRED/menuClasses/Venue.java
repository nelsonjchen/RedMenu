package com.mindflakes.TeamRED.menuClasses;
import java.util.ArrayList;

import com.vercer.engine.persist.annotation.Parent;

/** A container class that holds all the {@link com.mindflakes.TeamRED.menuClasses.FoodItem FoodItem}s
 * that a specific venue are serving. An example of a venue would be "Bakery" or "Grill" and all the FoodItems served there..
 * @author Johan Henkens
 *
 */
public class Venue {
	@Parent
	private MealMenu parent;
	
	protected void setParent(MealMenu parent){
		this.parent=parent;
	}
	
	private String name;
	private ArrayList<FoodItem> foodItems;
	/** Constructs a <code>Venue</code> with the specified name and FoodItems to be served.
	 * @param name name of the venue at which the food is served
	 * @param foodItems ArrayList of FoodItems that will be served at this <code>Venue</code>.
	 */
	public Venue(String name, ArrayList<FoodItem> foodItems) {
		this.name = name;
		this.foodItems = foodItems;
	}
	
	@SuppressWarnings("unused")
	private Venue() {
		
	}
	
	/** returns the name of this <code>Venue</code>. For example, "Grill" or "Bakery".
	 * @return the name of this <code>Venue</code>.
	 */
	public String getName() {
		return name;
	}
	
	
	/** returns the FoodItems that are served at this <code>Venue</code>.
	 * @return the FoodItems that are served at this <code>Venue</code>.
	 */
	public ArrayList<FoodItem> getFoodItems() {
		return foodItems;
	}
	/** adds the specified FoodItem to the existing collection of FoodItems that are served at this <code>Venue</code>.
	 * @param toAdd the specified FoodItem to be added.
	 */
	public void addToItems(FoodItem toAdd){
		foodItems.add(toAdd);
	}
}
