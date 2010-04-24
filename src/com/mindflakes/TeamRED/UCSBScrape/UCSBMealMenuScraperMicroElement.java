package com.mindflakes.TeamRED.UCSBScrape;

public class UCSBMealMenuScraperMicroElement {
	private int left;
	private int width;
	private UCSBMealMenuScraperElement parent;
	
	public void setLeft(int left) {
		this.left = left;
	}
	public int getLeft() {
		return left;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setParent(UCSBMealMenuScraperElement parent) {
		this.parent = parent;
	}
	public UCSBMealMenuScraperElement getParent() {
		return parent;
	}
}
