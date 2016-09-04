package com.amazon.logsprocessing.ip.strategy;

/**
 * Interface for validating the IP addresses. Validating the IP address has been
 * implemented as strategy as we will have the flexibility to support IPV6 as
 * well in the future by implementing one more strategy
 * 
 * @author Nittin Singla
 *
 */
public interface IPStrategy {

	/**
	 * checks whether the input string is a valid ip address.
	 * 
	 * @param ipAddress
	 * @return boolean
	 */
	public boolean isValidIPAddress(String ipAddress);
}
