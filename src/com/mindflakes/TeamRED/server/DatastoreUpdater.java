package com.mindflakes.TeamRED.server;
//
//import com.google.appengine.api.users.User;
//import com.google.appengine.api.users.UserService;
//import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.*;
import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import com.mindflakes.TeamRED.utils.MealMenuUtil;
//import com.mindflakes.TeamRED.tests.MealMenuTestUtils;
import com.mindflakes.TeamRED.UCSBScrape.*;
import com.mindflakes.TeamRED.menuClasses.*;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
/**
 * @author Johan Henkens
 *	Utility class used for managing the datastore for the REDMenu app.
 */
public class DatastoreUpdater {

	/**
	 * Method to be used ONLY WHEN USING THE LOCAL DEVELOPMENT SUITE.
	 * Requests will be FAR TOO LARGE for the deployed app
	 */
	public static void updateDatastoreLocal(){
		//Create Queue and menus
		Queue queue = QueueFactory.getDefaultQueue();
		ArrayList<MealMenu> menus;
		UCSBJMenuScraper scraper;
		for(int menuCode=11;menuCode<=42;menuCode++){
			System.out.println(menuCode);
			scraper = new UCSBJMenuScraper(new RemoteUCSBMenuFile(getCommonsURL(menuCode)));
			menus = scraper.getMenus();
			updateDatastoreHelper(0, menus.size(), menus);
			if(menuCode%2==0)menuCode+=8;
		}
	}
	
	/**
	 * Method to be used ONLY ON DEPLOYED APPLICATION.
	 * Local suit does NOT support the Task Queue!
	 * Method deletes all menus that are over a month old. Corresponding FoodItems are left in the datastore.
	 */
	public static void deleteOldMealMenus(){
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
		Iterator<MealMenu> toDelete = datastore.find().type(MealMenu.class)
		.addFilter("endMillis",FilterOperator.LESS_THAN_OR_EQUAL, (new DateTime().minusMonths(1)).getMillis())
		.fetchResultsBy(5).returnResultsNow();
		
		boolean doMore = toDelete.hasNext();
		while(toDelete.hasNext()){
			datastore.delete(toDelete.next());
		}
		Queue queue = QueueFactory.getDefaultQueue();
		if(doMore) queue.add(TaskOptions.Builder.url("/cron/deleteOldMenus.jsp"));
	}

	/**
	 * Method to be used ONLY ON DEPLOYED SERVERS
	 * Local suite does NOT support the Task Queue!<br>
	 * Updates the datastore with the latest information from the web
	 * Only updates 5 menus at a time, and then hands off remainder of the work to Task Queue
	 * @param iteration represents where in the currently parsed file to start adding to the datastore
	 * @param menuCode two digit number that determines which menu we are pulling from. 
	 * The second digit represents either this weeks menus (1) or next week's (2). 
	 * The first digit represents the dining common: 1=Carrillo, 2=DLG, 3=Ortega, 4=Portola
	 * For example, a menuCode of 32 would represent Ortega's menu for next week.
	 */
	public static void updateDatastore(int iteration, int menuCode){
		//Create Queue and menus
		Queue queue = QueueFactory.getDefaultQueue();
		ArrayList<MealMenu> menus;
		UCSBJMenuScraper scraper = new UCSBJMenuScraper(new RemoteUCSBMenuFile(getCommonsURL(menuCode)));
		menus = scraper.getMenus();
		if(iteration<menus.size()){
			iteration+=5;
			if(iteration<menus.size()) queue.add(TaskOptions.Builder.url("/cron/update.jsp")
					.param("count", ""+iteration).param("menu",""+menuCode));
			updateDatastoreHelper(iteration-5, iteration, menus);
		}
	}
	
	
	/**
	 * @param start start position in the arraylist passed in
	 * @param stop stop position in the arraylist passed in
	 * @param menus arraylist of meal menus to be added to the datastore
	 */
	private static void updateDatastoreHelper(int start, int stop, ArrayList<MealMenu> menus){
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
		List<Future<Key>> keys = new LinkedList<Future<Key>>();
		
		for(int o = start;o<stop&&o<menus.size();o++){
			MealMenu tmp = datastore.load(MealMenu.class,menus.get(o).getMenuKey());
			if(tmp!=null){
				if(menus.get(0).getModDate().getMillis()>tmp.getModDate().getMillis()){
					datastore.delete(tmp);
					keys.add(datastore.store().instance(menus.get(0)).returnKeyLater());
				}
			} else{
				keys.add(datastore.store().instance(menus.get(o)).returnKeyLater());
			}
//			datastore.store().instance().returnKeyNow();
		}
		
		for(Future<Key> key:keys){
			try {
				key.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * TO BE USED ON DEPLOYED SERVERS ONLY. WILL NOT WORK ON LOCAL SERVERS AS TASK QUEUE ISN'T IMPLEMENTED
	 * @param mode 0 to clear meal menus, 1 to clear food items.
	 */
	public static void clearAll(int mode){
		Queue queue = QueueFactory.getDefaultQueue();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
		boolean shouldContinue = false;
		switch(mode){
		case 0:
		{
			Iterator<MealMenu> it = datastore.find().type(MealMenu.class).fetchResultsBy(5).returnResultsNow();
			if(it.hasNext()) shouldContinue=true;
			MealMenu mm;
			int count = 0;
			while(it.hasNext() && count<5){
				mm = it.next();
				datastore.delete(mm);
				count++;
			}
		}
		break;
		case 1:
		{
			Iterator<FoodItem> it = datastore.find().type(FoodItem.class).fetchResultsBy(20).returnResultsNow();
			if(it.hasNext()) shouldContinue=true;
			int count = 0;
			while(it.hasNext()&&count<20){
				datastore.delete(it.next());
				count++;
			}
		}
		break;
		}
		if(shouldContinue) queue.add(TaskOptions.Builder.url("/cron/clear.jsp").param("count",""+mode));
	}
	
	private static String getCommonsURL(int menuCode){
		switch(menuCode){
		case 11:
			return RemoteUCSBMenuFile.CARRILLO_THIS_WEEK;
		case 12:
			return RemoteUCSBMenuFile.CARRILLO_NEXT_WEEK;
		case 21:
			return RemoteUCSBMenuFile.DLG_THIS_WEEK;
		case 22:
			return RemoteUCSBMenuFile.DLG_NEXT_WEEK;
		case 31:
			return RemoteUCSBMenuFile.ORTEGA_THIS_WEEK;
		case 32:
			return RemoteUCSBMenuFile.ORTEGA_NEXT_WEEK;
		case 41:
			return RemoteUCSBMenuFile.PORTOLA_THIS_WEEK;
		case 42:
			return RemoteUCSBMenuFile.PORTOLA_NEXT_WEEK;
		default:
			return RemoteUCSBMenuFile.CARRILLO_THIS_WEEK;
		}
	}
}