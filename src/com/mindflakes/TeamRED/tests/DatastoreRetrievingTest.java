package com.mindflakes.TeamRED.tests;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import com.mindflakes.TeamRED.UCSBScrape.RemoteUCSBMenuFile;
import com.mindflakes.TeamRED.UCSBScrape.UCSBJMenuScraper;
import com.mindflakes.TeamRED.utils.*;
import com.mindflakes.TeamRED.menuClasses.*;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DatastoreRetrievingTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private AnnotationObjectDatastore datastore;
	private static ArrayList<UCSBJMenuScraper> scrapers;
	
    @BeforeClass
    public static void oneTimeSetUp() {
    	scrapers=new ArrayList<UCSBJMenuScraper>();
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.CARRILLO_THIS_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.CARRILLO_NEXT_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.DLG_THIS_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.DLG_NEXT_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.ORTEGA_THIS_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.ORTEGA_NEXT_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.PORTOLA_THIS_WEEK)));
    	scrapers.add(new UCSBJMenuScraper(new RemoteUCSBMenuFile(RemoteUCSBMenuFile.PORTOLA_NEXT_WEEK)));
    }

    @Before
    public void setUp() {
        helper.setUp();
		datastore = new AnnotationObjectDatastore(DatastoreServiceFactory.getDatastoreService());
    }
    
    public void addMenus(){
    	for(UCSBJMenuScraper scraper : scrapers){
        	for(MealMenu menu : scraper.getMenus()){
        		datastore.store(menu);
        	}
    	}
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
    	assertFalse(datastore.find().type(MealMenu.class).returnResultsNow().hasNext());
    	addMenus();
    	Iterator<MealMenu> it = datastore.find().type(MealMenu.class).returnResultsNow();
    	assertTrue(it.hasNext());
    	while(it.hasNext()){
    		MealMenu current = it.next();
    		assertTrue(current!=null);
    		assertTrue(current.getVenues()!=null);
    		for(Venue v:current.getVenues()){
    			assertTrue(v!=null);
    			assertTrue(v.getFoodItems()!=null);
    			for(FoodItem food:v.getFoodItems())
    				assertTrue(food!=null);
    		}
    		boolean isInScraper = false;
    		for(UCSBJMenuScraper scraper : scrapers){
    			for(MealMenu menu : scraper.getMenus()){
    				if(current==menu){
    					isInScraper=true;
    					break;
    				}
    			}
    		}
    		assertTrue(isInScraper);
    	}
    }

    @Test
    public void testInsert1() {
    	doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
    
    @Test
    public void testSearch(){
    	assertFalse(datastore.find().type(MealMenu.class).returnResultsNow().hasNext());
    	addMenus();
    	Iterator<MealMenu> it = datastore.find().type(MealMenu.class).returnResultsNow();
    	assertTrue(it.hasNext());
    	try{
    		DateTime time = new DateTime(DateTimeZone.forID("America/Los_Angeles")); 
    		Iterator<MealMenu> future_menu = datastore.find()
    		.type(MealMenu.class)
    		.addFilter("endMillis",
    				com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, time.getMillis())
    				.addSort("endMillis")
    				.returnResultsNow();
    		int count=0;
    		while(future_menu.hasNext()){
    			future_menu.next();
    			count++;
    		}
    		System.out.println(count);
    	} catch(IllegalStateException e){
    		assertTrue(false);
    	}
    }
}