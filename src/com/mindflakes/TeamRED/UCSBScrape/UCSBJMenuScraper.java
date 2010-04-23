package com.mindflakes.TeamRED.UCSBScrape;
import java.util.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.DateTime;
import com.mindflakes.TeamRED.menuClasses.*;

public class UCSBJMenuScraper {
    private UCSBMenuFile file;
    //private static String date[] = new String[7];
    private  ArrayList<MealMenu> menus = new ArrayList<MealMenu>();
    
    
    public ArrayList<MealMenu> getMenus() {
		return menus;
	}

	private static long modDateStringToLongMillis(String modDate){
    	int[] result = new int[7];
    		result[0]=Integer.parseInt(modDate.substring(0,4));
    		result[1]=Integer.parseInt(modDate.substring(4,6));
    		result[2]=Integer.parseInt(modDate.substring(6,8));
    		result[3]=Integer.parseInt(modDate.substring(8,10));
    		result[4]=Integer.parseInt(modDate.substring(10,12));
    		result[5]=Integer.parseInt(modDate.substring(12,14));
    		result[6]=00;
    	return (new DateTime(result[0],result[1],result[2],result[3],result[4],result[5],result[6])).getMillis();
    }
    
    private static void initializeALofALofString(ArrayList<ArrayList<String>> arrList){
    	for(int i = 0; i<7;i++){
    		arrList.add(new ArrayList<String>());
    	}
    }
    
