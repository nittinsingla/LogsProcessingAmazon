package com.amazon.logsprocessing.template;

import com.amazon.logsprocessing.bo.LogLine;

/**
 * Template defined to parse a single line from the log file
 * 
 * Defined as a template so that it is flexible to support multiple log formats
 * in the future
 * 
 * @author Nittin Singla
 *
 */
public abstract class ParseLogLineTemplate {

	/**
	 * Takes the log line and checks if it is valid as per the selected
	 * implementation
	 * 
	 * @param line
	 * @return boolean
	 */
	protected boolean isValidLine(String line) {
		String[] tokens = tokenizeLine(line);
		if (tokens.length != 4) {
			return false;
		}
		return isvalidDateTime(tokens[0]) && isValidUserAction(tokens[1])
				&& isValidIPAddress(tokens[2]) && isValidUserId(tokens[3]);
	}

	/**
	 * Parses a log line string into LogLine object
	 * 
	 * @param line
	 * @return LogLine
	 */
	public abstract LogLine parse(String line);

	/**
	 * Tokenizes the input String into LogLine object
	 * 
	 * @param line
	 * @return String[]
	 */
	protected abstract String[] tokenizeLine(String line);

	/**
	 * Checks whether the datetime is valid as per the strategy
	 * 
	 * @param dateTime
	 * @return boolean
	 */
	protected abstract boolean isvalidDateTime(String dateTime);

	/**
	 * Checks whether the user action is valid
	 * 
	 * @param action
	 * @return boolean
	 */
	protected abstract boolean isValidUserAction(String action);

	/**
	 * Checks whether the ip address is valid as per the strategy
	 * 
	 * @param ipAddress
	 * @return
	 */
	protected abstract boolean isValidIPAddress(String ipAddress);

	/**
	 * Checks whether the user id is valid as per the strategy
	 * 
	 * @param userId
	 * @return boolean
	 */
	protected abstract boolean isValidUserId(String userId);
}
