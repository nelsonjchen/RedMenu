package com.mindflakes.TeamRED.menuClasses;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import org.joda.time.format.DateTimeFormat;
import com.vercer.engine.persist.annotation.Key;

import java.util.ArrayList;

public class MealMenu {
	private String commonsName;
	private long startMillis, endMillis, modMillis;
	private ArrayList<Venue> venues;
	private String mealName;
	@SuppressWarnings("unused")
	@Key
	private String menukey;
	
	public MealMenu(String commonsName, long startMillis, long endMillis,
			long modMillis, ArrayList<Venue> venues, String mealName) {
		this.commonsName = commonsName;
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		this.modMillis = modMillis;
		this.venues = venues;
		this.mealName = mealName;
		setmenuKey();
	}
	
	@SuppressWarnings("unused")
	private MealMenu(){
		this.commonsName = null;
		this.venues = new ArrayList<Venue>();
		this.mealName = null;
		this.modMillis = 0;
		this.startMillis = 0;
		this.endMillis = 0;
		
	}
	
	/**
	 * This method sets a key for unique persistance.
	 */
	private void setmenuKey() {
		this.menukey =  this.commonsName.toLowerCase() +
						this.mealName.toLowerCase() +
						DateTimeFormat.forPattern("MMddyyyy")
						.print(startMillis);
	}
	
	/**
	 * @return common name like "Ortega" or "Carrillo"
	 */
	public String getCommonsName() {
		return commonsName;
	}
	public ArrayList<Venue> getVenues() {
		return venues;
	}
	
	/**
	 * @return name of meal like "Dinner" or "Breakfast" or "Late Night"
	 */
	public String getMealName() {
		return mealName;
	}
	
	/**
	 * @return interval 
	 */
	public Interval getMealInterval(){
		return new Interval(startMillis, endMillis);
	}
	
	public DateTime getModDate() {
		return new DateTime(modMillis);
	}
	
	public void setModDate(DateTime modDate) {
		this.modMillis = modDate.getMillis();
	}

}