    public UCSBJMenuScraper(String filename, int mode) {    	
        if (mode==1) {
            file = new LocalUCSBMenuFile(filename);
        } else if (mode==0) {
            file = new RemoteUCSBMenuFile(filename);
        }
        String currentLine = file.nextLine();
        ArrayList<ArrayList<String> > breakfasts = new ArrayList<ArrayList<String>>(7);
        ArrayList<ArrayList<String> > lunches = new ArrayList<ArrayList<String>>(7);
        ArrayList<ArrayList<String> > dinners = new ArrayList<ArrayList<String>>(7);
        ArrayList<ArrayList<String> > brunches = new ArrayList<ArrayList<String>>(7);
        ArrayList<ArrayList<String> > lateNites = new ArrayList<ArrayList<String>>(7);
        initializeALofALofString(breakfasts);
        initializeALofALofString(lunches);
        initializeALofALofString(dinners);
        initializeALofALofString(brunches);
        initializeALofALofString(lateNites);
        int[][] positions = new int[7][20];
        String[] dates = new String[7];
        String[] mealNames = new String[5];
        String[][] mealTimes = new String[5][2];
        
        ArrayList<ArrayList<String>> currentMealArrayList = breakfasts;
        
        long modDateMillis=0;
        String currentCommons = "";

        //Gets to line containing CreationDate for all menus
        while(!currentLine.contains("CreationDate")) {
        	currentLine = file.nextLine();
        }
        //Removes just the content from the CreationDate line.
        currentLine=currentLine.substring(currentLine.indexOf('\"',currentLine.indexOf("Date\"")+5)+1,currentLine.lastIndexOf('-'));
        modDateMillis=modDateStringToLongMillis(currentLine);
        
        while(!currentLine.contains("Commons")){
        	currentLine=file.nextLine();
        }
		if(!currentLine.contains("Commons")){
			throw new LineErrorException("Expected \"Commons\" in line" + currentLine);
		}
		currentCommons = contentOfLine(currentLine);

//        System.out.println(DateTimeFormat.mediumDateTime().print(new DateTime(modDateMillis)));
        currentLine = file.nextLine();
        boolean firstPass= true;
        
        String lastLine = "";
        while(true){
        	if(currentLine.equals("EOF")) break;
        	if(currentLine.equals(lastLine)) System.out.println("Failing on:" + currentLine);
        	else lastLine=currentLine;
        		        	if(currentLine.contains("<page ") || currentLine.equals("</page>")|| 
        			currentLine.contains("Weekly Menu")|| currentLine.equals("</pdf2xml>")||
        			currentLine.contains(" Commons")
        			|| getHValue(currentLine)<8){
        		currentLine = file.nextLine();
        		continue;
        	}
        	
        	
        	if(isMealTimeLine(currentLine)){
        		String currentMeal = contentOfLine(currentLine);
        		
        		String currentMealStartTime = currentMeal.substring(currentMeal.indexOf('(')+1,currentMeal.indexOf('-'));
        		currentMealStartTime= fixStringTimeForAMPM(currentMealStartTime.trim());
        		
        		String currentMealEndTime = currentMeal.substring(currentMeal.indexOf('-')+1,currentMeal.indexOf(')')); 
        		currentMealEndTime = fixStringTimeForAMPM(currentMealEndTime.trim());
        		{
        			int meal = whichMealIsIt(currentLine);
        			mealNames[meal-1]=currentMeal.substring(0,currentMeal.indexOf("(")-1);
        			mealTimes[meal-1][0]=currentMealStartTime;
        			mealTimes[meal-1][1]=currentMealEndTime;
        			switch(whichMealIsIt(currentLine)){
        			case 1:
        				currentMealArrayList = breakfasts;
        				break;
        			case 2:
        				currentMealArrayList = lunches;
        				break;
        			case 3:
        				currentMealArrayList = dinners;
        				break;
        			case 4:
        				currentMealArrayList = brunches;
        				break;
        			case 5:
        				currentMealArrayList = lateNites;
        				break;
        			}
        		}
        		
        		currentLine=file.nextLine();
        		while(dayOfWeekFromLine(currentLine)!=0){
        			if(!firstPass){
        				currentLine=file.nextLine();
        				continue;
        			}
        			int day = dayOfWeekFromLine(currentLine);
        			{
        				int currentPos = getCurrentPos(currentLine);
        				addPosnToArr(positions[day-1],currentPos);
        			}
        			currentLine = file.nextLine();        		
        		}
        		while(isLineADate(currentLine)){
        			if(!firstPass){
        				currentLine=file.nextLine();
        				continue;
        			}
        			int currentPos = getCurrentPos(currentLine);
        			int dayNear = getNearestDayIndex(positions,currentPos);
        			addPosnToArr(positions[dayNear],currentPos);
        			dates[dayNear]=contentOfLine(currentLine);
        			currentLine=file.nextLine();
        		}
//        		System.out.println(firstPass);
        		firstPass=false;
        		
        		while(!currentLine.equals("</page>")){
        			if(getHValue(currentLine)<8) {
        				currentLine=file.nextLine();
        				continue;
        			}
        			
        			//Checks if the line matches venue names and then adds to all lists if it is a venue.
        			if(lineIsVenue(currentLine,currentCommons)){
        				for(ArrayList<String> ar : currentMealArrayList){
        					ar.add(currentLine);
        				}
//        				System.out.println("Adding venue line: " + currentLine);
        				currentLine=file.nextLine();
        				continue;
        			}
        			
        			
        			boolean isTwoEntries = isTwoEntries(currentLine);
        			String secondLine = "";
        			
        			//Following block of code is used to handle if multiple entries exist on one line
        			if(isTwoEntries){
        				
        				String workingLine = currentLine;
        				int currentPos = getCurrentPos(workingLine);
            			int dayNear = getNearestDayIndex(positions,currentPos);
            			
            			if(dayNear==6) isTwoEntries=false;
            			
            			else{
            				int[] pArr = splitIntsFromPValInLine(workingLine);
            				int pos = -1;
            				for(int i = 2;i<pArr.length;i+=2){
            					if(pArr[i]-(pArr[i-2]+pArr[i-1])>2){
            						for(int o = 0; o<positions[dayNear+1].length;o++){
            							if(positions[dayNear+1][o]==pArr[i]){
            								pos=i;
//            								System.out.println("###Found the day! Day: " + (dayNear+1)+" with position: "+ pArr[i] + "at index: "+ i);
            							}
            						}
            					}
            				}
            				
            				String[] contentAr = contentOfLine(workingLine).split(" ");
            				String currentCont = "";
            				String secondCont = "";
//            				System.out.println(pos+" "+pos/2);
            				
            				for(int o = 0; o<contentAr.length;o++){
            					if(o<pos/2) currentCont+=(contentAr[o]+" ");
            					else secondCont+=(contentAr[o]+" ");
            				}
            				
            				currentCont=currentCont.trim();
            				secondCont=secondCont.trim();
            				
            				//Makes a new 'currentLine' that does not include the later part of the string
            				currentLine = workingLine.substring(0, workingLine.indexOf(">")+1)+currentCont+workingLine.substring(workingLine.indexOf("</"));
            				
//            				System.out.println("#### DOUBLE LINE DETECTED:" + workingLine);
//            				System.out.println("First Line: " + currentLine);
            				
            				//Makes a new 'secondLine' that will be parsed next that has a shorter length, proper starting point, and only the correct data
            				secondLine = workingLine.substring(0,workingLine.indexOf("l=\"")+3)+pArr[pos]+ // through the value of l="
            				workingLine.substring(workingLine.indexOf("\"",workingLine.indexOf("l=\"")+3),workingLine.indexOf("w=\"")+3)+ // through w="
            				"120"+workingLine.substring(workingLine.indexOf("\"",workingLine.indexOf("w=\"")+3),workingLine.indexOf(">")+1)+ // through >
            				secondCont+workingLine.substring(workingLine.indexOf("</")); // adds the content and the final tag
            			}
        			}
        			
        			//Standard adding code, also used with two entires
        			int currentPos = getCurrentPos(currentLine);
        			int dayNear = getNearestDayIndex(positions,currentPos);
        			addPosnToArr(positions[dayNear],currentPos);
        			currentMealArrayList.get(dayNear).add(currentLine);
//        			System.out.println("Added current line: " + currentLine + " to day index: " + dayNear);
        			if(!isTwoEntries) currentLine = file.nextLine();
        			else currentLine = secondLine;
        		}
        		
        		System.out.print("");
//        		if(currentLine.equals("</page>")) System.out.println("Hit Page");
//        		for(ArrayList<String> ar : currentMealArrayList){
//        			System.out.println(ar.size());
//        			for(String st : ar){
//        				System.out.println(st);
//        			}
//        		}
        	}
        	convertDatesToMMDDYYYY(dates);
        	createMealMenusForMeal(breakfasts,currentCommons, dates, mealTimes[0],modDateMillis,mealNames[0]);
        	createMealMenusForMeal(lunches,currentCommons, dates, mealTimes[1],modDateMillis,mealNames[1]);
        	createMealMenusForMeal(dinners,currentCommons, dates, mealTimes[2],modDateMillis,mealNames[2]);
        	createMealMenusForMeal(brunches,currentCommons, dates, mealTimes[3],modDateMillis,mealNames[3]);
        	createMealMenusForMeal(lateNites,currentCommons, dates, mealTimes[4],modDateMillis,mealNames[4]);

        }
    }
    
