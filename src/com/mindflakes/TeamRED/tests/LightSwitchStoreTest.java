package com.mindflakes.TeamRED.tests;

import static junit.framework.Assert.assertEquals;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
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
	public void checkSwitch() {
		LightSwitch sw = new LightSwitch();
//		LightSwitch sw_name = new LightSwitch();
		
		Key key = datastore.store(sw);
		LightSwitch sw2 = datastore.load(key);
		assertFalse(sw2.isOn());
		assertEquals("Name Accuracy", sw2.getName(), "Unknown");

	}

	
	@Test
	public void checkEqual() {
		assertEquals("One Equals One",1,1);
	}
}
