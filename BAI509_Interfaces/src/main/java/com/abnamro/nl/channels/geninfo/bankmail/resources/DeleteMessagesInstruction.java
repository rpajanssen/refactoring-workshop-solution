package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DeleteMessagesInstruction Stores message instructions which have been deleted
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
public class DeleteMessagesInstruction extends AbstractDTO {
    private static final long serialVersionUID = 1L;
    /**
     * References to the messages to be deleted.
     */
    private List<String> messageIds;

    /**
     * Source folder name.
     */
    private MailboxFolderName fromFolderName;

    /**
     * @return List<String> of references to the messages to be deleted.
     */
    public List<String> getMessageIds() {
        return messageIds;
    }

    /**
     * @param messageIds :References to the messages to be deleted.
     */
    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }

    /**
     * @return Source folder name.
     */
    public MailboxFolderName getFromFolderName() {
        return fromFolderName;
    }

    /**
     * @param fromFolderName :Source folder name.
     */
    public void setFromFolderName(MailboxFolderName fromFolderName) {
        this.fromFolderName = fromFolderName;
    }
}
