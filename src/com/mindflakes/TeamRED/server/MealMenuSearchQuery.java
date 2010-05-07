package com.mindflakes.TeamRED.server;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.*;

import org.joda.time.DateTime;
import com.mindflakes.TeamRED.menuClasses.*;

import java.util.Iterator;
import java.util.ArrayList;

import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
public class MealMenuSearchQuery {
	private ArrayList<MealMenu> menus;
	
	public MealMenuSearchQuery(DateTime startDate, DateTime endDate){
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore(service);
		
		Iterator<MealMenu> it = datastore.find().type(MealMenu.class)
		.addFilter("startMillis", FilterOperator.GREATER_THAN_OR_EQUAL, startDate.getMillis()).returnResultsNow();
		
		menus = new ArrayList<MealMenu>();
		while(it.hasNext()){
			MealMenu tmp = it.next();
			if(tmp.getMealInterval().getEndMillis()<=endDate.getMillis()) menus.add(tmp);
		}
	}
	
	public MealMenuSearchQuery(ArrayList<MealMenu> menus){
		this.menus = menus;
	}
	
	public MealMenuSearchQuery findCommons(String commonsName){
//		System.out.println("searching for commons: "+commonsName);
		commonsName=commonsName.toLowerCase();
		ArrayList<MealMenu> veganMenus = new ArrayList<MealMenu>();
		for(MealMenu menu:menus){
			if(menu.getCommonsName().toLowerCase().equals(commonsName)) veganMenus.add(menu);
		}
		return new MealMenuSearchQuery(veganMenus);
	}
	
	public MealMenuSearchQuery findVegan(){
//		System.out.println("Searching for vegan");
		ArrayList<MealMenu> veganMenus = new ArrayList<MealMenu>();
		for(MealMenu menu:menus){
			menu=menu.newMealMenuFromVegan();
			if(menu.getVenues().size()>0) veganMenus.add(menu);
		}
		return new MealMenuSearchQuery(veganMenus);
	}
	
	public MealMenuSearchQuery findVegetarian(){
//		System.out.println("Searching for vegetarian");
		ArrayList<MealMenu> vgtMenus = new ArrayList<MealMenu>();
		for(MealMenu menu:menus){
			menu=menu.newMealMenuFromVegetarian();
			if(menu.getVenues().size()>0) vgtMenus.add(menu);
		}
		return new MealMenuSearchQuery(vgtMenus);
	}
	
	public MealMenuSearchQuery findFoodItem(String search){
		ArrayList<String> searchParts = splitSearch(search);
		ArrayList<MealMenu> searchMenus = new ArrayList<MealMenu>();
//		System.out.print("Searchin for...: ");
//		for(String s: searchParts){
//			System.out.println(s+" ");
//		}
//		System.out.println();
		for(MealMenu menu:menus){
			boolean foundFoodInThisMenu = false;
			try{
				for(Venue venue:menu.getVenues()){
					if(foundFoodInThisMenu) break;
					for(FoodItem food:venue.getFoodItems()){
						//					if(food.getName().contains("Focaccia")) System.out.println(food.getName());
						if(foundFoodInThisMenu) break;
						for(String s: searchParts){
							if(food.getName().toLowerCase().contains(s)){
								foundFoodInThisMenu=true;
								break;
							}
						}
					}
				}
			}catch(NullPointerException e){
				searchMenus.add(menu);
			}
			if(foundFoodInThisMenu) searchMenus.add(menu);
		}
		return new MealMenuSearchQuery(searchMenus);	
	}
	
	private ArrayList<String> splitSearch(String search){
		System.out.println("splitting: " + search);
		ArrayList<String> splitStrings = new ArrayList<String>();
		search=search.toLowerCase();
		while(search.indexOf('\"')!=-1){
			if(search.indexOf('\"',search.indexOf('\"')+1)!=-1){
				if(search.indexOf('\"')!=0){
//					System.out.println("adding: "+search.substring(0,search.indexOf('\"')).trim());
					splitStrings.add(search.substring(0,search.indexOf('\"')).trim());
				}
				search=search.substring(search.indexOf('\"')+1);
//				System.out.println("adding:" +search.substring(0,search.indexOf('\"')).trim());
				splitStrings.add(search.substring(0,search.indexOf('\"')).trim());
				search=search.substring(search.indexOf('\"')+1);
			} else{
				String tmp = search.substring(0,search.indexOf('\"')).trim();
				if(tmp.length()!=0) splitStrings.add(tmp);
				search = search.substring(search.indexOf('\"')+1).trim();
				break;
			}
		}
		for(String s:search.split(" ")){
			if(s.length()!=0){
				splitStrings.add(s);
//				System.out.println("adding: "+s);
			}
		}
		for(int i = 0; i<splitStrings.size();i++){
			if(splitStrings.get(i).trim().compareTo("")==0){
				splitStrings.remove(i);
			}
		}
		return splitStrings;
	}
	
	public ArrayList<MealMenu> returnResults(){
		return menus;
	}
	
}