package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;

import org.joda.time.DateTime;
import org.junit.Test;


public class MealMenuPersistTest extends LocalDatastoreTestCase {
	
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
		mealmenu = MealMenuTestUtils.createTestMenu();
		datastore.store().instance(mealmenu).returnKeyNow();
	}
	
	@Test
	public void persistTwice() {
		storeMenu();
		storeMenu();
		Iterator<MealMenu> retrieved_menus = datastore.find().type(MealMenu.class)
		.addFilter("commonsName", EQUAL, "Carrillo")
		.returnResultsNow();
		int count = 0;
		while (retrieved_menus.hasNext()) {
			retrieved_menus.next();
			count++;
		}
		assertEquals(1, count);
	}
	
	@Test
	public void checkEqual() {
		assertEquals("One Equals One",1,1);
	}
	
	@Test
	public void persistMenu() {
		mealmenu = MealMenuTestUtils.createTestMenu();
		datastore.store().instance(mealmenu).returnKeyNow();
	}
	
	@Test
	public void doesMenuExist() {
		mealmenu = MealMenuTestUtils.createTestMenu();
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
		.addFilter("endMillis", GREATER_THAN_OR_EQUAL, early_time)
		.addSort("endMillis")
		.returnResultsNow();
		assertEquals("Name", "Carrillo", future_menu.next().getCommonsName());
	}
}	
