package com.mindflakes.TeamRED.UCSBScrape;

/**
 * @author Johan Henkens
 *
 */
public class LineErrorException extends RuntimeException {

	/**
	 * Constructs a new LineErrorException with the specified description.
	 * @param throwError String containing description of the LineErrorException that occurred
	 */
	public LineErrorException(String throwError){
		super(throwError);
	}
	
	
	/**
	 * Constructs a new LineErrorException with the specified description and cause.
	 * @param throwError description of the LineErrorException that occurred
	 * @param cause the cause of the Exception
	 */
	public LineErrorException(String throwError, Throwable cause){
		super(throwError,cause);
	}
	
	
}
