package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.UCSBScrape.TestMain;
import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;

import org.joda.time.DateTime;
import org.junit.Test;


public class MealMenuTest extends LocalDatastoreTestCase {
	
//	@SuppressWarnings("unused")
	private AnnotationObjectDatastore datastore;
	private MealMenu mealmenu;

	@Override
	public void setUp()
	{
		super.setUp();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		datastore = new AnnotationObjectDatastore(service);
	}
	
	private void storeMenu() {
		mealmenu = TestMain.createTestMenu();
		datastore.store().instance(mealmenu).returnKeyNow();
	}
	
	@Test
	public void checkEqual() {
		assertEquals("One Equals One",1,1);
	}
	
	@Test
	public void persistMenu() {
		mealmenu = TestMain.createTestMenu();
		datastore.store().instance(mealmenu).returnKeyNow();
	}
	
	@Test
	public void doesMenuExist() {
		mealmenu = TestMain.createTestMenu();
		datastore.store().instance(mealmenu).returnKeyNow();
		Iterator<MealMenu> retrieved_menus = datastore.find().type(MealMenu.class)
		.addFilter("commonsName", EQUAL, "Carrillo")
		.returnResultsNow();
		MealMenu retrieve = retrieved_menus.next();
		assertEquals("Name", "Carrillo", retrieve.getCommonsName());
	}

	@Test
	public void findCommonByTime() {
		storeMenu();
//		Only one inequality is supported.
//		7 o clock
		long early_time = new DateTime(2010, 4, 12, 7, 00, 00, 00).getMillis();
		Iterator<MealMenu> future_menu = datastore.find()
		.type(MealMenu.class)
		.addFilter("startMillis", GREATER_THAN_OR_EQUAL, early_time)
		.addSort("startMillis")
		.returnResultsNow();
		assertEquals("Name", "Carrillo", future_menu.next().getCommonsName());
	}
}	