    private static int getHValue(String line){
    	return Integer.parseInt(line.substring(line.indexOf("h=\"")+3,line.indexOf("\"",line.indexOf("h=\"")+3)));
    }
   
    private static boolean hasMoreThanVenues(ArrayList<String> arr, String commons){
    	for(String st : arr){
    		if(!lineIsVenue(st, commons)) return true;
    	}
    	return false;
    }
    
    private static String fixAndSigns(String in){
    	while(in.indexOf("&amp;") != -1){
    		in=in.substring(0,in.indexOf("&amp;"))+"&"+in.substring(in.indexOf("&amp;")+5);
    	}
    	return in;
    }
    
    private static boolean isVegan(String in){
    	return false;
    }
    private static boolean isVgt(String in){
    	return false;
    }
    
    private static void convertDatesToMMDDYYYY(String[] dates){

    	int currentYear = (new DateTime()).getYear();
    	int currentMonth = (new DateTime()).getMonthOfYear();
    	for(int i = 0; i<dates.length;i++){
    		if(dates[i].indexOf(" ") == -1)continue;
    		String year = ""+currentYear;
    		String cur = dates[i].substring(0,dates[i].indexOf(" ")).toLowerCase();
    		if(cur.equals("january")){
    			cur="01";
    			if(currentMonth==12){
    				currentYear++;
    				year=""+currentYear;
    			}
    		} else if(cur.equals("february")){
    			cur="02";
    		} else if(cur.equals("march")){
    			cur="03";
    		} else if(cur.equals("april")){
    			cur="04";
    		} else if(cur.equals("may")){
    			cur="05";
    		} else if(cur.equals("june")){
    			cur="06";
    		} else if(cur.equals("july")){
    			cur="07";
    		} else if(cur.equals("august")){
    			cur="08";
    		} else if(cur.equals("september")){
    			cur="09";
    		} else if(cur.equals("october")){
    			cur="10";
    		} else if(cur.equals("november")){
    			cur="11";
    		} else if(cur.equals("december")){
    			cur="12";
    		}
    		dates[i]=cur+dates[i].substring(dates[i].indexOf(" ")+1)+year;
    	}
    }

