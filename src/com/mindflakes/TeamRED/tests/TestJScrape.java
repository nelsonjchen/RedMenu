package com.mindflakes.TeamRED.tests;

import com.mindflakes.TeamRED.UCSBScrape.LocalUCSBMenuFile;
import com.mindflakes.TeamRED.UCSBScrape.RemoteUCSBMenuFile;
import com.mindflakes.TeamRED.UCSBScrape.UCSBJMenuScraper;
import com.mindflakes.TeamRED.UCSBScrape.UCSBMenuFile;

public class TestJScrape {
	
	public static void main(String[] args){
//		UCSBMenuFile file = new RemoteUCSBMenuFile("https://docs.google.com/viewer?url=http://www.housing.ucsb.edu/dining/menu/ortega/ThisWeekMenu.pdf&a=gt");
//		UCSBMenuFile file2 = new LocalUCSBMenuFile("docs/viewer.xml");
//		UCSBJMenuScraper scrape = new UCSBJMenuScraper("http://localhost/carrillo.xml",0);
		
//		UCSBJMenuScraper carrillo = new UCSBJMenuScraper("docs/Menus/carrillo4.19.xml", 1);
//		carrillo.printAll();
		
//		UCSBJMenuScraper portola = new UCSBJMenuScraper("docs/Menus/portola4.19.xml", 1);
//		portola.printAll();
		
//		UCSBJMenuScraper dlg = new UCSBJMenuScraper("docs/Menus/dlg4.19.xml", 1);
//		dlg.printAll();
		
//		UCSBJMenuScraper ortega = new UCSBJMenuScraper("docs/Menus/ortega4.19.xml", 1);
		UCSBJMenuScraper ortega = new UCSBJMenuScraper("https://docs.google.com/viewer?url=http://www.housing.ucsb.edu/dining/menu/carrillo/ThisWeekMenu.pdf&a=gt", 0);
		ortega.printAll();

	}

}
