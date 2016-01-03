package org.jcarvajal.webapp.services;

import org.jcarvajal.webapp.model.AccessToken;

public interface UserService {

	/**
	 * Get access token by the token if it does exist and is not expired.
	 * @param token
	 * @return
	 */
	AccessToken getAccessToken(String token);

	AccessToken loginUser(String user, String pass);

	void deleteAccessToken(String token);
}
