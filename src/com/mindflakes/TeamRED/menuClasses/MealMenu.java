package com.mindflakes.TeamRED.menuClasses;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import java.util.ArrayList;

public class MealMenu {
	private String commonsName;
	private Interval dateTimeInterval;
	private ArrayList<Venue> venues;
	private String mealName;
	private DateTime modDate;
	public MealMenu(String commonsName, Interval dateTimeInterval,
			ArrayList<Venue> venues, String mealName, DateTime modDate) {
		this.commonsName = commonsName;
		this.dateTimeInterval = dateTimeInterval;
		this.venues = venues;
		this.mealName = mealName;
		this.modDate = modDate;
	}
	public String getCommonsName() {
		return commonsName;
	}
	public Interval getDateTimeInterval() {
		return dateTimeInterval;
	}
	public ArrayList<Venue> getVenues() {
		return venues;
	}
	public String getMealName() {
		return mealName;
	}
	public DateTime getModDate() {
		return modDate;
	}
	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

}
