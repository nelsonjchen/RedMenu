package com.mindflakes.TeamRED.UCSBScrape;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UCSBMenuFile file = new RemoteUCSBMenuFile("https://docs.google.com/viewer?url=http://www.housing.ucsb.edu/dining/menu/ortega/ThisWeekMenu.pdf&a=gt");
		UCSBMenuFile file2 = new LocalUCSBMenuFile("docs/viewer.xml");
		for(int i = 0; i<10;i++){
			System.out.println(file.nextLine().compareTo(file2.nextLine()));
		}
		for(int i = 0; i<10; i++){
			System.out.println(file.nextLine());
		}
	}

}
