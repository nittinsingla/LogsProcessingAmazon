package com.amazon.logsprocessing.bo;

import java.util.Date;

import com.amazon.logsprocessing.user.actions.UserAction;

/**
 * BO representing a valid log line that we are interested in
 * 
 * @author Nittin Singla
 *
 */
public class LogLine {

	private String dateStr;

	private String ipAddress;

	private UserAction userAction;

	private String userId;
	
	private Date date;
	
	private String logLineStr;

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public UserAction getUserAction() {
		return userAction;
	}

	public void setUserAction(UserAction userAction) {
		this.userAction = userAction;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLogLineStr() {
		return logLineStr;
	}

	public void setLogLineStr(String logLineStr) {
		this.logLineStr = logLineStr;
	}

}
