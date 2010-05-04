package com.mindflakes.TeamRED.tests;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class PatternTesting {
	@Test
	public void matchingCommon() {
		Pattern MEAL_COMMON_PATTERN = Pattern.compile("/feed/(\\w+)");
		Matcher m = null;
		
		m = MEAL_COMMON_PATTERN.matcher("/feed/ortega");
		assertTrue(m.matches());
		String matched = m.group(1);
		assertEquals("Matched Common of Ortega", "ortega", matched);
		
	}
}
