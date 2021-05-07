package com.abnamro.nl.channels.geninfo.bankmail.client;

import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.siebel.customui.BankmailAccess;
import com.siebel.customui.BankmailAccessProxy;
import com.siebel.customui.BankmailPlus1Input;
import com.siebel.customui.BankmailPlus1Output;

import javax.xml.ws.BindingProvider;

/**
 * BankmailAccessImpl <br/>
 *
 * <pre>
 *  &lt;b&gt;History:&lt;/b&gt;
 *
 *
 * </pre>
 * @author tcs
 */
@LogInterceptorBinding
public class BankmailAccessImpl implements BankmailAccess {

	private BankmailAccess service = null;

	/**
	 * default constructor
	 */
	public BankmailAccessImpl() {
		init();
	}

	/**
	 * Retrieves the web service url and initializes the service .
	 */
	private void init() {
		BankmailAccessImplHelper bankmailAccessImplHelper = new BankmailAccessImplHelper();

		BankmailAccessProxy proxy = new BankmailAccessProxy();
		proxy.getDescriptor().setEndpoint(bankmailAccessImplHelper.getWSURL());
		service = proxy.getDescriptor().getProxy();
		bankmailAccessImplHelper.setHandler((BindingProvider) service);

	}

	/**
	 * BankmailPlus1
	 * @param bankmailPlus1Input bankmailPlus1Input
	 * @return BankmailPlus1Output
	 */
	public BankmailPlus1Output bankmailPlus1(BankmailPlus1Input bankmailPlus1Input) {
		return service.bankmailPlus1(bankmailPlus1Input);
	}

}
