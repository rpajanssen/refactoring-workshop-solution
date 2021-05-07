package com.abnamro.nl.channels.geninfo.bankmail.client;

import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.configurationservice.ConfigurationService;
import com.abnamro.nl.configurationservice.ConfigurationServiceException;
import com.abnamro.nl.configurationservice.TechnicalConfigurationServiceFactory;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import org.xmlsoap.schemas.ws._2002._07.secext.Security;
import org.xmlsoap.schemas.ws._2002._07.secext.UsernameToken;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.List;

/**
 * BankmailAccessImplHelper <br/>
 *
 * <pre>
 *  &lt;b&gt;History:&lt;/b&gt;
 *
 *
 * </pre>
 * @author tcs
 */
@LogInterceptorBinding
public class BankmailAccessImplHelper {

	/** The Constant CLAZZ. used for logging */
	private static final Class<BankmailAccessImplHelper> CLASS = BankmailAccessImplHelper.class;

	/**
	 * Instantiate the Logger
	 */
    private static final LogHelper LOGGER = new LogHelper(CLASS);

	private static final ConfigurationService TECHNICAL_CONFIG_SRVC_FOR_URL = TechnicalConfigurationServiceFactory
		.createInstanceForApplication(BankmailConstants.MSEC_APP_NAME, CLASS.getName());

	public static final ConfigurationService TECHNICAL_CONFIGURATION_SERVICE = TechnicalConfigurationServiceFactory
		.createInstanceForGlobal(BankmailConstants.REMOTE_CRM_CONN);

	/**
	 * Sets the handler.
	 * @param bindingProvider the new handler
	 */
	public void setHandler(BindingProvider bindingProvider) {
		// Set the handler to set the authentication data in the soap header
		List<Handler> handlerChainList = bindingProvider.getBinding().getHandlerChain();
		handlerChainList.add(new CRMHeaderHandler(getSecurityContext()));
		bindingProvider.getBinding().setHandlerChain(handlerChainList);
	}

	/**
	 * Gets the Webservice URL from the NSB.
	 * @return the url
	 */
	public String getWSURL() {
		final String LOG_METHOD = "getWSURL()";

		String url = "";
		// Get the URL from the NSB
		try {
			url = TECHNICAL_CONFIG_SRVC_FOR_URL.getString(BankmailConstants.BANKMAIL_WEBSERVICE_URL);
			url = url.trim();
		} catch (ConfigurationServiceException e) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_URL_EXCEPTION, e, true);
		}

		// Check if the URL is empty
		if (isEmpty(url)) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_URL_EXCEPTION, null, true);
		}

		String parm = "";
		try {
			parm = TECHNICAL_CONFIG_SRVC_FOR_URL.getString(BankmailConstants.BANKMAIL_WEBSERVICE_PARM);
		} catch (ConfigurationServiceException e) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_URL_EXCEPTION, e, true);
            LOGGER.debugHardCodedMessage(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_PARAM);
		}

		if (!isEmpty(parm)) {
			parm = parm.trim();
			url += "?" + parm.replace(";", "&");
        } else {
            LOGGER.debugHardCodedMessage(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_PARAM);
		}

		return url;

	}

	/**
	 * Gets the security context.
	 * @return the security context
	 */
	private Security getSecurityContext() {
		final String LOG_METHOD = "getSecurityContext()";

		String user = "";

		try {
			user = TECHNICAL_CONFIGURATION_SERVICE.getString(BankmailConstants.BANKMAIL_WEBSERVICE_USER);
			user = user.trim();
		} catch (ConfigurationServiceException e) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_USER_EXCEPTION, e, true);
		}

		// Check if the usernm is empty
		if (isEmpty(user)) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_USER_EXCEPTION, null, true);
		}

		String pd = "";

		try {
			pd = TECHNICAL_CONFIGURATION_SERVICE.getString(BankmailConstants.BANKMAIL_WEBSERVICE_PD);
			pd = pd.trim();
		} catch (ConfigurationServiceException e) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_PD_EXCEPTION, e, true);
		}

		// Check if the pswrd is empty
		if (isEmpty(pd)) {
			handleInitializationError(LOG_METHOD, BankmailConstants.LOG_EMPTY_WEBSERVICE_PD_EXCEPTION, null, true);
		}
		return getSecurityContext(user, pd);

	}

	/**
	 * Gets the security context from user and pswrd.
	 * @param user the user
	 * @param pd the pswrd
	 * @return the security context
	 */
	private static Security getSecurityContext(String user, String pd) {
		Security security = new Security();
		UsernameToken usernameToken = new UsernameToken();
		usernameToken.setUsername(user);
		usernameToken.setPassword(pd);
		security.setUsernameToken(usernameToken);
		return security;
	}

	/**
	 * Handle initialization error.
	 * @param methodName the method name
	 * @param message the messages
     * @param e the exception
   	 * @param throwError the throw error
	 */
	private static void handleInitializationError(String methodName, String message, Exception e, boolean throwError) {
        LOGGER.error(methodName, message, e);

		if (throwError) {
			if (e == null) {
				throw new ExceptionInInitializerError(message);
			} else {
				throw new ExceptionInInitializerError(e);
			}
		}

	}

	/**
	 * Checks if String is empty.
	 * @param str the str
	 * @return true, if is empty
	 */
	private static boolean isEmpty(String str) {
		if (str != null) {
			str = str.trim();
			if (str.length() != 0) {
				return false;
			}
		}
		return true;
	}
}
