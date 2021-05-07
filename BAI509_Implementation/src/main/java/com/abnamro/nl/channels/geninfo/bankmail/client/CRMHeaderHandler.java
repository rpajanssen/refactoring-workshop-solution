package com.abnamro.nl.channels.geninfo.bankmail.client;

import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import org.xmlsoap.schemas.ws._2002._07.secext.Security;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;

/**
 * CRMHeaderHandler <br/>
 *
 * <pre>
 *  &lt;b&gt;History:&lt;/b&gt;
 *
 * </pre>
 * @author tcs
 */
@LogInterceptorBinding
public class CRMHeaderHandler implements SOAPHandler<SOAPMessageContext> {

	/** The Constant CLAZZ. used for logging */
	private static final Class<CRMHeaderHandler> CLASS = CRMHeaderHandler.class;

	/**
	 * Instantiate the Logger
	 */
    private static final LogHelper LOGGER = new LogHelper(CLASS);

	/** The Constant SECURITY_PREFIX. */
	private static final String SECURITY_PREFIX = "wsse";

	/** The Constant SECURITY_URI. */
	private static final String SECURITY_URI = "http://schemas.xmlsoap.org/ws/2002/07/secext";

	/** The security header. */
	private Security securityHeader;

	/**
	 * Instantiates a new CRM header handler.
	 * @param securityHeader the security header
	 */
	public CRMHeaderHandler(Security securityHeader) {
		this.securityHeader = securityHeader;
	}

	/**
	 * Gets the headers.
	 * @return the headers
	 */
    @Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	/**
	 * Close.
	 * @param context the context
	 */
    @Override
	public void close(MessageContext context) {
		//this method overrides the close method.
	}

	/**
	 * Handle fault.
	 * @param context the context
	 * @return true, if successful
	 */
    @Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	/**
	 * Handle message.
	 * @param context the context
	 * @return true, if successful
	 */
    @Override
	public boolean handleMessage(SOAPMessageContext context) {
		final String LOG_METHOD = "handleMessage(SOAPMessageContext context)";

		boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		// if this is a request, true for outbound messages, false for inbound
		if (isRequest) {
			SOAPMessage soapMsg = context.getMessage();
			SOAPEnvelope soapEnv;
			try {
				soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();
				// if no header, add one
				if (soapHeader == null) {
					soapHeader = soapEnv.addHeader();
				}
				SOAPElement security = soapHeader.addHeaderElement(soapEnv.createName("Security", SECURITY_PREFIX,
					SECURITY_URI));
				SOAPElement usernameToken = security.addChildElement(soapEnv.createName("UsernameToken", SECURITY_PREFIX,
					SECURITY_URI));
				SOAPElement username = usernameToken.addChildElement(soapEnv.createName("Username", SECURITY_PREFIX,
					SECURITY_URI));
				username.setValue(securityHeader.getUsernameToken().getUsername());
				SOAPElement password = usernameToken.addChildElement(soapEnv.createName("Password", SECURITY_PREFIX,
					SECURITY_URI));
				password.addAttribute(soapEnv.createName("Type"), SECURITY_PREFIX + ":PassText");
				password.setValue(securityHeader.getUsernameToken().getPassword());
				soapMsg.saveChanges();
            } catch (SOAPException e) {
                LOGGER.error(LOG_METHOD, BankmailConstants.LOG_WEBSERVICE_HEADER_EXCEPTION, e);
				throw new IllegalArgumentException(e);
			}
		}
		return true;
	}

}
