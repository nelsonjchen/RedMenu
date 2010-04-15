package com.mindflakes.TeamRED.tests;


import org.junit.After;
import org.junit.Before;


import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


/**
 * @author 
 *
 * From twig-persist's tests which are based on Google's example.
 */
public abstract class LocalDatastoreTestCase {


    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());


    @Before
    public void setUp() {
        helper.setUp();
    }


    @After
    public void tearDown() {
        helper.tearDown();
    }
}