package com.amazon.logsprocessing.bo;

import java.util.Date;
import java.util.Map;
import java.util.Stack;

/**
 * Class that keeps ip address, logged in users and their login times map, total
 * sessions and sessions time
 * 
 * @author Nittin Singla
 *
 */
public class IPUserTracker {

	public IPUserTracker() {
		sessions = 0;
		totalLoginTime = 0l;
	}

	private String ipAddress;

	private Map<String, Stack<Date>> userToLoginTimeMap;

	private long totalLoginTime;

	private int sessions;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Map<String, Stack<Date>> getUserToLoginTimeMap() {
		return userToLoginTimeMap;
	}

	public void setUserToLoginTimeMap(
			Map<String, Stack<Date>> userToLoginTimeMap) {
		this.userToLoginTimeMap = userToLoginTimeMap;
	}

	public long getTotalLoginTime() {
		return totalLoginTime;
	}

	public void setTotalLoginTime(long totalLoginTime) {
		this.totalLoginTime = totalLoginTime;
	}

	public int getSessions() {
		return sessions;
	}

	public void setSessions(int sessions) {
		this.sessions = sessions;
	}

}