    public void printAll(){
    	for(MealMenu menu : menus){
    		System.out.println("Commons: " + menu.getCommonsName());
    		System.out.println("Start Time: " + DateTimeFormat.mediumDateTime().print(menu.getMealInterval().getStart()));
    		System.out.println("End Time: " + DateTimeFormat.mediumDateTime().print(menu.getMealInterval().getEnd()));
    		System.out.println("Mod Time: " + DateTimeFormat.mediumDateTime().print(menu.getModDate()));
    		System.out.println("Meal Name: " + menu.getMealName());
    		System.out.println("Venues: ");
    		for(Venue ven : menu.getVenues()){
    			System.out.println("    Venue Name: " + ven.getName());
    			System.out.println("    Food Items:");
    			for(FoodItem food : ven.getFoodItems()){
    				System.out.println("        " + food.getName()+" " + food.isVegan() +" "+ food.isVegetarian());
    			}
    		}
    	}
    }
    
    private static long combineAndConvertToMillis(String dates, String time){
    	long result =(new DateTime(
        			Integer.parseInt(dates.substring(4)),
        			Integer.parseInt(dates.substring(0,2)),
        			Integer.parseInt(dates.substring(2,4)),
        			Integer.parseInt(time.substring(0,2)),
        			Integer.parseInt(time.substring(2))
    				,00,00)).getMillis();

    	return result;
    }
    
    private  void createMealMenusForMeal(ArrayList<ArrayList<String>> arrOfArr, String commons, String[] dates, String[] mealTimes, long modDateMillis, String mealName){
    	
    	for(int i = 0; i<arrOfArr.size();i++){
    		if(!hasMoreThanVenues(arrOfArr.get(i),commons)) continue;
    		ArrayList<Venue> venues = new ArrayList<Venue>();
    		ArrayList<FoodItem> foodItems = null;
    		for(String st : arrOfArr.get(i)){
//    			System.out.println(st);
    			if(lineIsVenue(st,commons)){
    				venues.add(new Venue(contentOfLine(st),new ArrayList<FoodItem>()));
    				foodItems = venues.get(venues.size()-1).getFoodItems();
    				continue;
    			}
    			st = contentOfLine(st);
    			foodItems.add(new FoodItem(fixAndSigns(st),isVegan(st), isVgt(st)));
    		}
    		long startMillis = combineAndConvertToMillis(dates[i], mealTimes[0]);
        	long endMillis  = combineAndConvertToMillis(dates[i], mealTimes[1]);
    		menus.add(new MealMenu(commons.substring(0,commons.indexOf(" Commons")),
    				startMillis,endMillis,modDateMillis, venues, mealName));
    	}
    }
    
    
    private static int[] splitIntsFromPValInLine(String in){
    	in=in.substring(in.indexOf("p=\"")+3,in.indexOf("\"",in.indexOf("p=\"")+3));
    	String[] ar = in.split(",");
    	int[] result = new int[ar.length];
    	for(int i = 0;i<ar.length;i++){
    		result[i]=Integer.parseInt(ar[i]);
//    		System.out.print(result[i]+",");
    	}
//    	System.out.println();
    	return result;
    }
    
    private static boolean isTwoEntries(String line){
    	int width = Integer.parseInt(line.substring(line.indexOf("w=\"")+3,line.indexOf('\"',line.indexOf("w=\"")+3)));
    	return (width>130);
    }
    
    private static boolean lineIsVenue(String line, String commonsName){
    	line=contentOfLine(line).toLowerCase();
    	//For Carrillo venues...
    	if(commonsName.equals("Carrillo Commons") && (line.equals("grill (cafe)") || line.equals("bakery") ||
    			line.equals("salads") || line.equals("deli")||
    			line.equals("mongolian grill") || line.equals("euro")||
    			line.equals("pizza") || line.equals("pasta"))) return true;
    	//For DLG venues...
    	if(commonsName.equals("De La Guerra Commons") && (
    			line.equals("blue plate special") || line.equals("taqueria (east side)") ||
    			line.equals("pizza") || line.equals("to order")||line.equals("grill (cafe)") ||
    			line.equals("salads/deli (west side)") || line.equals("bakery"))) return true;
    	//For ortega venues...
    	if(commonsName.equals("Ortega Commons") && (line.equals("hot foods") || line.equals("bakery") ||
    			line.equals("salads"))) return true;
    	//For portola venues...
    	if(commonsName.equals("Portola Commons") && (line.equals("hot foods") || line.equals("specialty line") ||
    			line.equals("bakery") || line.equals("hot foods") || line.equals("salads"))) return true;
    	return false;
    }
    
