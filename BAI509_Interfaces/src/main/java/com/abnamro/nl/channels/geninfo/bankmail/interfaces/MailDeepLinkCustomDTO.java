package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.io.Serializable;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailDeepLinkCustomDTO
 *
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * Capgemini			  	19-03-2021	Initial version	  Release 1.0
 * Capgemini				19-03-2021	Initial version	  Mailbox Resource_2.5
 * </PRE>
 * @author
 * @see
 */
public class MailDeepLinkCustomDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * bcNumber
     */
    private String bcNumber;

    /**
     * contract Number
     */
    private String contractNumber;

    /**
     * document Id
     */
    private String documentId;

    /**
     * creation date
     */
    private String creationDate;

    /**
     * post box document type
     */
    private String PostboxDocumentType;

    /**
     * @return String:bcNumber
     */
    public String getBcNumber() {
        return bcNumber;
    }

    /**
     * @param bcNumber String:bc Number
     */
    public void setBcNumber(String bcNumber) {
        this.bcNumber = bcNumber;
    }

    /**
     * @return String:contractNumber
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * @param contractNumber String:contract Number
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @return String:documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId String:document Id
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * @return String:creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }
    
    /**
     * @param creationDate String:creation Date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return String:PostboxDocumentType
     */
    public String getPostboxDocumentType() {
        return PostboxDocumentType;
    }

    /**
     * @param postboxDocumentType String:postbox Document Type
     */
    public void setPostboxDocumentType(String postboxDocumentType) {
        PostboxDocumentType = postboxDocumentType;
    }


}
