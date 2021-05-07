package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

/**
 * MailContactRestServiceLogMessages It holds log message for MailContact Rest
 * Service.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer         Date       Change Reason	  Change
 * ----------------- ---------- ----------------- ----------------------------------------------
 * TCS       27-09-2012 Initial version	  Release 1.0
 * </PRE>
 * 
 * @author TCS
 * @see
 */
public class BankmailRestServiceLogKeys {
	
	//Log constants for BAI509_RestWeb
	public static final String LOG_MAILBOX_NAME_NOT_PROVIDED 		= "LOG_RST509_0001";
	public static final String LOG_MESSAGE_ID_NOT_PROVIDED 		= "LOG_RST509_0002";
	public static final String LOG_ILLEGAL_ACCESS_EXCEPTION		= "LOG_RST509_0003";
	public static final String LOG_INVOCATION_TARGET_EXCEPTION	= "LOG_RST509_0004";
	public static final String LOG_ILLEGAL_MESSAGE_TYPE_PROVIDED 	= "LOG_RST509_0005";
	public static final String LOG_ENUM_EXCEPTION 				= "LOG_RST509_0006";	
	public static final String LOG_INVALID_PAGE_SIZE_PROVIDED 	= "LOG_RST509_0007";
	public static final String LOG_INVALID_PAGE_NO_PROVIDED 		= "LOG_RST509_0008";
	public static final String LOG_INVALID_PAGE_SIZE_FOR_PAGE_NO 	= "LOG_RST509_0009";
	public static final String LOG_NUMBER_FORMAT_EXCEPTION 		= "LOG_RST509_0010";
	public static final String LOG_FOLDER_NAME_NOT_PROVIDED 		= "LOG_RST509_0011";
	
	public static final String LOG_CREATE_MESSAGE_INPUT_NOT_PROVIDED = "LOG_RST509_0012";
	
	//At least one recipient is mandatory
	public static final String LOG_RECIPIENT_NOT_PROVIDED = "LOG_RST509_0013";
	
	public static final String LOG_UPDATE_MESSAGE_INPUT_NOT_PROVIDED = "LOG_RST509_0014";
	
	public static final String LOG_DELETE_MESSAGES_INPUT_NOT_PROVIDED = "LOG_RST509_0015";
	
	public static final String LOG_ILLEGAL_MESSAGE_ID_PROVIDED 	= "LOG_RST509_0016";
	
	public static final String LOG_SEND_MESSAGE_INPUT_NOT_PROVIDED = "LOG_RST509_0017";
	
	public static final String LOG_ILLEGAL_FOLDER_NAME_PROVIDED 		= "LOG_RST509_0018";
	
	public static final String LOG_INVALID_SORT_ORDER_PROVIDED 		= "LOG_RST509_0019";
	
	public static final String LOG_INVALID_INCLUDE_ACTIONS_PROVIDED 		= "LOG_RST509_0020";
		
	public static final String LOG_CONCERNING_CUSTOMER_NOT_PROVIDED = "LOG_RST509_0021";

	public static final String LOG_INVALID_CONCERNING_CUSTOMER_PROVIDED = "LOG_RST509_0022";
	
	public static final String LOG_CONSTRAINTS_VIOLATED_EXCEPTION = "LOG_RST509_0023";
	
	public static final String LOG_BANKMAIL_APPLICATION_EXCEPTION = "LOG_RST509_0024";
	
	public static final String LOG_ASC_MSEC_EXCEPTION = "LOG_RST509_0025";
	
	public static final String LOG_SUBJECT_NOT_PROVIDED = "LOG_RST509_0026";
	
	public static final String LOG_COTENT_NOT_PROVIDED = "LOG_RST509_0027";
	
	public static final String LOG_IS_SEEN_NOT_PROVIDED = "LOG_RST509_0028";
			
	public static final String LOG_TECHINCAL_EXCEPTION = "LOG_RST509_9000";

	//Log Constants for BankMailPolicy RestService
	public static final String LOG_BP_BANKMAIL_APPLICATION_EXCEPTION = "LOG_RST509_1001"; 
	public static final String LOG_BP_TECHINCAL_EXCEPTION = "LOG_RST509_9001";
	
	//Log Constants for MailContact RestService
	public static final String LOG_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED = "LOG_RST509_2001";

	public static final String LOG_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_INVALID = "LOG_RST509_2002";

	public static final String LOG_MAILCONTACT_CONSTRAINTS_VIOLATED_EXCEPTION = "LOG_RST509_2030";

	public static final String LOG_MAILCONTACT_BANKMAIL_EXCEPTION = "LOG_RST509_2031";

	//Log Constants for MailMessage RestService
	public static final String LOG_MAIL_MESSAGE_ID_NOT_PROVIDED = "LOG_RST509_3001";

	public static final String LOG_MAIL_MESSAGE_ID_INVALID = "LOG_RST509_3002";

	public static final String LOG_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED = "LOG_RST509_3003";

	public static final String LOG_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_INVALID = "LOG_RST509_3004";

	public static final String LOG_PERIOD_UNIT_NOT_PROVIDED = "LOG_RST509_3005";

	public static final String LOG_PERIOD_UNIT_INVALID = "LOG_RST509_3006";

	public static final String LOG_PERIOD_VALUE_NOT_PROVIDED = "LOG_RST509_3007";
	
	public static final String LOG_PERIOD_VALUE_INVALID = "LOG_RST509_3008";

	public static final String LOG_MAIL_MESSAGE_TYPE_NOT_PROVIDED = "LOG_RST509_3009";
	
	public static final String LOG_MAIL_MESSAGE_TYPE_INVALID = "LOG_RST509_3010";
	
	public static final String LOG_MAILMESSAGE_CONSTRAINTS_VIOLATED_EXCEPTION = "LOG_RST509_3030";

	public static final String LOG_MAILMESSAGE_BANKMAIL_EXCEPTION = "LOG_RST509_3031";
	
}
