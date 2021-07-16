package com.abnamro.nl.channels.geninfo.bankmail.resources;


import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * Policy: A bank defined policy applicable to a business contact.
 */
 public abstract class Policy extends AbstractDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Business Contact Contains the properties of a BusinessContact
     */
    private transient BusinessContact customer;

    /**
     * @return BusinessContact which holds the properties of a BusinessContact.
     */
    public BusinessContact getCustomer() {
        return customer;
    }

    /**
     * @param customer set BusinessContact
     */
    public void setCustomer(BusinessContact customer) {
        this.customer = customer;
    }
}