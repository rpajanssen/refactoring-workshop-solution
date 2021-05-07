package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

/**
 * Stores the constants required across the application called inside different classes.
 */
public class ArchivingConstants {
	
	public static final String STANDARD_XML_PI = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; 
	public static final String BODY_OPEN_TAG = "<body>";
	public static final String BODY_CLOSE_TAG = "</body>";
	public static final String ARCH_DETL_OPEN_TAG = "<archivingDetails>";
	public static final String ARCH_DETL_CLOSE_TAG = "</archivingDetails>";
	public static final String APP_DATA_OPEN_TAG = "<applicationData>";
	public static final String APP_DATA_CLOSE_TAG = "</applicationData>";
	public static final String BC_CARD_OWNER_OPEN_TAG = "<bcCardOwner>";
	public static final String BC_CARD_OWNER_CLOSE_TAG = "</bcCardOwner>";
	public static final String ARCHIVING_TIMESTAMP_OPEN_TAG = "<archivingTimeStamp>";
	public static final String ARCHIVING_TIMESTAMP_CLOSE_TAG = "</archivingTimeStamp>";
	
	public static final String EMPTY_SPACE = " ";
	public static final String CLOSE_TAG = ">";
	public static final String CLOSE_TAG_WITH_SLASH = "/>";
	
	
	public static final String BANKMAIL_MSG_OPEN_TAG = "<BankmailMessage";
	public static final String BANKMAIL_MSG_CLOSE_TAG = "</BankmailMessage>";
	
	public static final String ID = "id";
	public static final String SENDER = "sender";
	public static final String MESSAGE_TYPE = "messageType";
	public static final String IS_REPLY_ALLWD = "isReplyAllowed";
	public static final String CONV_ID = "conversationId";
	public static final String IN_REPLY_TO = "inReplyTo";
	public static final String ORGINATION_DATE = "originationDate";
	public static final String EXPIRY_DATE = "expiryDate";
	public static final String CONCRN_CUST_BCNUM = "concerningCustomerBCNumber";
	
	public static final String FROM_OPEN_TAG = "<from>";
	public static final String FROM_CLOSE_TAG = "</from>";
	
	public static final String MAIL_CONTACT_OPEN_TAG = "<MailContact";
	public static final String ADDRESS = "address";
	public static final String DISPLAY_NAME = "displayName";
	
	public static final String TO_OPEN_TAG = "<to>";
	public static final String TO_CLOSE_TAG = "</to>";
	
	public static final String SUBJECT_OPEN_TAG = "<subject>";
	public static final String SUBJECT_CLOSE_TAG = "</subject>";
	                                                                     
	public static final String CONTENT_OPEN_TAG = "<content>";
	public static final String CONTENT_CLOSE_TAG = "</content>";
	
	public static final String CDATA_OPEN_TAG = "<![CDATA[";
	public static final String CDATA_CLOSE_TAG = "]]>";
	public static final String APPID = "AppId";
}
