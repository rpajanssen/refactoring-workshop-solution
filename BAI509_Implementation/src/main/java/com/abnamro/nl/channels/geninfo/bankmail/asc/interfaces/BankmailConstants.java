package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

/**
 * BankmailConstants
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
public class BankmailConstants {

	// Prefix to be added for accessing Mailbox of employee.
	// Example em{employeeId}.
	public static final String EMPLOYEE_MAILBOX_NAME_PREFIX = "em";
	// Prefix to be added for accessing Mailbox of BC user.
	// Example bc{bcnumber}.
	public static final String BC_MAILBOX_NAME_PREFIX = "bc";

	// Prefix to be added for accessing Mailbox of AT user.
	// Example at{bcnumber}.
	public static final String AT_MAILBOX_NAME_PREFIX = "at";

	// Special case user name "user"
	public static final String DEFAULT_MAILBOX_NAME = "user";

	// MSec Application Name
	public static final String MSEC_APP_NAME = "CYV_ApplicationBankmailPlus";

	/**
	 * Define Userclass constant for BC user
	 */
	public static final String BC_USERCLASS_CONSTANT = "1";

	/**
	 * Define Userclass constant for AT user
	 */
	public static final String AT_USERCLASS_CONSTANT = "2";

	/**
	 * Define employee constant for employee user
	 */
	public static final String EM_USERCLASS_CONSTANT = "3";

	public static final String ASC_RETAIL = "ASC";

	public static final String CNMB_YBB = "YBB";

	public static final String CNMB_RM = "CSU";

	public static final String PREFERRED_BANKING = "Preferred";

	public static final String PRIVATE_BANKING = "Private";

	public static final String CCA_MAILBOX_TEMPLATE_PREFERRED_CLASS = "PreferredBankerMailboxTemplate";
	public static final String CCA_MAILBOX_TEMPLATE_PRIVATE_CLASS = "PrivateBankerMailboxTemplate";

	public static final String INVESTMENT_ADVISOR = "15";
	public static final String PREFERRED_BANKER = "12";
	public static final String PRIVATE_BANKER = "33";

	public static final String SERVICE_CONCEPT = "ServiceConcept";
	public static final String SEGMENT = "Segment";
	public static final String CGC = "CGC";
	public static final String BO = "BO";
	public static final String SLA_MESSAGE_KEY = "slaMessageKey";
	public static final String CAN_SELECT_SUBJECT = "canSelectSubject";
	public static final String DEST_MAILBOX_TEMPLATE = "destinationMailboxTemplate";

	public static final String SERVICE_CONCEPT_BY_CGC_ROOT_NODE = "SERVICECONCEPTBYCGC";
	public static final String FILTERED_BO = "FILTEREDBOS";
	public static final String FILTERED_CUSTOMER_GROUPS_ROOT_NODE = "FILTEREDCUSTOMERGROUPS";

	public static final String GENESYS_ASC_ROOT_NODE = "GENESYSMAILBOXTEMPLATEASC";
	public static final String GENESYS_YBB_ROOT_NODE = "GENESYSMAILBOXTEMPLATEYBB";
	public static final String BO_MAILBOX_TEMPLATE_ROOT_NODE = "BOMAILBOXTEMPLATE";
	public static final String CCA_PREFERRED_ROOT_NODE = "CCAMAILBOXTEMPLATEPREFERRED";
	public static final String CCA_PRIVATE_ROOT_NODE = "CCAMAILBOXTEMPLATEPRIVATE";
	public static final String CSU_BO_CHARACTERISTIC = "BSI130";

	public static final String GENESYS_MAILBOX_TEMPLATE = "GenesysMailboxTemplate";
	public static final String BO_MAIL_TEMPLATE = "BOMailTemplate";
	public static final String CCA_MAILBOX_TEMPLATE = "CCAMailboxTemplate";

	public static final String DISPLAYNAME = "displayName";
	public static final String DISPLAYNAMEPREFIX = "displayNamePrefix";
	public static final String FALLBACK_STRATEGY = "fallbackStrategy";
	public static final String SIGNATURE = "signature";
	public static final String SIGNATURE_TEMPLATE = "signatureTemplate";
	public static final String CCAROLE = "ccaRole";

	/** Added for bankmail++ */
	public static final String GENESYS_MAILBOX_TEMPLATE_OPS = "genesysmailboxtemplateops";
	public static final String GENESYS_OPS_ROOT_NODE = "GENESYSMAILBOXTEMPLATEOPS";

	// DRAFT folder name
	public static final String DRAFT_FOLDER_NAME = "DRAFT";
	// SENT folder name
	public static final String SENT_FOLDER_NAME = "SENT";

	public static final String END_FUNCTION_PREFIX = "com/abnamro/security/endfunction/";

	/** MSEC Task id Starts **/
	public static final String MSEC_CLIENT_REACH_END_FUNCTION_DEFAULT = "54";
	public static final String MSEC_ENF_BANKMAIL_PREF_BANKING_DEFAULT = "404";
	public static final String MSEC_ENF_BANKMAIL_PRIVATE_BANKING_DEFAULT = "405";
	public static final String MSEC_ENF_BANKMAIL_ASC_YBB_DEFAULT = "406";
	public static final String MSEC_ENF_BANKMAIL_ASC_RETAIL_DEFAULT = "407";
	public static final String MSEC_ENF_BANKMAIL_CLIENT_SUPPORT_DEFAULT = "408";
	public static final String MSEC_ENF_OPERATIONS_SUPPORT_DEFAULT = "510";

	public static final String MI_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String MI_OPEN_EVENT = "OPEN";

	public static final String MI_DELETE_EVENT = "DELETE";

	public static final String MI_MESSAGE_LOG = "MESSAGE";

	public static final char COMMA_SEPARATOR = 0x2C;

	// 'C' = message is initiated by a client
	public static final String INITIATIVE_OF_BC = "C";

	// 'B' = message is initiated by the bank
	public static final String INITIATIVE_OF_BANK = "B";

	public static final String EMPTY_STRING = "";

	public static final int SUBSTR_THIRD_LETTER = 2;

	public static final int INT_ZERO = 0;

	// WS constants
	public static final String BANKMAIL_WEBSERVICE_URL = "BankMail_Webservice_URL";

	public static final String BANKMAIL_WEBSERVICE_PARM = "BankMail_Webservice_Parameters";

	public static final String BANKMAIL_WEBSERVICE_USER = "BankMail_Webservice_User";

	public static final String BANKMAIL_WEBSERVICE_PD = "BankMail_Webservice_Pd";
    public static final String LOG_EMPTY_WEBSERVICE_URL_EXCEPTION = "Empty CRM Webservice URL found in NSB";
	public static final String LOG_EMPTY_WEBSERVICE_PARAM = "Empty CRM Webservice Parameters found in NSB";
	public static final String LOG_EMPTY_WEBSERVICE_USER_EXCEPTION = "Empty CRM Webservice User found in NSB";

	public static final String LOG_EMPTY_WEBSERVICE_PD_EXCEPTION = "Empty CRM Webservice Pd found in NSB";
	public static final String REMOTE_CRM_CONN = "crmremoteconnection";
	public static final String LOG_WEBSERVICE_HEADER_EXCEPTION = "Error during setting the SOAP Header for  Web Service";

	public static final String BANKMAIL = "Bankmail";
	public static final String IS_AUTHORISED_FOR = "IsAuthorizedUserList";
	public static final String GET_USER_LIST = "GetUserList";

	public static final String RECORD_EXIST_N = "N";
	public static final String MAILBOX_NAME = "Mailbox Name : ";
	public static final String MAILBOX_NAME_0 = "mailboxName={0}";
	public static final String BANKMAIL_PLUS1_INPUT_USERSBTID = "Bankmail Input UserSbtId: {0}";
	public static final String BANKMAIL_PLUS1_OUTPUT_SBTACCESSID= "Bankmail Output SbtAccessId: {0}";
	public static final String BANKMAIL_PLUS1_RECORD_EXIST = "Record Exist: {0}";
	public static final String ERR_USER_002 = "ERR_USER_002";
	public static final String ERR_OPR_001 = "ERR_OPR_001";


	/*Added for JSON File Reading*/
	public static final String BANKMAIL_JSON_DATA_ISSUE = "BankMail_Json_Data_Issue";
}
