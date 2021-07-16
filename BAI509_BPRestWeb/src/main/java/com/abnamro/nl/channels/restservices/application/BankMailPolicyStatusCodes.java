package com.abnamro.nl.channels.restservices.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * BankmailPolicyStatusCodes: Retrieves the BankmailPolicy resources
 * applicable for the logged in user codes. The logged in user is allowed to send
 * bankmails concerning customers whom he/she is authorized for.
 *
 * <PRE>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	18-09-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
@Qualifier
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BankMailPolicyStatusCodes {

}