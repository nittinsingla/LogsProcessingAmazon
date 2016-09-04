package com.amazon.logsprocessing.app;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.amazon.logsprocessing.bo.IPUserTracker;
import com.amazon.logsprocessing.bo.LogLine;
import com.amazon.logsprocessing.bo.User;
import com.amazon.logsprocessing.user.actions.UserAction;

/**
 * This is a file meant for processing the log file.
 * 
 * @author Nittin Singla
 *
 */
public class LogsProcessor {

	public LogsProcessor() {
		ipToUserLoginMap = new HashMap<String, Set<String>>();
		userIdToUserMap = new HashMap<String, User>();
		ipToIPTrackerMap = new HashMap<String, IPUserTracker>();
		maxLogins = 0;
		globalMaxOpenSessions = 0;
	}

	private Map<String, Set<String>> ipToUserLoginMap;

	public Map<String, Set<String>> getIpToUserLoginMap() {
		return ipToUserLoginMap;
	}

	// for theIP that received the most distinct User-Logins
	private int maxLogins;

	private String maxLoginIp;
	// For the User that at one point had the highest number of sessions open

	private String userWithMaxOpenSessions;

	private int globalMaxOpenSessions;

	private Map<String, User> userIdToUserMap;

	public Map<String, User> getUserIdToUserMap() {
		return userIdToUserMap;
	}

	private Map<String, IPUserTracker> ipToIPTrackerMap;

	public Map<String, IPUserTracker> getIpToIPTrackerMap() {
		return ipToIPTrackerMap;
	}

	/**
	 * Populates a map that contains userid as the key and user object as the
	 * value. On processing each valid log line, we calculate the max open
	 * sessions for that user id and add those to the user object inside the
	 * map. Also, we keep the track of maxOpenSessions amongst all the users and
	 * the user having max open sessions by populating private variables so that
	 * we don't have to scan the map again.
	 * 
	 * @param logLine
	 */
	public void populateUserToOpenSessionsMap(LogLine logLine) {
		String userId = logLine.getUserId();
		if (logLine.getUserAction().equals(UserAction.LOGIN)) {
			// In case of login, we are going to add to number of open sessions
			if (getUserIdToUserMap().containsKey(userId)) {
				User user = getUserIdToUserMap().get(userId);
				Integer maxOpenSessions = user.getMaxOpenSessions();
				Integer openSessions = user.getOpenSessions();
				user.setOpenSessions(openSessions + 1);
				if (user.getOpenSessions() > maxOpenSessions) {
					user.setMaxOpenSessions(user.getOpenSessions());
				}
				if (user.getMaxOpenSessions() > globalMaxOpenSessions) {
					globalMaxOpenSessions = user.getMaxOpenSessions();
					userWithMaxOpenSessions = userId;
				}
			} else {
				User user = new User();
				user.setUserId(userId);
				user.setOpenSessions(1);
				user.setMaxOpenSessions(1);
				getUserIdToUserMap().put(userId, user);
			}
		} else if (logLine.getUserAction().equals(UserAction.LOGOUT)) {
			// in case of logout, we are decrementing the number of open
			// sessions
			User user = getUserIdToUserMap().get(userId);
			user.setOpenSessions(user.getOpenSessions() - 1);

		}
	}

	/**
	 * It populates the map which has IP address as the key and set of users
	 * that have logged in to that IP Also, along with every valid log line that
	 * we process, we keep on populating the variable max logins and ip address
	 * that has the max logins. So that for getting the final ip address having
	 * the max logins, we don't have to scan the map again
	 * 
	 * @param logLine
	 */
	public void populateIPToUserIdLoginMap(LogLine logLine) {
		String ipAddress = logLine.getIpAddress();
		Set<String> users;
		if (getIpToUserLoginMap().containsKey(ipAddress)) {
			users = getIpToUserLoginMap().get(ipAddress);
		} else {
			users = new HashSet<String>();
			getIpToUserLoginMap().put(ipAddress, users);
		}
		if(logLine.getUserAction().equals(UserAction.LOGIN)){
			users.add(logLine.getUserId());	
		}
		
		if (users.size() > maxLogins) {
			maxLogins = users.size();
			maxLoginIp = ipAddress;
		}
	}

