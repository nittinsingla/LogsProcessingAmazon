package com.amazon.logsprocessing.bo;

public class User {
	private String userId;
	private Integer openSessions;
	private Integer maxOpenSessions;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getOpenSessions() {
		return openSessions;
	}

	public void setOpenSessions(Integer openSessions) {
		this.openSessions = openSessions;
	}

	public Integer getMaxOpenSessions() {
		return maxOpenSessions;
	}

	public void setMaxOpenSessions(Integer maxOpenSessions) {
		this.maxOpenSessions = maxOpenSessions;
	}

}
