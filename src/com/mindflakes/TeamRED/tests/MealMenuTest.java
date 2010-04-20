package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.mindflakes.TeamRED.UCSBScrape.TestMain;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

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
		.addFilter("commonsName", FilterOperator.EQUAL, "Carrillo")
		.returnResultsNow();
		MealMenu retrieve = retrieved_menus.next();
		assertEquals("Name", "Carrillo", retrieve.getCommonsName());
	}

}
