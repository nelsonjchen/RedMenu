package com.mindflakes.TeamRED.tests;

public class LightSwitch {

	private Boolean on = false;
	
	public LightSwitch() {
		on = false;
	}
	
	public void turnon() {
		on = true;
	}
	
	public void turnoff() {
		on = false;
	}
	
	public void toggle() {
		if (on) {
			on = false;
		} else {
			on = true;
		}
	}
	
	public Boolean isOn() {
		if (on) {
			return true;
		} else {
			return false;
		}
	}
}
