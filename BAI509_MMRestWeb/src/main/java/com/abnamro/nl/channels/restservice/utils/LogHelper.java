package com.abnamro.nl.channels.restservice.utils;
/**
 * LogHelper Helps in logging output
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
public class LogHelper {
	
	/**
	 * logger instance.
	 */
	private com.abnamro.nl.logging.log4j2.helper.LogHelper logger;
	
	/**
	 * Loghelper is used to log output to console. This is loghelper constructor (a parameterized one)
	 * @param className  : [Class] className
	 */
	public LogHelper (Class<?> className) {
		this.logger = new com.abnamro.nl.logging.log4j2.helper.LogHelper(className);
	}
	
	/**
	 * To log error message in a method with the errorkey.
	 * 
	 * @param logMethod     : [String] Method Name where error is logged.
	 * @param logMessageKey : [String] Log key of the Error.
	 * @param params        : [Object[]] Other inputs to be logged with more information about error.
	 */
	public void logError(String logMethod, String logMessageKey, Object... params) {
		logger.error(logMethod, logMessageKey, params);
	}
	
	/**
	 * To log error message in a method with the errorkey and exception stack.
	 * 
	 * @param logMethod     : [String] Method Name where error is logged.
	 * @param logMessageKey : [String] Log key of the Error.
	 * @param params        : [Object[]] Other inputs to be logged with more information about error.
	 * @param t             : [Throwable] Exception
	 */
	public void logError(String logMethod, String logMessageKey, Throwable t, Object... params) {
		logger.error(logMethod, logMessageKey, t, params);
	}
	
	/**
	 * For debug logging
	 * 
	 * @param logMethod     : [String] Method Name where error is logged. 
	 * @param debugString : [ String ] DebugString to be logged.
	 */
	public void logDebug(String logMethod, String debugString) {
		logger.debugHardCodedMessage(logMethod, debugString);
	}
	
}
