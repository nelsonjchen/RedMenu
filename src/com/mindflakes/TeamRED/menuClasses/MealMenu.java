package com.mindflakes.TeamRED.menuClasses;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import org.joda.time.format.DateTimeFormat;
import com.vercer.engine.persist.annotation.Key;

import java.util.ArrayList;

/** A container object that represents a specific meal at a specific dining common.
 * @author Johan Henkens
 *
 */
public class MealMenu {
	private String commonsName;
	private long startMillis, endMillis, modMillis;
	private ArrayList<Venue> venues;
	private String mealName;
	@SuppressWarnings("unused")
	@Key
	private String menukey;
	
	/** constructs a MealMenu object with the specified parameters. Time is passed to the constructor in the form returned by
	 * a call to Joda Time's {@link org.joda.time.DateTime#getMillis() DateTime.getMillis. This convention is used for the start, end, and modification times
	 * @param commonsName Name of the dinning common at which this meal occurs. Should not contain 'Commons' at the end.
	 * @param startMillis number of milliseconds after the java epoch at which this meal starts
	 * @param endMillis number of milliseconds after the java epoch at which this meal end
	 * @param modMillis number of milliseconds after the java epoch at which this MealMenu was modified. Mostly used with a parser in
	 * which the modification date depends on when the input file was created.
	 * @param venues arrayList of Venues that will be serving food at this meal.
	 * @param mealName name of the meal being served (for example, "Breakfast").
	 */
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
	
	/** gets the name of the dining commons at which this meal occurs. For example, "Carrillo" or "Ortega".
	 * @return name of the dining commons at which this meal occurs
	 */
	public String getCommonsName() {
		return commonsName;
	}
	/** retrieves all the venues which will be serving food at this meal
	 * @return the venues which will be serving food at this meal.
	 */
	public ArrayList<Venue> getVenues() {
		return venues;
	}
	
	/**	gets the name of this meal. For example, "Dinner", "Breakfast" or "Late Night"
	 * @return name of this meal
	 */
	public String getMealName() {
		return mealName;
	}
	
	/**	creates and return an <code>Interval</code> object from the start and end values of this meal. The <code>Interval</code> represents the time 
	 * during which the meal is being served.
	 * @return interval the <code>Interval</code> representing the time during which the meal is served
	 */
	public Interval getMealInterval(){
		return new Interval(startMillis, endMillis);
	}
	
	/** creates and returns a <code>DateTime</code> object from the modTime of this meal. 
	 * The <code>DateTime</code> is the time at which this <code>MealMenu</code> object's information was last modified.
	 * @return the DateTime at which this MealMenu was last modified.
	 */
	public DateTime getModDate() {
		return new DateTime(modMillis);
	}
	
	/**	sets the modDate of this <code>MealMenu</code> to the specified modDate
	 * @param modDate <code>DateTime</code> containing the modDate that will be used
	 */
	public void setModDate(DateTime modDate) {
		this.modMillis = modDate.getMillis();
	}
	
	/** sets the modDate of this <code>MealMenu</code> to the specified modDate
	 * @param modMilis time represented by the milliseonds since the epoch of java to use as the new modification date
	 */
	public void setModDate(long modMillis){
		this.modMillis=modMillis;
	}

}
