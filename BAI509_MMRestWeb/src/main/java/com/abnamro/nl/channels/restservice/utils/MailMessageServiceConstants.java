package com.abnamro.nl.channels.restservice.utils;

import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.PeriodUnit;

/**
 * MailMessageServiceConstants - constants related to MailMessageRestService<br>
 * <pre>
 * <b>History:</b>
 * Developer         Date           Change Reason     Change
 * ----------------- -------------  ----------------- ----------------------------------------------
 * TCS               5-Oct-12       Initial version   Release 1.0
 * </pre>
 * @author TCS
 * @see
 */
public final class MailMessageServiceConstants {

	private MailMessageServiceConstants(){}

	public static final  String UNDERSCORE = "_";
	public static final  String SEPERATOR = "\\|";

	public static final  String JSON_STRING_MAIL_MESSAGE_LIST = "mailMessageList" ;
	public static final  String JSON_STRING_MAIL_MESSAGES = "mailMessages" ;
	public static final  String JSON_STRING_MAIL_MESSAGE = "mailMessage" ;

	protected static final  MailMessageType[] VALID_MESSAGE_TYPES = {MailMessageType.COMMERCIAL, MailMessageType.CONVERSATION};
	protected static final  PeriodUnit[] VALID_PERIOD_UNIT = {PeriodUnit.MONTH};
	
}