    private static int getCurrentPos(String currentLine){
    	return Integer.parseInt(currentLine.substring(currentLine.indexOf("l=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("l=\"")+3)));
    }
    
    private static boolean isLineADate(String line){
    	line=contentOfLine(line);
    	try{
    		if(line.indexOf(" ")==-1 || line.indexOf(" ")+1==line.length()) return false;
    		Integer.parseInt(line.substring(line.indexOf(" ")+1));
    	} catch(NumberFormatException e){
    		return false;
    	}
    	line=line.substring(0,line.indexOf(" ")).toLowerCase();
    	return (line.equals("januarary")||line.equals("february")||line.equals("march")||line.equals("april")||line.equals("may")||line.equals("june")||line.equals("july")
    			||line.equals("august")||line.equals("september")||line.equals("october")||line.equals("november")||line.equals("december"));
    }
    
    private static void addPosnToArr(int[] arr, int posn){
		int openIndex = 0;
		while(arr[openIndex]!=0){
			if(arr[openIndex]==posn || arr.length==(openIndex+1)){
				openIndex=-1;
				break;
			}
			openIndex++;
		}
		if(openIndex!=-1) arr[openIndex]=posn;
    }
    
    private int getNearestDayIndex(int[][] positions, int posn){
    	int closest = 0;
    	int closestDiff = Math.abs(positions[0][0]-posn);
    	for(int i = 0; i< positions.length; i++){
    		for(int o = 0; o<positions[i].length;o++){
    			if(positions[i][o]==0) break;
    			if(positions[i][o]==posn) return i;
    		}
    	}
    	for(int i = 1; i<positions.length;i++){
    		if(Math.abs(positions[i][0]-posn)<closestDiff){
    			closest = i;
    			closestDiff = Math.abs(positions[i][0]-posn);
    		}
    	}
    	return closest;
    }
    
    private static int dayOfWeekFromLine(String cL){
    	cL = contentOfLine(cL);
    	
    	if(cL.equals("Monday")) return 1;
    	if(cL.equals("Tuesday")) return 2;
    	if(cL.equals("Wednesday")) return 3;
    	if(cL.equals("Thursday")) return 4;
    	if(cL.equals("Friday")) return 5;
    	if(cL.equals("Saturday")) return 6;
    	if(cL.equals("Sunday")) return 7;
    	return 0;
    }
    
    private static boolean isMealTimeLine(String currentLine){
    	currentLine = contentOfLine(currentLine);
    	return ((currentLine.startsWith("Breakfast") ||
    			currentLine.startsWith("Lunch")||
    			currentLine.startsWith("Dinner")||
    			currentLine.startsWith("Brunch")||
    			currentLine.startsWith("Late Night")) && 
    				(currentLine.toLowerCase().contains("am") ||
    				currentLine.toLowerCase().contains("pm")));
    }
    
    private static int whichMealIsIt(String currentLine){
    	currentLine = contentOfLine(currentLine);
    	if((currentLine.toLowerCase().contains("am") ||
				currentLine.toLowerCase().contains("pm"))==false) return -1;
    	if(currentLine.startsWith("Breakfast")) return 1;
    	if(currentLine.startsWith("Lunch")) return 2;
    	if(currentLine.startsWith("Dinner")) return 3;
    	if(currentLine.startsWith("Brunch")) return 4;
    	if(currentLine.startsWith("Late Night")) return 5;
    	return -1;
    }
    
    private static String contentOfLine(String inputLine){
    	return inputLine.substring(inputLine.indexOf('>')+1,inputLine.lastIndexOf('<'));
    }
    
    private static String fixStringTimeForAMPM(String inputTime){
    	if(inputTime.substring(inputTime.length()-2).toLowerCase().equals("pm")){
			inputTime=""+(Integer.parseInt(inputTime.substring(0,inputTime.indexOf(":")))+12)+inputTime.substring(inputTime.indexOf(":")+1,inputTime.length()-2);
		} else{
			inputTime=inputTime.substring(0,inputTime.indexOf(':'))+inputTime.substring(inputTime.indexOf(":")+1, inputTime.indexOf(":")+3);
			if(inputTime.length()==3) inputTime = "0"+inputTime;
		}
    	return inputTime;
    }
