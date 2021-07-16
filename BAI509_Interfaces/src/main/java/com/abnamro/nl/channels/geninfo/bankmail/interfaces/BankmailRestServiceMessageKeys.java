package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.messages.MessageKey;

/**
 * BankmailRestServiceMessageKeys It holds message keys for Bankmail Rest
 * Services.
 * 
 * <PRE>
 * Developer         Date       Change Reason	  Change
 * ----------------- ---------- ----------------- ----------------------------------------------
 * TCS       		15-10-2012 Initial version	  Release 1.0
 * </PRE>
 * 
 * @author TCS
 * @see
 */
public class BankmailRestServiceMessageKeys {

	/**
	* Tridion Message key : MESSAGE_RST509_0001
	* Description : Error when the mailbox name is not provided 
	*/
   public static final MessageKey ERROR_MAILBOX_NAME_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0001");
   
	/**
	* Tridion Message key : MESSAGE_RST509_0002
	* Description : Error when the message id is not provided 
	*/
   public static final MessageKey ERROR_MESSAGE_ID_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0002");
   /**
	* Tridion Message key : MESSAGE_RST509_0003
	* Description : Error when the message id is not provided 
	*/
   public static final MessageKey ERROR_INVALID_MESSAGE_TYPE_PROVIDED = new MessageKey("MESSAGE_RST509_0003");  
	
   	/**
	* Tridion Message key : MESSAGE_RST509_0004
	* Description : Error when invalid page number provided 
	*/   
   	public static final MessageKey ERROR_INVALID_PAGE_NO = new MessageKey("MESSAGE_RST509_0004");
   	
   	/**
	* Tridion Message key : MESSAGE_RST509_0005
	* Description : Error when invalid page size provided 
	*/
   	public static final MessageKey ERROR_INVALID_PAGE_SIZE = new MessageKey("MESSAGE_RST509_0005");
   	
   	/**
	 * Tridion Message key : MESSAGE_RST509_0006 Description : Error: page size is mandatory
	 * when page number is provided. Invalid page size provided. 
	 */
	public static final MessageKey ERROR_INVALID_PAGE_SIZE_FOR_PAGE_NO = new MessageKey(
			"MESSAGE_RST509_0006");
	    
    /**
	* Tridion Message key : MESSAGE_RST509_0007
	* Description : Error when the folder name is not provided 
	*/
    public static final MessageKey ERROR_FOLDER_NAME_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0007");
      
    /**
     * Tridion Message key : MESSAGE_RST509_0008
     * Description : Error when the mailbox  is not found 
     */
    public static final MessageKey ERROR_MAILBOX_NOT_FOUND11 = new MessageKey("MESSAGE_RST509_0008");
  
    /**
     * Tridion Message key : MESSAGE_RST509_0009
     * Description : Error when the create Message input not provided  
     */
    public static final MessageKey ERROR_CREATE_MESSAGE_INPUT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0009");
    
    /**
     * Tridion Message key : MESSAGE_RST509_0010
     * Description : Error when the update Message input not provided  
     */
    public static final MessageKey ERROR_UPDATE_MESSAGE_INPUT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0010");
  
    /**
     * Tridion Message key : MESSAGE_RST509_0011
     * Description : Error when the delete Messages input not provided  
     */
    public static final MessageKey ERROR_DELETE_MESSAGES_INPUT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0011");
      
  
    /**
     * Tridion Message key : MESSAGE_RST509_0012
     * Description : Error when the invalid message id is provided 
     */
    public static final MessageKey ERROR_INVALID_MESSAGE_ID_PROVIDED = new MessageKey("MESSAGE_RST509_0012");
 
    /**
     * Tridion Message key : MESSAGE_RST509_0013
     * Description : Error when the send Message input not provided  
     */
    public static final MessageKey ERROR_SEND_MESSAGE_INPUT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0013");
 
    /**
     * Tridion Message key : MESSAGE_RST509_0014
     * Description : Error when the invalid folder name provided  
     */
    public static final MessageKey ERROR_INVALID_FOLDER_NAME_PROVIDED = new MessageKey("MESSAGE_RST509_0014");
 
    /**
     * Tridion Message key : MESSAGE_RST509_0015
     * Description : Error when the invalid sort order is provided 
     */
    public static final MessageKey ERROR_INVALID_SORT_ORDER_PROVIDED = new MessageKey("MESSAGE_RST509_0015");
 
    /**
     * Tridion Message key : MESSAGE_RST509_0016
     * Description : Error when the invalid Include Actions is provided 
     */
    public static final MessageKey ERROR_INVALID_INCLUDE_ACTIONS_PROVIDED = new MessageKey("MESSAGE_RST509_0016");
 
