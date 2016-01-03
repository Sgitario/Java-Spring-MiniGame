package org.jcarvajal.webapp.repositories;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;

public interface UserRepository {

	AccessToken getAccessToken(String token);
	void addAccessToken(AccessToken accessToken);
	void deleteAccessToken(AccessToken accessToken);

	User getUser(String username, String pass);
	void addUser(String username, String pass, String role);
	
}
