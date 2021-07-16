package com.abnamro.nl.channels.restservice;

/**
 * MailboxConstants 
 * It holds Mailbox Rest Web layer Constants.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	10-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class MailboxConstants {

	/**
	 * The minimum for page number.
	 */
	public static final Integer MIN_PAGE_NUMBER = 0;

	/**
	 * The minimum for page size.
	 */
	public static final Integer MIN_PAGE_SIZE = 0;

	/**
	 * The maximum for page size.
	 */
	public static final Integer MAX_PAGE_SIZE = Integer.MAX_VALUE;

	/**
	 * The maximum allowed message id value.
	 */
	public static final Long MAX_MESSAGE_ID = 999999999L;

	/**
	 * The minimum allowed message id value.
	 */
	public static final Long MIN_MESSAGE_ID = 1L;

	/**
	 * Logging application for MSec call 
	 */
	public static final String MSEC_APP_NAME = "CYV_ApplicationBankmailPlus";

}
