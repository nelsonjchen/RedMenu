package com.mindflakes.TeamRED.tests;

public class LightSwitch {

	private Boolean on = false;
	private String name;
	
	public LightSwitch() {
		on = false;
		name = "Unknown";
	}
	
	public LightSwitch(String name) {
		on = false;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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
