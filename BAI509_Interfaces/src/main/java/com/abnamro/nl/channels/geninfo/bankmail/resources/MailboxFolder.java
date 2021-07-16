package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * MailboxFolder Stores properties for a mailbox folder
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
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MailboxFolder extends AbstractDTO {
    private static final long serialVersionUID = 1L;
    /**
     * Name of the folder. Name is unique within a mailbox.
     */
    private MailboxFolderName name;

    /**
     * Number of unseen messages in the folder.
     */
    private Integer unseenMessagesCount;

    /**
     * Number of unseen alert messages in the folder
     */
    private Integer unseenAlertMessagesCount;

    /**
     * @return Name of the folder
     */
    public MailboxFolderName getName() {
        return name;
    }

    /**
     * @param name set the name of the folder
     */
    public void setName(MailboxFolderName name) {
        this.name = name;
    }

    /**
     * @return no of unseen messages in the folder.
     */
    public Integer getUnseenMessagesCount() {
        return unseenMessagesCount;
    }

    /**
     * @param unseenMessagesCount :Number of unseen messages in the folder.
     */
    public void setUnseenMessagesCount(Integer unseenMessagesCount) {
        this.unseenMessagesCount = unseenMessagesCount;
    }

    /**
     * @return unseenAlertMessagesCount
     */
    public Integer getUnseenAlertMessagesCount() {
        return unseenAlertMessagesCount;
    }

    /**
     * @param unseenAlertMessagesCount Integer
     */
    public void setUnseenAlertMessagesCount(Integer unseenAlertMessagesCount) {
        this.unseenAlertMessagesCount = unseenAlertMessagesCount;
    }

}
