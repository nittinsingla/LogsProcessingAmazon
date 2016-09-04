package com.amazon.logsprocessing.username.strategy;

/**
 * Interface to check the user names. It has been implemented as a strategy so
 * that we have the flexibility to support various regex on the username apart
 * from alphanumeric
 * 
 * @author Nittin Singla
 *
 */
public interface UserIdStrategy {
	public boolean isValidUserId(String userId);
}
