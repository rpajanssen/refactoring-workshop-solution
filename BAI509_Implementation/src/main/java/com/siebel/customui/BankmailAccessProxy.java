package com.siebel.customui;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
/**
 * BankmailAccessProxy
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-11-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
public class BankmailAccessProxy{

    protected Descriptor descriptor;

    public class Descriptor {
        private com.siebel.customui.AABSpcBankmailSpcCalSpcAccessSpcInboundSpcWF service = null;
        private com.siebel.customui.BankmailAccess proxy = null;
        private Dispatch<Source> dispatch = null;

        /**
         * Constructor for Descriptor class
         */
        public Descriptor() {
            service = new com.siebel.customui.AABSpcBankmailSpcCalSpcAccessSpcInboundSpcWF();
            initCommon();
        }
        /**
         * Parameterized Constructor for the Descriptor class
         * @return Dispatch dispatch
         */
        public Descriptor(URL wsdlLocation, QName serviceName) {
            service = new com.siebel.customui.AABSpcBankmailSpcCalSpcAccessSpcInboundSpcWF(wsdlLocation, serviceName);
            initCommon();
        }
        private void initCommon() {
            proxy = service.getBankmailAccess();
        }
        public com.siebel.customui.BankmailAccess getProxy() {
            return proxy;
        }
        /**
         * Gets the depatched output
         * @return Dispatch dispatch
         */
        public Dispatch<Source> getDispatch() {
            if (dispatch == null ) {
                QName portQName = new QName("http://siebel.com/CustomUI", "BankmailAccess");
                dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl)) {
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);}
            }
            return dispatch;
        }
        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }
        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (dispatch != null ) {
                bp = (BindingProvider) dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }
    }
    /**
     * Constructor for BankmailAccessProxy class
     */
    public BankmailAccessProxy() {
        descriptor = new Descriptor();
    }
    /**
     * Parameterized Constructor for BankmailAccessProxy class
     */
    public BankmailAccessProxy(URL wsdlLocation, QName serviceName) {
        descriptor = new Descriptor(wsdlLocation, serviceName);
    }
    /**
     * Method to be called inside BankmailPlus1Output to get descriptor
     */
    public Descriptor getDescriptor() {
        return descriptor;
    }
    /**
     * Constructor for Descriptor class
     * @param bankmailPlus1Input BankmailPlus1Input
     * @return BankmailPlus1Output BankmailPlus1Output
     */
    public BankmailPlus1Output bankmailPlus1(BankmailPlus1Input bankmailPlus1Input) {
        return getDescriptor().getProxy().bankmailPlus1(bankmailPlus1Input);
    }

}