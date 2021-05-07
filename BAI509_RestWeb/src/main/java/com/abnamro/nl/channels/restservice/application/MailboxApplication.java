package com.abnamro.nl.channels.restservice.application;

import java.util.Set;
import javax.ws.rs.ApplicationPath;

import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextProvider;
import com.abnamro.nl.channels.rest.application.AabRestApplication;
import com.abnamro.nl.channels.restservice.MailboxRestService;

/**
 * MailboxApplication 
 * Rest application class which registers classes 
 * which implements Rest service.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-08-2012	Initial version	  Release 1.0
 * Mitesh Nakrani	17 August 2015	Changed According to New RestUtils standard
 * </PRE>
 * @author 
 * @see
 */
@ApplicationPath("/*")
public class MailboxApplication extends AabRestApplication{
	
	/**
	 * Get REST classes
	 * @return Set<Class<?>>
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes=super.getClasses();
		classes.add(SecurityContextProvider.class);
		classes.add(MailboxRestService.class);
		return classes;
	}
}
