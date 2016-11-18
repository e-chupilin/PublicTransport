package com.epam.training.utilites;

import org.apache.log4j.Logger;
import static com.epam.training.constants.Constants.*;

public class Utility {
	private final static String PUBLIC_TRANSPORT_LOGGER = "PublicTransportLogger";
	private final static Logger logger = Logger.getLogger(PUBLIC_TRANSPORT_LOGGER);
	private final static String ARROW = " => ";
	private final static String ERROR_SLEEP = "Set thread sleep error: ";
	private final static int RATIO_SLEEP = 100;
	private final static int LINE_LENGHT = 50;

	public static void logLine() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < LINE_LENGHT; i++) {
			str.append(DASH);
		}
		logger.info(str);
	}

	/*
	 * Since there are no specific requirements for logging, I used one logger
	 * for all threads. I think for this problem is the solution allows more
	 * clearly track the action threads. For example : <Kypalavskaja[4]> =>
	 * [Passenger-21<waiting>] I am wake UP! Add me at [Bus-22[0-3]]
	 */
	public static void logData(Object Obj, String data) {
		logger.info(Obj + ARROW + data);
	}

	public static void logErr(Object obj, String data) {
		logger.error(obj + COLON + data);
	}

	public static void sleep(long sec) {
		try {
			Thread.sleep(sec * RATIO_SLEEP);
		} catch (InterruptedException e) {
			logger.error(ERROR_SLEEP, e);
			throw new IllegalArgumentException(ERROR_SLEEP + Long.toString(sec));
		}
	}

	public static String makeSquareHooks(String str) {
		return LEFT_SQUARE_HOOKS + str + RIGHT_SQUARE_HOOKS;
	}

	public static String makeSquareHooks(int i) {
		return makeSquareHooks(Integer.toString(i));
	}

	public static String makeAngleHooks(String str) {
		return LEFT_ANGLE_HOOKS + str + RIGHT_ANGLE_HOOKS;
	}

	public static String makeAngleHooks(int i) {
		return makeAngleHooks(Integer.toString(i));
	}
}