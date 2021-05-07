package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.enumeration.MailMessageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
/**
 * MailMessage Stores message and other parameters for a mail
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MailMessage {

    /**
     * Unique identifier of the Mail Message.
     */
    private String id;

    /**
     * Display name of the actual sender of the Mail Message. Is not returned to IB user.
     */
    private String senderName;

    /**
     * Mail Message sender.
     */
    private MailContact from;

    /**
     * Message recipient(s).
     */
    private List<MailContact> to;

    /**
     * Type of the message.
     */
    private MailMessageType messageType;

    /**
     * Date/time when message was sent for sent/received messages, date/time of creation for draft messages.
     */
    private Long originationDate;

    /**
     * Date/time when this message will be deleted from mailbox.
     */
    private Long expiryDate;

    /**
     * Message subject.
     */
    private String subject;

    /**
     * Message content in HTML format.
     */
    private String content;

    /**
     * The message to which this message is a reply. Used only for reply messages..
     */
    private MailMessage inReplyTo;

    /**
     * Customer concerning whom the message is sent. A representative can send a Mail Message concerning a customer.
     */
    private BusinessContact concerningCustomer;

    /**
     * Mail Message deepLink.
     */
    private MailDeepLink deepLink;

    /**
     * A list of links or buttons in the mail message.
     */
    private MailAction[] mailActions;

    private String messageSubType;

    /**
     * @return String Unique message identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Unique message identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String Display name of the actual sender of the Mail Message
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @param senderName String Display name of the actual sender of the Mail Message
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * @return MailContact Mail Message sender
     */
    public MailContact getFrom() {
        return from;
    }

    /**
     * @param from MailContact Mail Message sender
     */
    public void setFrom(MailContact from) {
        this.from = from;
    }

    /**
     * @return MailContact[]:Message recipient(s)
     */
    public List<MailContact> getTo() {
        return to;
    }

    /**
     * @param to MailContact[]:Message recipient(s)
     */
    public void setTo(List<MailContact> to) {
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
     * @return Long:date/time of creation for draft messages
     */
    public Long getOriginationDate() {
        return originationDate;
    }

    /**
     * @param originationDate Long:date/time of creation for draft messages
     */
    public void setOriginationDate(Long originationDate) {
        this.originationDate = originationDate;
    }

    /**
     * @return Long:Date/time when this message will be deleted from mailbox
     */
    public Long getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate Long:Date/time when this message will be deleted from mailbox
     */
    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return String:Message subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject String:Message subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return String:Message content in HTML format
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content String:Message content in HTML format
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return MailMessage:The message to which this message is a reply. Used only for reply messages.
     */
    public MailMessage getInReplyTo() {
        return inReplyTo;
    }

    /**
     * @param inReplyTo MailMessage:The message to which this message is a reply. Used only for reply messages.
     */
    public void setInReplyTo(MailMessage inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    /**
     * @return BusinessContact:A representative can send a Mail Message concerning a customer.
     */
    public BusinessContact getConcerningCustomer() {
        return concerningCustomer;
    }

    /**
     * @param concerningCustomer BusinessContact:A representative can send a Mail Message concerning a customer.
     */
    public void setConcerningCustomer(BusinessContact concerningCustomer) {
        this.concerningCustomer = concerningCustomer;
    }

    /**
     * @return DeepLink: DeepLink value for a Message.
     */
    public MailDeepLink getDeepLink() {
        return deepLink;
    }
    /**
     * @param deepLink :DeepLink value for a Message.
     */
    public void setDeepLink(MailDeepLink deepLink) {
        this.deepLink = deepLink;
    }

    /**
     * @return the mailActions
     */
    public MailAction[] getMailActions() {
        return mailActions;
    }

    /**
     * @param mailActions the mailActions to set
     */
    public void setMailActions(MailAction[] mailActions) {
        this.mailActions = mailActions;
    }

    /**
     * @return the messageSubType
     */
    public String getMessageSubType() {
        return messageSubType;
    }

    /**
     * @param messageSubType the messageSubType to set
     */
    public void setMessageSubType(String messageSubType) {
        this.messageSubType = messageSubType;
    }

}