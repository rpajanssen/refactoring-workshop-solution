package com.abnamro.nl.channels.geninfo.bankmail.resources;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * MailboxMessageWrapper Wrapper class for MailboxMessage
 *
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MailboxMessageWrapper {

    /**
     * Wrapper for MailboxMessage
     */
    private MailboxMessage mailboxMessage;

    /**
     * @return MailboxMessage
     */
    public MailboxMessage getMailboxMessage() {
        return mailboxMessage;
    }

    /**
     * @param mailboxMessage MailboxMessage
     */
    public void setMailboxMessage(MailboxMessage mailboxMessage) {
        this.mailboxMessage = mailboxMessage;
    }

}
