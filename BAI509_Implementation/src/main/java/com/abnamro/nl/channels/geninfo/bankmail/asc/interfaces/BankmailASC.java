package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.abnamro.nl.enumeration.MessagesSortOrder;
import com.abnamro.nl.enumeration.PeriodUnit;

import java.util.List;

/**
 * BankmailASC Bankmail 'Application Specific Controller' handles all business logic to access mailboxes, manipulate and
 * send messages.
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
public interface BankmailASC {
	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and content are taken from input. Attributes
	 * isSeen, subject and content are required. Employee must also choose message recipient. Reply messages have to have
	 * inReplyTo attribute filled in, only message id has to be provided. New message is created in "Draft" folder.
	 * @param mailboxName name of the mailbox
	 * @param message MailboxMessageCustomDTO which holds message information
	 * @return String message id
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public String createMailboxMessage(String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException;

	/**
	 * Delivers message to Bankmail mailboxes. Precondition: message must exist and be in the "DRAFT" folder. Sender of
	 * the message is set to the current user, originationDate to the current system time. Message is moved to "SENT"
	 * folder and if it is a reply message, the replied message gets isAnswered = TRUE.
	 * @param senderName String
	 * @param mailboxName String of mailbox name
	 * @param messageId String of message id
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void deliverMailboxMessage(String senderName, String mailboxName, String messageId)
			throws BankmailApplicationException;

	/**
	 * Deletes messages from a mailbox.
	 * @param mailboxName String of mailbox name
	 * @param messageIds String of message ids
	 * @param fromFolder from Folder name
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void deleteMailboxMessages(String mailboxName, List<String> messageIds, String fromFolder)
			throws BankmailApplicationException;

	/**
	 * Retrieves all folders of a mailbox.
	 * @param mailboxName String
	 * @return FolderCustomDTO[] list of folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<FolderCustomDTO> getFolders(String mailboxName) throws BankmailApplicationException;

	/**
	 * Retrieves a folder from given mailbox.
	 * @param mailboxName String
	 * @param folderName MailboxFolderName
	 * @return FolderCustomDTO folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public FolderCustomDTO getFolder(String mailboxName, MailboxFolderName folderName)
			throws BankmailApplicationException;

	/**
	 * Retrieves messages from given folder within given mailbox. If employee is logged in then bankmailAddress is
	 * returned in Contact otherwise it is skipped from output. Messages can be searched by given text. Messages can be
	 * filtered by MailboxMessageTypes. Result can be sorted by one of the available sort orders. Result can be paged.
	 * @param mailboxName String
	 * @param folderName MailboxFolderName
	 * @param messageTypes MailboxFolderName[]
	 * @param searchText String
	 * @param sortBy MessagesSortOrder
	 * @param pageNumber int
	 * @param pageSize int
	 * @return MailboxMessageCustomDTO[] mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<MailboxMessageCustomDTO> getMailboxMessages(String mailboxName, MailboxFolderName folderName,
			List<MailMessageType> messageTypes, String searchText, MessagesSortOrder sortBy, int pageNumber, int pageSize)
			throws BankmailApplicationException;

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is returned in Contact otherwise it is skipped
	 * from output. If includeActions parameter is provided, actions allowed on the message are returned.
	 * @param mailboxName String
	 * @param messageId String
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailboxMessageCustomDTO getMailboxMessage(String mailboxName, String messageId)
			throws BankmailApplicationException;

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only messages available in the folders "Inbox",
	 * "Archive" and "Sent" are returned. Messages are returned in reversed chronological order (latest first). If
	 * message has receivedDate (received messages only), then it is used for sorting, otherwise originationDate is used.
	 * @param mailboxName String of mailbox name
	 * @param messageId String of message Id
	 * @return MailboxMessageListDTO holds list of messages in same conversation
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws MessageNotFoundException messageNotFoundException thrown when message is not available
	 */
	public List<MailboxMessageCustomDTO> getMailboxMessageThread(String mailboxName, String messageId)
			throws BankmailApplicationException, MessageNotFoundException;

	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All other changes will be ignored. For
	 * messages in "Draft" folder subject and content can be updated. Employee can also change message recipient.
	 * @param mailboxName mailbox name
	 * @param oldMessage MailboxMessageCustomDTO to be updated
	 * @param newMessage MailboxMessageCustomDTO with message details to be updated
	 * @return String message id of updated message
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public String updateMailboxMessage(String mailboxName, MailboxMessageCustomDTO oldMessage,
			MailboxMessageCustomDTO newMessage) throws BankmailApplicationException;

	/**
	 * Retrieves the mail message history for an input customer. All mail messages matching input customer =
	 * concerningCustomer are returned. The mail messages are sorted in the descending order of originationDate (from the
	 * most recent to the older ones).
	 * @param concerningCustomerBCNumber String BC Number
	 * @param periodValue Integer
	 * @param periodUnit PeriodUnit
	 * @param messageTypes MailMessageType[]
	 * @return MailMessageCustomDTO[]
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public List<MailMessageCustomDTO> getMailMessages(String concerningCustomerBCNumber, Integer periodValue,
			PeriodUnit periodUnit, List<MailMessageType> messageTypes) throws BankmailApplicationException;

	/**
	 * Gets the details of a mail message.
	 * @param messageId String message id
	 * @return MailMessageCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public MailMessageCustomDTO getMailMessage(String messageId) throws BankmailApplicationException;

}
