package com.mindflakes.TeamRED.UCSBScrape;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class RemoteUCSBMenuFile extends UCSBMenuFile {
	public RemoteUCSBMenuFile(String URLPath){
		try{
			br = new BufferedReader(new InputStreamReader((new URL(URLPath).openStream())));
		}catch(IOException e){
			e.printStackTrace();
			br = new BufferedReader(null);
		}
	}
}
