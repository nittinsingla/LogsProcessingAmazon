package com.amazon.logsprocessing.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.amazon.logsprocessing.bo.LogLine;
import com.amazon.logsprocessing.ip.strategy.IPStrategy;
import com.amazon.logsprocessing.ip.strategy.impl.IPV4StrategyImpl;
import com.amazon.logsprocessing.parsedate.strategy.ParseDateTimeStrategy;
import com.amazon.logsprocessing.parsedate.strategy.impl.ParseTimeStrategyImpl;
import com.amazon.logsprocessing.template.impl.ParseLogLineImpl;
import com.amazon.logsprocessing.username.strategy.UserIdStrategy;
import com.amazon.logsprocessing.username.strategy.impl.AlphaNumericUserIdStrategy;

/**
 * Approach followed :
 * This is the main file for running the log processing application. It picks up
 * the file input.txt from the classpath and reads every single line, parses it
 *  
 * We will use buffer reader to read the file line by line I have placed the
 * file at the classpath. If any other path has to be specified, it can be done
 * by changing the input.txt to any other path
 * 
 * Once we read a line, we split it using ", " and get time, user action, ip
 * address and user id
 * 
 * then we validate that the time is in the format HH:mm:ss and the action is
 * either of LOGIN or LOGOUT and IP address is a valid IPv4 address and the user
 * id is alphanumeric for userid, I have made an assumption that it has to be
 * strictly alphanumeric i.e. it will consist of both alphabets and numbers and
 * it can start with only alphabet.
 * 
 * Also, I have assumed that the LOGOUT can only happen after LOGIN. If the log
 * line reads LOGOUT action before LOGIN, it will be ignored.
 * 
 * One more assumption is that if the user has multiple login sessions and when
 * the user logsout, the most recent session will be logged out. we are using
 * the stack there.
 * 
 * Any date/time which is in some format other than HH:mm:ss will be ignored.
 * 
 * If the user id is not alphanumeric, or if it doesn't start with an alphabet,
 * it will be ignored.
 * 
 * If the IP address is not a valid Ipv4 address, it will be ignored
 * 
 * we are keeping three collections : 
 * 
 * One for tracking the IP that recieved most distinct user logins
 * 
 * second one for tracking the user that has the highest number of sessions open.
 * 
 *  Third one for tracking the average session length in seconds.
 *  
 *  when we read a line, we are populating these collections with the respective data.
 * 
 * @author Nittin Singla
 *
 */
public class LogProcessingMainApp {

	public static void main(String[] args) {
		BufferedReader br = null;
		String sCurrentLine;

		try {

			br = new BufferedReader(new FileReader("input.txt"));
			LogsProcessor processor = new LogsProcessor();
			while ((sCurrentLine = br.readLine()) != null) {
				ParseLogLineImpl parseLogLine = getParseLogLine();
				LogLine logLine = parseLogLine.parse(sCurrentLine);
				if (logLine != null) {
					processor.populateIPToUserIdLoginMap(logLine);
					processor.populateUserToOpenSessionsMap(logLine);
					processor.populateIPToIPTrackerMap(logLine);
				}

			}
			// Calculate the IP that received the most distinct User-Logins
			String ipWithMostDistinctLogins = processor
					.getIPwithMostDistinctUserLogins();
			System.out
					.println("The IP that recieved most distinct User Logins is : "
							+ ipWithMostDistinctLogins);

			String userWithMaxOpenSessions = processor
					.getUserWithMaxOpenSessions();
			System.out
					.println("User that at one point had the highest number of sessions open is : "
							+ userWithMaxOpenSessions);
			String avgSessionLengthPerIpStr = processor
					.getAvgSessionLengthPerIP();
			System.out
					.println("average session length in seconds (time between Login and Logout event for same User) per IP is : ");
			System.out.println(avgSessionLengthPerIpStr);
		} catch (IOException e) {
			// in case of any exception, we are just printing the stack trace
			e.printStackTrace();
		}

	}

	private static ParseLogLineImpl getParseLogLine() {
		ParseLogLineImpl parseLogLine = new ParseLogLineImpl();
		IPStrategy ipStrategy = new IPV4StrategyImpl();
		parseLogLine.setIpStrategy(ipStrategy);
		ParseDateTimeStrategy parseDateStratgy = new ParseTimeStrategyImpl();
		parseLogLine.setParseDateStratgy(parseDateStratgy);
		UserIdStrategy userIdStrategy = new AlphaNumericUserIdStrategy();
		parseLogLine.setUserIdStrategy(userIdStrategy);
		return parseLogLine;
	}
}
