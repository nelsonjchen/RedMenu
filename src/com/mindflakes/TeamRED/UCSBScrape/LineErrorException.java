package com.mindflakes.TeamRED.UCSBScrape;

public class LineErrorException extends RuntimeException {

	public LineErrorException(String throwError){
		super(throwError);
	}
	
	public LineErrorException(String throwError, Throwable t){
		super(throwError,t);
	}
	
	
}
