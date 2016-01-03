package org.jcarvajal.webapp.filters.security;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;
import org.jcarvajal.webapp.services.UserService;
import org.jcarvajal.webapp.filters.Filter;
import org.jcarvajal.webapp.server.RequestContext;

/**
 * Read the access-token from the cookies 
 * and initialize the principal user in the request context.
 * 
 * @author JoseCH
 *
 */
public class AuthenticationProviderFilter implements Filter {
	
	public static final String TOKEN = "access-token";
	
	private final UserService userService;
	
	/**
	 * Initializes a new instance of the AuthenticationProviderFilter class.
	 * @param userService
	 */
	public AuthenticationProviderFilter(UserService userService) {
		this.userService = userService;
	}
	
	public void doFilter(RequestContext context) {
		AccessToken token = userService.getAccessToken(context.getCookie(TOKEN));
		if (token != null) {
			User user = token.getUser();
			
			context.setPrincipal(user.getUsername(), user.getRole());
		}
	}

}
