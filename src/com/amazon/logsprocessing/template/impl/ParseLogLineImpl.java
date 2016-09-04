package com.amazon.logsprocessing.template.impl;

import com.amazon.logsprocessing.bo.LogLine;
import com.amazon.logsprocessing.ip.strategy.IPStrategy;
import com.amazon.logsprocessing.parsedate.strategy.ParseDateTimeStrategy;
import com.amazon.logsprocessing.template.ParseLogLineTemplate;
import com.amazon.logsprocessing.user.actions.UserAction;
import com.amazon.logsprocessing.username.strategy.UserIdStrategy;

/**
 * Class to parse the log line It will validate the line and parse it if it is
 * valid. As per the parser implemented, the following are valid log lines
 * 21:00:00, LOGIN, 10.0.0.1, user1
 * 
 * 21:02:00, LOGIN, 10.0.0.5, user1
 * 
 * 21:05:00,LOGOUT, 10.0.0.1, user1
 * 
 * The first string is valid time, followed by the user action which can be
 * LOGIN/LOGOUT, then followed by IPv4 address, and then the alpha numeric
 * username
 * 
 * @author Nittin Singla
 *
 */
public class ParseLogLineImpl extends ParseLogLineTemplate {
	private static String DELIMITER = ", ";

	private ParseDateTimeStrategy parseDateStratgy;

	private IPStrategy ipStrategy;

	private UserIdStrategy userIdStrategy;

	public ParseDateTimeStrategy getParseDateStratgy() {
		return parseDateStratgy;
	}

	public void setParseDateStratgy(ParseDateTimeStrategy parseDateStratgy) {
		this.parseDateStratgy = parseDateStratgy;
	}

	public IPStrategy getIpStrategy() {
		return ipStrategy;
	}

	public void setIpStrategy(IPStrategy ipStrategy) {
		this.ipStrategy = ipStrategy;
	}

	public UserIdStrategy getUserIdStrategy() {
		return userIdStrategy;
	}

	public void setUserIdStrategy(UserIdStrategy userIdStrategy) {
		this.userIdStrategy = userIdStrategy;
	}

	@Override
	public LogLine parse(String line) {
		if (!isValidLine(line)) {
			// if the line is not valid as per the strategy, we are returning
			// null
			return null;
		}

		String[] tokens = tokenizeLine(line);
		LogLine logLine = new LogLine();
		logLine.setLogLineStr(line);
		logLine.setDate(parseDateStratgy.parse(tokens[0]));
		logLine.setUserAction(getUserAction(tokens[1]));
		logLine.setIpAddress(tokens[2]);
		logLine.setUserId(tokens[3]);

		return logLine;
	}

	@Override
	protected String[] tokenizeLine(String line) {
		return line.split(DELIMITER);
	}

	@Override
	protected boolean isvalidDateTime(String dateTime) {
		return getParseDateStratgy().isValidDate(dateTime);
	}

	@Override
	protected boolean isValidUserAction(String action) {
		boolean isValid = false;
		UserAction[] userActions = UserAction.values();

		for (UserAction userAction : userActions) {
			if (userAction.toString().equalsIgnoreCase(action)) {
				isValid = true;
				break;
			}
		}
		return isValid;

	}

	/**
	 * Takes a string and returns the enum value for it
	 * 
	 * @param action
	 * @return
	 */
	private UserAction getUserAction(String action) {
		UserAction userAction = null;
		UserAction[] userActions = UserAction.values();

		for (UserAction usrActn : userActions) {
			if (usrActn.toString().equalsIgnoreCase(action)) {
				userAction = usrActn;
				break;
			}
		}
		return userAction;

	}

	@Override
	protected boolean isValidIPAddress(String ipAddress) {
		return getIpStrategy().isValidIPAddress(ipAddress);
	}

	@Override
	protected boolean isValidUserId(String userId) {
		return getUserIdStrategy().isValidUserId(userId);
	}

}
