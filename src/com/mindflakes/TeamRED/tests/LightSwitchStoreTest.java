package com.mindflakes.TeamRED.tests;

import static junit.framework.Assert.assertEquals;

import static org.junit.Assert.assertEquals;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import org.junit.Test;


public class LightSwitchStoreTest extends LocalDatastoreTestCase {
	
//	@SuppressWarnings("unused")
	private AnnotationObjectDatastore datastore;

	@Override
	public void setUp()
	{
		super.setUp();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		datastore = new AnnotationObjectDatastore(service);
	}
	
	@Test
	public void setupSwitch() {
		LightSwitch sw = new LightSwitch();
	}

	
	@Test
	public void checkEqual() {
		assertEquals("One Equals One",1,1);
	}
}
