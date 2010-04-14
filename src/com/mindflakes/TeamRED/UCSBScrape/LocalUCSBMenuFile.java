package com.mindflakes.TeamRED.UCSBScrape;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class LocalUCSBMenuFile extends UCSBMenuFile {
	public LocalUCSBMenuFile(String fileName){
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
		}catch(FileNotFoundException e){
			br=new BufferedReader(null);
		}
	}
}
