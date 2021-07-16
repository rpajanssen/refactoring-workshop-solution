package com.abnamro.nl.channels.restservices.application;

import java.util.Set;
import javax.ws.rs.ApplicationPath;

import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextProvider;
import com.abnamro.nl.channels.rest.application.AabRestApplication;

import com.abnamro.nl.channels.restservices.BankmailPolicyRestService;


/**
 * BankmailPolicyApplication 
 * Rest application class which registers classes 
 * which implements Rest service.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				18-09-2012	Initial version	  Release 1.0
 * Mitesh Nakrani	17 August 2015	Changed According to New RestUtils standard
 * </PRE>
 * @author 
 * @see
 */
@ApplicationPath("/*")
public class BankmailPolicyApplication extends AabRestApplication{
	
	/**
	 * Get REST classes
	 * @return Set<Class<?>>
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes=super.getClasses();
		classes.add(SecurityContextProvider.class);
		classes.add(BankmailPolicyRestService.class);
		return classes;
	}
}
