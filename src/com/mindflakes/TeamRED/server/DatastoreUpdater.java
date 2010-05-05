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
//import com.google.appengine.api.labs.taskqueue.Queue;
//import com.google.appengine.api.labs.taskqueue.QueueFactory;
//import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

public class DatastoreUpdater {

	public static void updateDatastore(){
		//DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(false);

		//Remove month old objects
		// Iterator<MealMenu> toDelete = datastore.find().type(MealMenu.class)
		// .addFilter("endMillis",FilterOperator.LESS_THAN_OR_EQUAL, (new DateTime().minusMonths(1)).getMillis()).returnResultsNow();
		//
		// while(toDelete.hasNext()){
		// datastore.delete(toDelete.next());
		// }

		ArrayList<MealMenu> menus;
		UCSBJMenuScraper scraper = new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.CARRILLO_THIS_WEEK));
		menus = scraper.getMenus();

		List<Future<Key>> keys = new LinkedList<Future<Key>>();
		for(MealMenu menu : menus){
			// MealMenu tmp = datastore.load(MealMenu.class,menu.getMenuKey());
			// if(tmp!=null){
			// if(menu.getModDate().getMillis()>tmp.getModDate().getMillis()){
			// datastore.delete(tmp);
			// keys.add(datastore.store().instance(menu).returnKeyLater());
			// }
			// } else{
//			keys.add(datastore.store().instance(menu).returnKeyLater());
			datastore.store().instance(menu).returnKeyNow();
			// }
		}
		// List<Future<Key>> keys = new LinkedList<Future<Key>>();
		// for(MealMenu menu:menus){
		// keys.add(datastore.store().instance(menu).returnKeyLater());
		// }
//		for(Future<Key> key:keys){
//			try {
//				key.get();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		// MealMenu dupeMenu = null;
		// {
		// Iterator<MealMenu> seeIfExists = datastore.find().type(MealMenu.class)
		// .addFilter("startMillis",FilterOperator.EQUAL,menus.get(0).getMealInterval().getStartMillis())
		// .returnResultsNow();
		// MealMenu tmp = null;
		// while(seeIfExists.hasNext()){
		// tmp = seeIfExists.next();
		// if(tmp.getCommonsName().equals(menus.get(0).getCommonsName())){
		// dupeMenu = tmp;
		// break;
		// }
		// }
		// }
		//
		// if(dupeMenu!=null){
		// if(dupeMenu.getModDate().getMillis()<menus.get(0).getModDate().getMillis()){
		// long minStart = Long.MAX_VALUE, maxEnd = 0;
		// for(MealMenu menu : menus){
		// if(menu.getMealInterval().getStartMillis()<minStart) minStart = menu.getMealInterval().getStartMillis();
		// if(menu.getMealInterval().getEndMillis()>maxEnd) maxEnd = menu.getMealInterval().getEndMillis();
		// }
		// toDelete = datastore.find().type(MealMenu.class)
		// .addFilter("startMillis",FilterOperator.GREATER_THAN_OR_EQUAL,minStart)
		// .returnResultsNow();
		// MealMenu tmp;
		// ArrayList<MealMenu> toDeleteArrL = new ArrayList<MealMenu>();
		// while(toDelete.hasNext()){
		// tmp = toDelete.next();
		// if(tmp.getCommonsName().equals(dupeMenu.getCommonsName()) && tmp.getMealInterval().getEndMillis()<=maxEnd){
		// toDeleteArrL.add(tmp);
		// }
		// }
		// for(MealMenu menu: toDeleteArrL){
		// datastore.delete(menu);
		// }
		// }
		// }
	}
}