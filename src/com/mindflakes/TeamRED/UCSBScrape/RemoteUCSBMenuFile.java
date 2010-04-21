package com.mindflakes.TeamRED.UCSBScrape;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class RemoteUCSBMenuFile extends UCSBMenuFile {
	public RemoteUCSBMenuFile(String URLPath){
		try{
			sc = new Scanner(new InputStreamReader((new URL(URLPath).openStream())));
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
}
