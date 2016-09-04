package com.amazon.logsprocessing.username.strategy.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazon.logsprocessing.username.strategy.UserIdStrategy;

/**
 * Class for validating the username as alphanumeric We have made an assumption
 * that the username will be strictly alpha numeric i.e it will start with an alphabet
 * and will also consist of at least one digit
 * 
 * Example : user1, USER1new, user123, user345id are valid user id's
 * And 12user, user, 123 are invalid user id's
 * 
 * @author Nittin Singla
 *
 */
public class AlphaNumericUserIdStrategy implements UserIdStrategy {

	private String ALPHA_NUMERIC_PATTERN = "^[\\p{Alpha}]+([\\p{Digit}]+?)+([\\p{Alpha}]*)";
	

	@Override
	public boolean isValidUserId(String userId) {
		Pattern alphaNumericPattern = Pattern.compile(ALPHA_NUMERIC_PATTERN);
		Matcher matcher = alphaNumericPattern.matcher(userId);
		return matcher.matches();
	}
}
