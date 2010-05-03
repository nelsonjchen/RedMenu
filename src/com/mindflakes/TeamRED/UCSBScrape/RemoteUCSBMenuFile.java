package com.mindflakes.TeamRED.UCSBScrape;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

/** Remote file implementation of the UCSBMenuFile abstract class. Used when the XML file is stored on a website
 * @author Johan Henkens
 *
 */
public class RemoteUCSBMenuFile extends UCSBMenuFile {
	/** Constructs a <code>RemoteUCSBMenuFile</code> with the specified URL as the location to the XML file representing a menu. The URL must be absolute.
	 * @param URLPath Absolute URL to the XML file hosted on a website
	 */
	public RemoteUCSBMenuFile(String URLPath){
		try{
			sc = new Scanner(new InputStreamReader((new URL(URLPath).openStream())));
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
}
