package com.mindflakes.TeamRED.UCSBScrape;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class UCSBMenuFile {
	BufferedReader br;
	/**
	 * Used to read the next line of the File or URL
	 * @return The next line of the File or URL as passed by the BufferedReader. Will return null if end of file is reached or nonexistant file is used
	 */
	protected String nextLine(){
		try{
			return br.readLine();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}

}
