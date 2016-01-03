package org.jcarvajal.webapp.filters;

import org.jcarvajal.webapp.server.RequestContext;

/**
 * Filter are invoked before handling a new request using reactive programming.
 * @author JoseCH
 *
 */
public interface Filter {
	public void doFilter(RequestContext context);
}
