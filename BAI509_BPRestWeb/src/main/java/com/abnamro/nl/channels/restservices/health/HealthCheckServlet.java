package com.abnamro.nl.channels.restservices.health;

import com.abnamro.nl.health.AbstractHealthCheckServlet;
import com.abnamro.nl.health.exceptions.HealthCheckException;


/**
 * Servlet called by the load balancer to see whether connections to
 */
public class HealthCheckServlet extends AbstractHealthCheckServlet {

	/** */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.abnamro.nl.health.AbstractHealthCheckServlet#isHealthy()
	 */
	protected boolean isHealthy() throws HealthCheckException {
		return true;
	}
}
