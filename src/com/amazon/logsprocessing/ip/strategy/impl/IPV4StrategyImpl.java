package com.amazon.logsprocessing.ip.strategy.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazon.logsprocessing.ip.strategy.IPStrategy;

/**
 * Strategy class for validating the IPV4 address
 * @author Nittin Singla
 *
 */
public class IPV4StrategyImpl implements IPStrategy {

	private static String ONE_BYTE_ADDRESS_PATTERN = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])";

	private static final String IPV4_PATTERN = "^" + ONE_BYTE_ADDRESS_PATTERN
			+ "\\." + ONE_BYTE_ADDRESS_PATTERN + "\\."
			+ ONE_BYTE_ADDRESS_PATTERN + "\\." + ONE_BYTE_ADDRESS_PATTERN + "$";

	@Override
	public boolean isValidIPAddress(String ipAddress) {
		Pattern pattern = Pattern.compile(IPV4_PATTERN);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

}
