package com.abnamro.nl.channels.restservice.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

	import javax.inject.Qualifier;
/**
 * MailContactStatusCodes Interface for mailcontactStatus codes
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
	@Qualifier
	@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MailContactStatusCodes {

	}
