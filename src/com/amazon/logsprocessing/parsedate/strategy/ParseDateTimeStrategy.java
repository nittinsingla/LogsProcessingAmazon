package com.amazon.logsprocessing.parsedate.strategy;

import java.util.Date;

/**
 * Interface written for parsing dates. Date parsing logic is written as a
 * strategy as we will have the flexibility to support any date formats in the
 * future.
 * 
 * @author Nittin Singla
 *
 */
public interface ParseDateTimeStrategy {
	/**
	 * Method that takes date string as an input and returns the java.util.Date
	 * as return as per the implemented strategy
	 * 
	 * @param date
	 * @return java.util.Date
	 */
	public Date parse(String dateStr);
	
	/** method to check if the given date string is a valid date
	 * @return boolean 
	 */
	public boolean isValidDate(String dateStr);

}
