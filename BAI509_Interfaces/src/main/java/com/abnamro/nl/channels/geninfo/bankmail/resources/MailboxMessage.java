package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * MailboxMessage Stores properties for a mailbox message
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
public class MailboxMessage extends MailMessage {

    /**
     * Folder where the message is placed. Only folder name is filled in.
     */
    private MailboxFolder folder;

    /**
     * Message has been read.
     */
    private Boolean isSeen;

    /**
     * Message has been answered.
     */
    private Boolean isAnswered;

    /**
     * Date/time when message was received. Not set in sent messages.
     */
    private Long receivedDate;

    /**
     * When message is moved to "Deleted" folder, this attribute is set by the server to indicate the date after which the
     * message may be deleted.
     */
    private Long deleteAfterDate;

    /**
     * Folder name where the message was placed before it was removed. Only mesages in "REMOVED" folder have this
     * attribute filled in.
     */
    private MailboxFolder deletedFromFolder;

    /**
     * @return MailboxFolder:Folder where the message is placed
     */
    public MailboxFolder getFolder() {
        return folder;
    }

    /**
     * @param folder MailboxFolder:Folder where the message is placed
     */
    public void setFolder(MailboxFolder folder) {
        this.folder = folder;
    }

    /**
     * @return Boolean:Message has been read.
     */
    public Boolean getIsSeen() {
        return isSeen;
    }

    /**
     * @param isSeen Boolean:Message has been read.
     */
    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    /**
     * @return Boolean:Message has been answered.
     */
    public Boolean getIsAnswered() {
        return isAnswered;
    }

    /**
     * @param isAnswered Boolean:Message has been answered.
     */
    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    /**
     * @return Long:Date/time when message was received.
     */
    public Long getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate Long:Date/time when message was received.
     */
    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return Long:When message is moved to "Deleted" folder
     */
    public Long getDeleteAfterDate() {
        return deleteAfterDate;
    }

    /**
     * @param deleteAfterDate Long:When message is moved to "Deleted" folder
     */
    public void setDeleteAfterDate(Long deleteAfterDate) {
        this.deleteAfterDate = deleteAfterDate;
    }

    /**
     * @return MailboxFolder:Folder name where the message was placed before it was removed.
     */
    public MailboxFolder getDeletedFromFolder() {
        return deletedFromFolder;
    }

    /**
     * @param deletedFromFolder MailboxFolder:Folder name where the message was placed before it was removed.
     */
    public void setDeletedFromFolder(MailboxFolder deletedFromFolder) {
        this.deletedFromFolder = deletedFromFolder;
    }

}
