package com.abnamro.nl.channels.restservice.health;

import com.abnamro.nl.health.AbstractHealthCheckServlet;
import com.abnamro.nl.health.exceptions.HealthCheckException;

/**
 * Servlet called by the load balancer to see whether connections to
 * DB2 and CBUS-IMS are healthy.
 * <PRE>
 * <B>History:</B>
 * Developer         Date       Change Reason	  Change
 * ----------------- ---------- ----------------- ----------------------------------------------
 * TCS       24-08-2012 Initial version	  Release 1.0
 * </PRE>
 * @author TCS
 * @see
 */
public class HealthCheckServlet  extends AbstractHealthCheckServlet {
	/** */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.abnamro.nl.health.AbstractHealthCheckServlet#isHealthy()
	 */
	protected boolean isHealthy() throws HealthCheckException {
		return true;
	}


}



