package com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces;

import java.io.IOException;
import java.util.List;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.FolderCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.FolderListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxMessageCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxMessageListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.resources.IncludeActions;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.abnamro.nl.enumeration.MessagesSortOrder;
import com.abnamro.nl.enumeration.PeriodUnit;

import com.abnamro.genj.generic.SecurityContext;
import org.xml.sax.SAXException;

/**
 * BankmailABPC Banknmail Application Business Process Controller handles all
 * business logic to access mailboxes, manipulate and send messages .
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
public interface BankmailABPC {
	/**
	 * Retrieves list of Mailboxes which can be accessed by logged in user. If
	 * includeActions parameter is provided, actions allowed on each mailbox are
	 * returned
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxListDTO which holds all mail boxes for logged in user.
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxListDTO getMailboxes(SecurityContext securityContext,
			IncludeActions includeActions) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves a mailbox. If includeActions parameter is provided, actions
	 * allowed on the mailbox are returned
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxCustomDTO mailbox DTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxCustomDTO getMailbox(SecurityContext securityContext,
			String mailboxName, IncludeActions includeActions)
            throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and
	 * content are taken from input. Employee must also choose message
	 * recipient. Reply messages have to have inReplyTo attribute filled in,
	 * only message id has to be provided. New message is created in "Draft"
	 * folder.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param message
	 *            MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public String createMailboxMessage(SecurityContext securityContext,
			String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Sends given message. Message has to be saved prior to sending. Message is
	 * removed from the folder after it is sent. During delivery it will be
	 * placed in the "Sent" folder. This operation is restricted to messages in
	 * "Draft" folder.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public void sendMailboxMessage(SecurityContext securityContext, String mailboxName,
			String messageId) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Permanently delete messages from the mailbox.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageIds
	 *            String[]
	 * @param fromFolder
	 *            String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public void deleteMailboxMessages(SecurityContext securityContext,
			String mailboxName, List<String> messageIds, String fromFolder)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves messages from given folder within given mailbox. If employee is
	 * logged in then bankmailAddress is returned in Contact otherwise it is
	 * skipped from output. Messages can be searched by given text. Messages can
	 * be filtered by MailboxMessageTypes. Result can be sorted by one of the
	 * available sort orders. Result can be paged.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param folderName
	 *            String
	 * @param messageTypes
	 *            MailboxMessageType[]
	 * @param searchText
	 *            String
	 * @param sortBy
	 *            MessagesSortOrder
	 * @param pageNumber
	 *            int
	 * @param pageSize
	 *            int
	 * @return MailboxMessageListDTO mailboxMessageListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 *
	 */
	public MailboxMessageListDTO getMailboxMessages(SecurityContext securityContext,
			String mailboxName, MailboxFolderName folderName,
			List<MailMessageType> messageTypes, String searchText,
			MessagesSortOrder sortBy, Integer pageNumber, Integer pageSize)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is
	 * returned in Contact otherwise it is skipped from output. If
	 * includeActions parameter is provided, actions allowed on the message are
	 * returned.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxMessageCustomDTO getMailboxMessage(SecurityContext securityContext,
			String mailboxName, String messageId, IncludeActions includeActions)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only
	 * messages available in the folders "Inbox", "Archive" and "Sent" are
	 * returned. Messages are returned in reversed chronological order (latest
	 * first). If message has receivedDate (received messages only), then it is
	 * used for sorting, otherwise originationDate is used.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @return MailboxMessageListDTO mailboxMessageListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailboxMessageListDTO getMailboxMessageThread(SecurityContext securityContext,
			String mailboxName, String messageId)
			throws BankmailApplicationException;

	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All
	 * other changes will be ignored. For messages in "Draft" folder subject and
	 * content can be updated. Employee can also change message recipient.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param message
	 *            MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public String updateMailboxMessage(SecurityContext securityContext,
			String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves all folders of a mailbox.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @return FolderListDTO list of folders in a mail as ListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public FolderListDTO getFolders(SecurityContext securityContext, String mailboxName)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves a folder from given mailbox.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            mailboxName
	 * @param folderName
	 *            MailboxFolderName
	 * @return FolderCustomDTO folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public FolderCustomDTO getFolder(SecurityContext securityContext, String mailboxName,
			MailboxFolderName folderName) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Description Retrieves a list of SendBankmailPolicy for customers which
	 * can be selected by logged in user in the attribute "concerningCustomer"
	 * in new messages.
	 * 
	 * List is always empty for employees. List can be empty for Internet
	 * Banking clients, in which case they are not allowed to create/send new
	 * messages.
	 * 
	 * Actions CREATE_MESSAGE, SEND_MESSAGE will be disallowed in this case (see
	 * getMailbox, getMailboxes, MailboxActionName).
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @return sendBankmailPolicyListDTO SendBankmailPolicyListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public SendBankmailPolicyListDTO getSendBankmailPolicies(
			SecurityContext securityContext) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves the mail message history for an input customer. 
	 * All mail messages matching input customer = concerningCustomer are 
	 * returned. The mail messages are sorted in the descending order of 
	 * originationDate (from the most recent to the older ones).
	 * @param securityContext SecurityContext
	 * @param concerningCustomerBCNumber Concerning Customer BC Number
	 * @param periodValue  period Value as a Integer
	 * @param periodUnit PeriodUnit
	 * @param messageTypes List<MailMessageType>
	 * @return MailMessageListDTO
	 * @throws BankmailApplicationException bankmailApplicationException 
	 */
	public MailMessageListDTO getMailMessageHistory(SecurityContext securityContext,
			String concerningCustomerBCNumber, Integer periodValue,
			PeriodUnit periodUnit, List<MailMessageType> messageTypes)
			throws BankmailApplicationException;

	/**
	 * Gets the details of a mail message. Only allowed for bank employees.
	 * @param securityContext securityContext
	 * @param id String
	 * @return MailMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException 
	 */
	public MailMessageCustomDTO getMailMessage(SecurityContext securityContext, String id)
			throws BankmailApplicationException;

	/**
	 * Retrieve all mail contacts for a selected customer. 
	 * This operation is open to bank employees only, 
	 * and is used to fill in the recipient list while creating a new 
	 * Mail Message. These mail contacts are essentially representatives 
	 * of the customer who have an access to the Bankmail application
	 * @param securityContext securityContext
	 * @param concerningCustomerBCNumber concerning Customer BC Number
	 * @return MailContactListDTO 
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailContactListDTO getMailContacts(SecurityContext securityContext,
			Long concerningCustomerBCNumber)
			throws BankmailApplicationException;
}
