package com.amazon.logsprocessing.parsedate.strategy.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazon.logsprocessing.parsedate.strategy.ParseDateTimeStrategy;

/**
 * Strategy implementation for parsing dates in the format : HH:mm:ss
 * 
 * @author Nittin Singla
 *
 */
public class ParseTimeStrategyImpl implements ParseDateTimeStrategy {

	private static String DATE_FORMAT = "HH:mm:ss";

	@Override
	public Date parse(String dateStr) {
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		Date startDate = null;
		try {
			startDate = df.parse(dateStr);
		} catch (ParseException e) {
			// We will ignore the log line if the date is not valid
		}
		return startDate;
	}

	@Override
	public boolean isValidDate(String dateStr) {
		boolean isValid = true;
		try {
			new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
		} catch (ParseException e) {
			isValid = false;
		}
		return isValid;
	}
}
