package com.mindflakes.TeamRED.menuClasses;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.appengine.repackaged.org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

public class MealMenu {
	private String commonsName;
	private long startMillis, endMillis, modMillis;
	private ArrayList<Venue> venues;
	private String mealName;
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
	
	private void setmenuKey() {
		this.menukey =  this.commonsName.toLowerCase() +
						this.mealName.toLowerCase() +
						DateTimeFormat.forPattern("MMddyyyy")
						.print(startMillis);
	}
	
	public String getCommonsName() {
		return commonsName;
	}
	public ArrayList<Venue> getVenues() {
		return venues;
	}
	public String getMealName() {
		return mealName;
	}
	
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
