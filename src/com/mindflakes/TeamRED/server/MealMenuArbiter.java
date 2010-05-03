package com.mindflakes.TeamRED.server;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.UCSBScrape.UCSBJMenuScraper;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class MealMenuArbiter {
	
	/**
	 * Stores Menu into datastore.
	 * @param filename
	 * @param mode
	 */
	public static void mergeMenu(String filename, int mode) {
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
		
		// Altered constructor of object to work with new constructor. 
		// mergeMenu method could be changed to instead accept a boolean input to make this cleaner
		ArrayList<MealMenu> menus = (new UCSBJMenuScraper(filename, (mode==1) ? true : false)).getMenus();
        for (MealMenu m : menus) {
        	datastore.store().instance(m).returnKeyNow();
        }
	}
	

}
