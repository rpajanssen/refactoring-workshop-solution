package com.abnamro.nl.channels.geninfo.bankmail.abpc.factory;

import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.factory.AabBaseFactory;
import com.abnamro.nl.health.interfaces.HealthCheckable;

/**
 * This is the Smart Proxy that supplies access to a component/service through the component
 * HomepageABPC interface definition<BR>
 * <PRE>
 * <B>History:</B>
 * Developer         Date       Change Reason	  Change
 * ----------------- ---------- ----------------- ----------------------------------------------
 * Nico v.d. Vijver  29-05-2012 Initial version	  Release 1.0
 * </PRE>
 * @author TCS 
 */

public final class BankmailABPCFactory extends AabBaseFactory {
    /**
     * The implementation class 
     */
    private static final Class IMPLEMENTATION_CLASS =
        getImplementationClass(
        		BankmailABPCFactory.class,
            new Class[] { BankmailABPC.class, HealthCheckable.class });

    /**
     * Private default no-arg constructor
     */
    private BankmailABPCFactory() {
    }
        
    /**
     * @return BankmailABPC bankmailABPC
     * @see com.abnamro.nl.factory.AabBaseFactory#newInstance(Class)
     */
    public static BankmailABPC newInstance() {
        return (BankmailABPC) newInstance(IMPLEMENTATION_CLASS);
    }
    
    /**
     * @return HealthCheckable healthCheckable
     * @see com.abnamro.nl.factory.AabBaseFactory#newInstance(Class)
     */
    public static HealthCheckable newHealthCheckable() {
        return (HealthCheckable) newInstance(IMPLEMENTATION_CLASS);
    }
}