	/**
	 * populates a map with IP address as the key and IPTracker as the value.
	 * IPTracker is a class per ip address that keeps a track of users logged in
	 * and their login times, sessions
	 * 
	 * every time a user logs out, we keep on calculating the time diff between
	 * the login and logout and adding it up to the total login time so that we
	 * can calculate the average in the end.
	 * 
	 * @param logLine
	 */
	public void populateIPToIPTrackerMap(LogLine logLine) {
		String ipAddress = logLine.getIpAddress();
		String userId = logLine.getUserId();
		Date time = logLine.getDate();
		UserAction action = logLine.getUserAction();
		if (ipToIPTrackerMap.containsKey(ipAddress)) {
			IPUserTracker ipUserTracker = ipToIPTrackerMap.get(ipAddress);
			if (ipUserTracker.getUserToLoginTimeMap().containsKey(userId)) {
				Stack<Date> userLoginTimesStack = ipUserTracker
						.getUserToLoginTimeMap().get(userId);
				if (action.equals(UserAction.LOGIN)) {
					userLoginTimesStack.push(time);
				} else if (action.equals(UserAction.LOGOUT)) {
					if (userLoginTimesStack.size() == 0) {
						System.out
								.println("Ignoring the line as logout cannot be done before/without login ["
										+ logLine.getLogLineStr() + "]");
						return;
					}
					Date loginTime = userLoginTimesStack.pop();
					ipUserTracker.setSessions(ipUserTracker.getSessions() + 1);
					Long sessionTimeInSeconds = (time.getTime() - loginTime
							.getTime()) / 1000;
					ipUserTracker.setTotalLoginTime(ipUserTracker
							.getTotalLoginTime() + sessionTimeInSeconds);

				}
			} else {
				Stack<Date> loginTimesStack = new Stack<Date>();
				loginTimesStack.push(time);
				ipUserTracker.getUserToLoginTimeMap().put(userId,
						loginTimesStack);
			}
		} else {
			IPUserTracker ipUserTracker = new IPUserTracker();
			ipUserTracker.setIpAddress(ipAddress);
			Stack<Date> loginTimesStack = new Stack<Date>();
			loginTimesStack.push(time);
			Map<String, Stack<Date>> userToLoginTimeMap = new HashMap<String, Stack<Date>>();
			userToLoginTimeMap.put(userId, loginTimesStack);
			ipUserTracker.setUserToLoginTimeMap(userToLoginTimeMap);
			ipToIPTrackerMap.put(ipAddress, ipUserTracker);

		}

	}

	/**
	 * Returns the IP Address with most distinct user logins
	 */
	public String getIPwithMostDistinctUserLogins() {
		return maxLoginIp;
	}

	/**
	 * Returns the user which had the highest number of sessions open at one
	 * point
	 */
	public String getUserWithMaxOpenSessions() {
		return userWithMaxOpenSessions;
	}

	/**
	 * @returns the average session length in seconds per ip address as a string
	 *          in the format (IPv4 address: Average session length in seconds)
	 */
	public String getAvgSessionLengthPerIP() {
		StringBuffer result = new StringBuffer();
		for (Map.Entry<String, IPUserTracker> entry : ipToIPTrackerMap
				.entrySet()) {
			if (result.length() > 0) {
				result.append(", ");
			}
			IPUserTracker ipUserTracker = entry.getValue();
			Long avgSessionLength = ipUserTracker.getTotalLoginTime()
					/ ipUserTracker.getSessions();
			result = result.append("(" + entry.getKey() + ":"
					+ avgSessionLength + ")");
		}
		return result.toString();
	}
}
