package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.server.MealMenuArbiter;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;


public class ArbiterTest extends LocalDatastoreTestCase {
	
	private AnnotationObjectDatastore datastore;
	private MealMenu mealmenu;
	
	@Override
	public void setUp()
	{
		super.setUp();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		datastore = new AnnotationObjectDatastore(service);
	}
	
	@Test
	public void sanityCheck() {
		assertEquals(1,1);
	}
	
	private void domerge() {
		MealMenuArbiter.mergeMenu("docs/viewer.xml",1);
	}
	
	@Test
	public void justamerge() {
		domerge();
	}
	
	@Test
	public void nowmergetwice() {
		domerge();
		domerge();
	}
	
	@Test
	public void mergetwice5breakfasts() {
		domerge();
		domerge();
		Iterator<MealMenu> menus = datastore.find()
								.type(MealMenu.class)
								.addFilter("mealName", EQUAL, "Breakfast")
								.returnResultsNow();
		int count = 0;
		while (menus.hasNext()) {
			count++;
		}
		assertEquals(5, count);
	}
}