/*
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
        for (int i=0; i<7; i++) {
        	makeMenu(day[i], commonsName);
        }
        
        System.out.println(menus.get(0).getCommonsName()+" "+date[0]+" "+DateTimeFormat.mediumDateTime().print(menus.get(0).getMealInterval().getStart()));
    } //construct
    public static ArrayList<MealMenu> getMealMenu() {
    	return menus;
    }
    private static void makeMenu(Vector<String> s, String commonsName) {
    	ArrayList<Venue> vs = new ArrayList<Venue>();
    	ArrayList<FoodItem> foods = new ArrayList<FoodItem>();
    
		for (int i=s.size()-1; i>=0; i--) {
			if (isMealTime(s.get(i))) {
				menus.add(new MealMenu(commonsName, dateToLong(date[0]), dateToLong(date[6]), 0, vs, s.get(i)) );
				vs = new ArrayList<Venue>();
				
			} else if (isVenue(s.get(i), commonsName)) {
				vs.add(new Venue(s.get(i), foods));
				foods = new ArrayList<FoodItem>();
				
			} else {
				foods.add(new FoodItem(s.get(i), isVegan(s.get(i)), isVgt(s.get(i))));
				
			}
			
		}
    }
    private static long dateToLong(String s) {
    	String out = "";
    	s = s.toLowerCase();
    	if (s.contains("january")) {
    		out+="01";
    	} 
    	if (s.contains("febuary")) {
    		out+="02";
    	} 
    	if (s.contains("march")) {
    		out+="03";
    	} 
    	if (s.contains("april")) {
    		out+="04";
    	} 
    	if (s.contains("may")) {
    		out+="05";
    	} 
    	if (s.contains("june")) {
    		out+="06";
    	} 
    	if (s.contains("july")) {
    		out+="07";
    	} 
    	if (s.contains("august")) {
    		out+="08";
    	} 
    	if (s.contains("september")) {
    		out+="09";
    	} 
    	if (s.contains("october")) {
    		out+="10";
    	} 
    	if (s.contains("november")) {
    		out+="11";
    	} 
    	if (s.contains("december")) {
    		out+="12";
    	}
    	StringTokenizer getnum = new StringTokenizer(s);
    	getnum.nextToken();
    	out+=getnum.nextToken();
    	
    	return Long.parseLong(out);
    	
    }
    private static boolean isMealTime(String input) {
    	input = input.toLowerCase();
    	if (input.contains("breakfast")) {
    		return true;
    	} else if (input.contains("lunch")) {
    		return true;
    	} else if (input.contains("brunch")) {
    		return true;
    	} else if (input.contains("dinner")) {
    		return true;
    	} else if (input.contains("late night")) {
    		return true;
    	} else {
    		return false;
    	}
    	
    	
    	}
    private static boolean isVenue(String food, String commons) {
    	food = food.toLowerCase();
    	commons = commons.toLowerCase();
    	if (commons.contains("ortega")) {
    		if (food.contains("bakery"))
    			return true;
    		else if (food.contains("hot food"))
    			return true;
    		else if (food.contains("salads"))
    			return true;
    	} else if (commons.contains("carrillo")) {
    		
    	} else if (commons.contains("portola")) {
    		
    	} else if (commons.contains("guerra")) {
    		
    	}
    	return false;
    }
    private static boolean isVegan(String food) {
    	food = food.toLowerCase();
    	if (food.contains("vegan")) {
    		return true;
    	} else {
    		return false;
    	}
    }
    private static boolean isVgt(String food) {
    	food = food.toLowerCase();
    	if (food.contains("vgt")) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private static void jump(int lines) {
        //moves scanning position of the menu file
        for(int i=0; i<lines; i++) {
            file.nextLine();
        }
    }
    private static String getText(String input) {
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
    private static int getNum(String input) {
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
    private static boolean hasText(String input) {
        if ((getText(input).equals("/notext"))) {
            return false;
        } else {
            return true;
        }

    }
    private static boolean hasNum(String input) {
        if ((getNum(input)==-1)) {
            return false;
        } else {
            return true;
        }
    }*/

}//class