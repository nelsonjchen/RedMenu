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
		currentCommons = getLineBody(currentLine);

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
        	
        	
        	if(lineHasMealTime(currentLine)){
        		String currentMeal = getLineBody(currentLine);
        		
        		String currentMealStartTime = currentMeal.substring(currentMeal.indexOf('(')+1,currentMeal.indexOf('-'));
        		currentMealStartTime= fixBodyOfMealTimeLineForAMPM(currentMealStartTime.trim());
        		
        		String currentMealEndTime = currentMeal.substring(currentMeal.indexOf('-')+1,currentMeal.indexOf(')')); 
        		currentMealEndTime = fixBodyOfMealTimeLineForAMPM(currentMealEndTime.trim());
        		{
        			int meal = findMealNumberFromBodyOfMealNameLine(currentLine);
        			mealNames[meal-1]=currentMeal.substring(0,currentMeal.indexOf("(")-1);
        			mealTimes[meal-1][0]=currentMealStartTime;
        			mealTimes[meal-1][1]=currentMealEndTime;
        			switch(findMealNumberFromBodyOfMealNameLine(currentLine)){
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
        		{
        			int dayOfWeekNumber = dayOfWeekNumberFromLineBody(getLineBody(currentLine)); 
        			while(dayOfWeekNumber!=0){
        				if(!firstPass){
        					currentLine=file.nextLine();
        					dayOfWeekNumber = dayOfWeekNumberFromLineBody(getLineBody(currentLine));
        					continue;
        				}
        				{
        					int currentPos = getLineLValue(currentLine);
        					addPosnToArr(positions[dayOfWeekNumber-1],currentPos);
        				}
        				currentLine = file.nextLine();        		
    					dayOfWeekNumber = dayOfWeekNumberFromLineBody(getLineBody(currentLine));
        			}
        		}
        		while(isLineADate(currentLine)){
        			if(!firstPass){
        				currentLine=file.nextLine();
        				continue;
        			}
        			int currentPos = getLineLValue(currentLine);
        			int dayNear = getNearestDayIndex(positions,currentPos);
        			addPosnToArr(positions[dayNear],currentPos);
        			dates[dayNear]=getLineBody(currentLine);
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
        				int currentPos = getLineLValue(workingLine);
            			int dayNear = getNearestDayIndex(positions,currentPos);
            			
            			if(dayNear==6) isTwoEntries=false;
            			
            			else{
            				int[] pArr = getLinePValues(workingLine);
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
            				
            				String[] contentAr = getLineBody(workingLine).split(" ");
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
        			int currentPos = getLineLValue(currentLine);
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
    				venues.add(new Venue(getLineBody(st),new ArrayList<FoodItem>()));
    				foodItems = venues.get(venues.size()-1).getFoodItems();
    				continue;
    			}
    			st = getLineBody(st);
    			foodItems.add(new FoodItem(fixAndSigns(st),isVegan(st), isVgt(st)));
    		}
    		long startMillis = combineAndConvertToMillis(dates[i], mealTimes[0]);
        	long endMillis  = combineAndConvertToMillis(dates[i], mealTimes[1]);
    		menus.add(new MealMenu(commons.substring(0,commons.indexOf(" Commons")),
    				startMillis,endMillis,modDateMillis, venues, mealName));
    	}
    }
    
    

    
    private static boolean isTwoEntries(String line){
    	int width = Integer.parseInt(line.substring(line.indexOf("w=\"")+3,line.indexOf('\"',line.indexOf("w=\"")+3)));
    	return (width>130);
    }
    
    private static boolean lineIsVenue(String line, String commonsName){
    	line=getLineBody(line).toLowerCase();
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
    

    private static boolean isLineADate(String line){
    	line=getLineBody(line);
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
    
    private static int dayOfWeekNumberFromLineBody(String cL){  	
    	if(cL.equals("Monday")) return 1;
    	if(cL.equals("Tuesday")) return 2;
    	if(cL.equals("Wednesday")) return 3;
    	if(cL.equals("Thursday")) return 4;
    	if(cL.equals("Friday")) return 5;
    	if(cL.equals("Saturday")) return 6;
    	if(cL.equals("Sunday")) return 7;
    	return 0;
    }
    
    private static boolean lineHasMealTime(String currentLine){
    	currentLine = getLineBody(currentLine);
    	return ((currentLine.startsWith("Breakfast") ||
    			currentLine.startsWith("Lunch")||
    			currentLine.startsWith("Dinner")||
    			currentLine.startsWith("Brunch")||
    			currentLine.startsWith("Late Night")) && 
    				(currentLine.toLowerCase().contains("am") ||
    				currentLine.toLowerCase().contains("pm")));
    }
    
    private static int findMealNumberFromBodyOfMealNameLine(String currentLine){
    	currentLine = getLineBody(currentLine);
    	if((currentLine.toLowerCase().contains("am") ||
				currentLine.toLowerCase().contains("pm"))==false) return -1;
    	if(currentLine.startsWith("Breakfast")) return 1;
    	if(currentLine.startsWith("Lunch")) return 2;
    	if(currentLine.startsWith("Dinner")) return 3;
    	if(currentLine.startsWith("Brunch")) return 4;
    	if(currentLine.startsWith("Late Night")) return 5;
    	return -1;
    }
    
    private static String fixBodyOfMealTimeLineForAMPM(String inputTime){
    	if(inputTime.substring(inputTime.length()-2).toLowerCase().equals("pm")){
			inputTime=""+(Integer.parseInt(inputTime.substring(0,inputTime.indexOf(":")))+12)+inputTime.substring(inputTime.indexOf(":")+1,inputTime.length()-2);
		} else{
			inputTime=inputTime.substring(0,inputTime.indexOf(':'))+inputTime.substring(inputTime.indexOf(":")+1, inputTime.indexOf(":")+3);
			if(inputTime.length()==3) inputTime = "0"+inputTime;
		}
    	return inputTime;
    }
    
    //Getters and setters for the line values
    
    private static String getLineBody(String inputLine){
    	return inputLine.substring(inputLine.indexOf('>')+1,inputLine.lastIndexOf('<'));
    }
    
    private static String setLineBody(String inputLine, String bodyVal){
    	return inputLine.substring(0,inputLine.indexOf('>'+1))+bodyVal+inputLine.substring(inputLine.lastIndexOf('<'));
    }
    
    private static int getLineLValue(String currentLine){
    	return Integer.parseInt(getLineLString(currentLine));
    }
    
    private static String getLineLString(String currentLine){
    	return currentLine.substring(currentLine.indexOf("l=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("l=\"")+3));
    }
    
    private static String setLineLValue(String line, String lVal){
    	return line = line.substring(0,line.indexOf("l=\"")+3)+lVal+
    	line.substring(line.indexOf("\"",line.indexOf("l=\"")+3));
    }
    
    private static String setLineLValue(String line, int lVal){
    	return line = line.substring(0,line.indexOf("l=\"")+3)+lVal+
    	line.substring(line.indexOf("\"",line.indexOf("l=\"")+3));
    }
    
    private static int getLineTValue(String currentLine){
    	return Integer.parseInt(getLineTString(currentLine));
    }
    
    private static String getLineTString(String currentLine){
    	return currentLine.substring(currentLine.indexOf("t=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("t=\"")+3));
    }
    
    private static String setLineTValue(String line, String tVal){
    	return line = line.substring(0,line.indexOf("t=\"")+3)+tVal+
    	line.substring(line.indexOf("\"",line.indexOf("t=\"")+3));
    }
    
    private static String setLineTValue(String line, int tVal){
    	return line = line.substring(0,line.indexOf("t=\"")+3)+tVal+
    	line.substring(line.indexOf("\"",line.indexOf("t=\"")+3));
    }
    
    private static int getLineWValue(String currentLine){
    	return Integer.parseInt(getLineWString(currentLine));
    }
    
    private static String getLineWString(String currentLine){
    	return currentLine.substring(currentLine.indexOf("w=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("w=\"")+3));
    }
    
    private static String setLineWValue(String line, String wVal){
    	return line = line.substring(0,line.indexOf("w=\"")+3)+wVal+
    	line.substring(line.indexOf("\"",line.indexOf("w=\"")+3));
    }

    private static String setLineWValue(String line, int wVal){
    	return line = line.substring(0,line.indexOf("w=\"")+3)+wVal+
    	line.substring(line.indexOf("\"",line.indexOf("w=\"")+3));
    }
    
    private static int getLineHValue(String currentLine){
    	return Integer.parseInt(getLineHString(currentLine));
    }
    
    private static String getLineHString(String currentLine){
    	return currentLine.substring(currentLine.indexOf("h=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("h=\"")+3));
    }
    
    private static String setLineHValue(String line, String hVal){
    	return line = line.substring(0,line.indexOf("h=\"")+3)+hVal+
    	line.substring(line.indexOf("\"",line.indexOf("h=\"")+3));
    }
    
    private static String setLineHValue(String line, int hVal){
    	return line = line.substring(0,line.indexOf("h=\"")+3)+hVal+
    	line.substring(line.indexOf("\"",line.indexOf("h=\"")+3));
    }
    
    private static int[] getLinePValues(String in){
    	in=getLinePString(in);
    	String[] ar = in.split(",");
    	int[] result = new int[ar.length];
    	for(int i = 0;i<ar.length;i++){
    		result[i]=Integer.parseInt(ar[i]);
    	}
    	return result;
    }
    
    private static String getLinePString(String currentLine){
    	return currentLine.substring(currentLine.indexOf("p=\"")+3,currentLine.indexOf("\"", currentLine.indexOf("p=\"")+3));
    }
    
    private static String setLinePValue(String line, String pVal){
    	return line = line.substring(0,line.indexOf("p=\"")+3)+pVal+
    	line.substring(line.indexOf("\"",line.indexOf("p=\"")+3));
    }
    
    private static String setLinePValue(String line, int[] pVals){
    	String pVal = "";
    	for(int a : pVals){
    		pVal = pVal + a + ",";
    	}
    	if(pVal.charAt(pVal.length()-1)==',') pVal = pVal.substring(0,pVal.length()-1);
    	return setLinePValue(line,pVal);
    }
    
}