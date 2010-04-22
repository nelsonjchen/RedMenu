package com.mindflakes.TeamRED.utils;

import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.tests.MealMenuTestUtils;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MealMenu menu = MealMenuTestUtils.createTestMenu();
		System.out.println(MealMenuUtil.mealMenuString(menu));
	}

}
