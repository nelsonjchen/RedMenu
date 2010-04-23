package com.mindflakes.TeamRED.UCSBScrape;

public class TestJScrape {
	
	public static void main(String[] args){
		UCSBJMenuScraper scrape = new UCSBJMenuScraper("http://localhost/carrillo.xml",0);
		scrape.printAll();
	}

}
