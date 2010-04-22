package com.mindflakes.TeamRED.UCSBScrape;
import java.util.*;

public class UCSBMenuScraper {
    private static UCSBMenuFile file;
    public UCSBMenuScraper(String filename, int mode) {
        if (mode==1) {
            file = new LocalUCSBMenuFile(filename);
        } else if (mode==0) {
            file = new RemoteUCSBMenuFile(filename);
        }
        String targetline = "";

        while(!((file.nextLine()).equals("<pdf2xml>"))) {
            //aligns the scanner to rid of leading whitespace
        }

        //gets commons name
        jump(6);       
        String commonsName;
        targetline = file.nextLine();
        commonsName=getText(targetline);
        
        file.nextLine();
        targetline = file.nextLine();
        
        
        //create arrays
        Vector<String> temp;
        Vector<String> day[] = new Vector[7];
        for (int i=0; i<7; i++) {
            Vector<String> food = new Vector<String>();
            day[i] = food;
        }
        
        //adds first meal to arrays
        for (int i=0; i<7; i++) {
    		temp = day[i];
    		temp.add(getText(targetline));
    		
    	}
        
        

        //gets dates
        jump(7);
        String date[] = new String[7];
        for (int i=0; i<7; i++) {
            date[i] = getText(file.nextLine());
        }
     
        
        targetline = file.nextLine();
        while(!targetline.equals("EOF")) {
        	
           switch(getNum(targetline)) {
                case 34:
                    temp = day[0];
                    temp.add(getText(targetline));
                    break;
                case 166:
                    temp = day[1];
                    temp.add(getText(targetline));
                    break;
                case 299:
                    temp = day[2];
                    temp.add(getText(targetline));
                    break;
                case 434:
                    temp = day[3];
                    temp.add(getText(targetline));
                    break;
                case 567:
                    temp = day[4];
                    temp.add(getText(targetline));
                    break;
                case 702:
                    temp = day[5];
                    temp.add(getText(targetline));
                    break;
                case 837:
                    temp = day[6];
                    temp.add(getText(targetline));
                    break;
                default:
                	String text = getText(targetline);
                	if (text.equals("Monday")) {
                		jump(13);
                	} else if (text.equals("/notext")) {
                	
                	} else if (text.equals(commonsName)) {
                	
                	} else if (text.equals("Weekly Menu")) {
                	
                	} else {
	                	for (int i=0; i<7; i++) {
	                		temp = day[i];
	                		temp.add(getText(targetline));
	                	}
                	}
                    break;
            } //switch
           targetline= file.nextLine();
        }




       for (int i=0; i<day[0].size(); i++) {
            System.out.println(day[0].get(i));
        }
        





    } //construct
    public static void jump(int lines) {
        //moves scanning position of the menu file
        for(int i=0; i<lines; i++) {
            file.nextLine();
        }
    }
    public static String getText(String input) {
        String temp = "";
        String text = "/notext";
        
        //cut off leading <text>
        StringTokenizer read = new StringTokenizer(input, ">", false);
        if (read.hasMoreTokens()) {
            read.nextToken();
            if (read.hasMoreTokens()) {
                temp = read.nextToken();
                //cut off trailing </text>
                StringTokenizer read2 = new StringTokenizer(temp, "<", false);
                if (read2.hasMoreTokens()) {
                    text = read2.nextToken();
                }
            }
        }
        return text;
    }
    public static int getNum(String input) {
        int temp = -1;

        StringTokenizer read = new StringTokenizer(input, "\"", false);
        if (read.hasMoreTokens()) {
            read.nextToken();
            if (read.hasMoreTokens()) {
                temp = Integer.parseInt((read.nextToken()));
            }
        }
        return temp;
    }
    public static boolean hasText(String input) {
        if ((getText(input).equals("/notext"))) {
            return false;
        } else {
            return true;
        }

    }
    public static boolean hasNum(String input) {
        if ((getNum(input)==-1)) {
            return false;
        } else {
            return true;
        }
    }

}//class