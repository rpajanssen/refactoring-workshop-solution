package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.abnamro.nl.enumeration.MailMessageType;

/**
 * MailboxMessageCustomDTO
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * TCS				01-10-2012	Initial version	  Mailbox Resource_2.5
 * </PRE>
 * @author
 * @see
 */

public class MailMessageCustomDTO extends AbstractDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Unique message identifier.
	 */
	private String id;

	/**
	 * name of the actual sender of the message
	 */
	private String sender;

	/**
	 * Sender of the message.
	 */
	private MailContactCustomDTO from;

	/**
	 * Message recipient(s).
	 */
	private List<MailContactCustomDTO> to;

	/**
	 * Type of the message.
	 */
	private MailMessageType messageType;

	/**
	 * Flag indicating if reply on this message is allowed.
	 */
	private Boolean isReplyAllowed;

	/**
	 * The message to which this message is a reply.
	 */
	private MailMessageCustomDTO inReplyTo;

	/**
	 * Reference to the conversation thread this message is a part of.
	 */
	private String conversationId;

	/**
	 * Date/time when message was sent for sent/received messages, date/time of creation for draft messages.
	 */
	private Date originationDate;

	/**
	 * Date/time when this message will be deleted from mailbox
	 */
	private Date expiryDate;

	/**
	 * Message subject.
	 */
	private String subject;

	/**
	 * Message content.
	 */
	private String content;

	/**
	 * BC number of the customer concerning whom the message is sent.
	 */
	private String concerningCustomerBCNumber;

	/**
	 * deeplink content for the paperless project to be used by the downloads overview
	 */
	private MailDeepLinkCustomDTO deepLink;

	/**
	 * Represents links or buttons and its corresponding values attached to a mail message to trigger processes. This is
	 * applicable for High Priority Bank mail.
	 */
	private MailActionCustomDTO[] mailActionList;
	
	/**
	 * Document Type for differentiating in app alerts
	 */
	private String documentType;

	/**
	 * @return the mailActionList
	 */
	public MailActionCustomDTO[] getMailActionList() {
		return mailActionList;
	}

	/**
	 * @param mailActionList the mailActionList to set
	 */
	public void setMailActionList(MailActionCustomDTO[] mailActionList) {
		this.mailActionList = mailActionList;
	}

	/**
	 * @return String:Unique message identifier.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id String:Unique message identifier
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return String:name of the actual sender of the message
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender String:name of the actual sender of the message
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return MailContactCustomDTO:Sender of the message.
	 */
	public MailContactCustomDTO getFrom() {
		return from;
	}

	/**
	 * @param from MailContactCustomDTO:Sender of the message.
	 */
	public void setFrom(MailContactCustomDTO from) {
		this.from = from;
	}

	/**
	 * @return List<MailContactCustomDTO>:Message recipient(s).
	 */
	public List<MailContactCustomDTO> getTo() {
		return to;
	}

	/**
	 * @param to MailContactCustomDTO[]:Message recipient(s).
	 */
	public void setTo(List<MailContactCustomDTO> to) {
		this.to = to;
	}

	/**
	 * @return MailMessageType:Type of the message.
	 */
	public MailMessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType MailMessageType:Type of the message.
	 */
	public void setMessageType(MailMessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return Boolean:Flag indicating if reply on this message is allowed.
	 */
	public Boolean getIsReplyAllowed() {
		return isReplyAllowed;
	}

	/**
	 * @param isReplyAllowed Boolean:Flag indicating if reply on this message is allowed.
	 */
	public void setIsReplyAllowed(Boolean isReplyAllowed) {
		this.isReplyAllowed = isReplyAllowed;
	}

	/**
	 * @return String:Reference to the conversation thread this message is a part of.
	 */
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * @param conversationId String:Reference to the conversation thread this message is a part of.
	 */
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	/**
	 * @return Date:Date/time when message was sent for sent/received messages, date/time of creation for draft messages
	 */
	public Date getOriginationDate() {
		return originationDate;
	}

	/**
	 * @param originationDate Date:Date/time when message was sent for sent/received messages, date/time of creation for
	 *           draft messages
	 */
	public void setOriginationDate(Date originationDate) {
		this.originationDate = originationDate;
	}

	/**
	 * @return Date:ate/time when this message will be deleted from mailbox
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate Date:ate/time when this message will be deleted from mailbox
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return String:Message subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject String:Message subject.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return String:Message content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content String:Message content.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return MailMessageCustomDTO:The message to which this message is a reply.
	 */
	public MailMessageCustomDTO getInReplyTo() {
		return inReplyTo;
	}

	/**
	 * @param inReplyTo MailMessageCustomDTO:The message to which this message is a reply.
	 */
	public void setInReplyTo(MailMessageCustomDTO inReplyTo) {
		this.inReplyTo = inReplyTo;
	}

	/**
	 * @return String: BC number of the customer concerning whom the message is sent
	 */
	public String getConcerningCustomerBCNumber() {
		return concerningCustomerBCNumber;
	}

	/**
	 * @param concerningCustomerBCNumber String: BC number of the customer concerning whom the message is sent
	 */
	public void setConcerningCustomerBCNumber(String concerningCustomerBCNumber) {
		this.concerningCustomerBCNumber = concerningCustomerBCNumber;
	}

	/**
	 * @return MailDeepLinkCustomDTO: deeplink object for the paperless
	 */
	public MailDeepLinkCustomDTO getDeepLink() { return deepLink; }

	/**
	 * @param deepLink MailDeepLinkCustomDTO: deeplink for the paperless
	 */
	public void setDeepLink(MailDeepLinkCustomDTO deepLink) { this.deepLink = deepLink;	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

}
