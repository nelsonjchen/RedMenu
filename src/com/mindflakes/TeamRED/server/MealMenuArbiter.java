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
		
		ArrayList<MealMenu> menus = (new UCSBJMenuScraper(filename, mode)).getMenus();
        for (MealMenu m : menus) {
        	datastore.store().instance(m).returnKeyNow();
        }
	}
	

}
