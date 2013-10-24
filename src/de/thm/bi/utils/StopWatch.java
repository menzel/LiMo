package de.thm.bi.utils;

import java.util.Stack;

/**
 * This StopWathc class can be used to track runtimes.
 * 
 * @author Artur Klos
 * 
 */
public class StopWatch {
	private static Stack<Long> startTimes = new Stack<Long>();

	/**
	 * Starts to track a time. Starting several times is possible two times are
	 * tracked then.
	 */
	public static final void start() {
		startTimes.push(System.currentTimeMillis());
	}

	/**
	 * @return Returns the time delay between start and stop. If start was
	 *         called several times the latest start call will be obtained the
	 *         time delay will be calculated and the latest start call will be
	 *         deleted from the start stack
	 */
	public static final long stop() {
		return System.currentTimeMillis() - startTimes.pop();
	}

	/**
	 * Stops and prints the elapsed time to std out
	 * 
	 * @return Returns the elapsed time
	 */
	public static final long stopAndLog() {
		return stopAndLog("");
	}

	/**
	 * Stops and prints the elapsed time to std out with a postfix
	 * 
	 * @param postfixMsg
	 *            Msg that will be posted
	 * @return Returns the elapsed time
	 */
	public static final long stopAndLog(String postfixMsg) {
		long elapsedTime = stop();
		System.out.println("Elapsed Time: " + elapsedTime + " " + postfixMsg);
		return elapsedTime;
	}

}