    /**
     * Tridion Message key : MESSAGE_RST509_0017
     * Description : Error when the concerning Customer not provided  
     */
    public static final MessageKey ERROR_CONCERNING_CUSTOMER_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0017");
    
    /**
     * Tridion Message key : MESSAGE_RST509_0018
     * Description : Error, Msec exception    
     */
    public static final MessageKey ERROR_MSEC_EXCEPTION = new MessageKey("MESSAGE_RST509_0018");
    
    /**
     * Tridion Message key : MESSAGE_RST509_0019
     * Description : Error when invalid concerning Customer provided  
     */
    public static final MessageKey ERROR_INVALID_CONCERNING_CUSTOMER_PROVIDED = new MessageKey("MESSAGE_RST509_0019");
    
    /**
	* Tridion Message key : MESSAGE_RST509_0020
	* Description : Error when the subject is not provided 
	*/
   public static final MessageKey ERROR_SUBJECT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0020");
   
   /**
	* Tridion Message key : MESSAGE_RST509_0021
	* Description : Error when the content is not provided 
	*/
   public static final MessageKey ERROR_CONTENT_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0021");
  
   /**
	* Tridion Message key : MESSAGE_RST509_0022
	* Description : Error when the isSeen is not provided 
	*/
   public static final MessageKey ERROR_IS_SEEN_NOT_PROVIDED = new MessageKey("MESSAGE_RST509_0022");
    
    /**
     * Tridion Message key : MESSAGE_RST509_9000
     * Description : Technical Exception
     */
    public static final MessageKey ERROR_TECHNICAL_EXCEPTION = new MessageKey("MESSAGE_RST509_9000");
    
    /**
	 * Tridion Message key : MESSAGE_RST509_2001 
	 * Description : Error when the 
	 * concerningCustomerBCNumber is not provided
	 */
	public static final MessageKey ERROR_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_2001");

	/**
	 * Tridion Message key : MESSAGE_RST509_2002 Description : Error when the
	 * concerningCustomerBCNumber is not valid.
	 */
	public static final MessageKey ERROR_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_INVALID = new MessageKey(
			"MESSAGE_RST509_2002");

	/**
	 * Tridion Message key : MESSAGE_RST509_3001 Description : Error when the
	 * mailMessageId is not provided
	 */
	public static final MessageKey ERROR_MAIL_MESSAGE_ID_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_3001");

	/**
	 * Tridion Message key : MESSAGE_RST509_3002 Description : Error when the
	 * mailMessageId is not valid.
	 */
	public static final MessageKey ERROR_MAIL_MESSAGE_ID_INVALID = new MessageKey(
			"MESSAGE_RST509_3002");

	/**
	 * Tridion Message key : MESSAGE_RST509_3003 Description : Error when the
	 * concerningCustomerBCNumber is not provided
	 */
	public static final MessageKey ERROR_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_3003");

	/**
	 * Tridion Message key : MESSAGE_RST509_3004 Description : Error when the
	 * concerningCustomerBCNumber is not valid.
	 */
	public static final MessageKey ERROR_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_INVALID = new MessageKey(
			"MESSAGE_RST509_3004");

	/**
	 * Tridion Message key : MESSAGE_RST509_3005 Description : PeriodUnit not provided.
	 * 
	 */
	public static final MessageKey ERROR_PERIOD_UNIT_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_3005");

	/**
	 * Tridion Message key : MESSAGE_RST509_3006 Description : PeriodUnit is Invalid
	 * 
	 */
	public static final MessageKey ERROR_PERIOD_UNIT_INVALID = new MessageKey(
			"MESSAGE_RST509_3006");

	/**
	 * Tridion Message key : MESSAGE_RST509_3007 Description : PeriodValue not provided
	 * 
	 */
	public static final MessageKey ERROR_PERIOD_VALUE_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_3007");
	
	/**
	 * Tridion Message key : MESSAGE_RST509_3008 Description : PeriodValue Invalid
	 * 
	 */
	public static final MessageKey ERROR_PERIOD_VALUE_INVALID = new MessageKey(
			"MESSAGE_RST509_3008");

	/**
	 * Tridion Message key : MESSAGE_RST509_3009 Description : MessageType not provided
	 * 
	 */
	public static final MessageKey ERROR_MAIL_MESSAGE_TYPE_NOT_PROVIDED = new MessageKey(
			"MESSAGE_RST509_3009");
	
	/**
	 * Tridion Message key : MESSAGE_RST509_3010 Description : MessageType InValid
	 * 
	 */
	public static final MessageKey ERROR_MAIL_MESSAGE_TYPE_INVALID = new MessageKey(
			"MESSAGE_RST509_3010");
	
}
