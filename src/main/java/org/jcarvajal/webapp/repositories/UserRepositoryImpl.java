package org.jcarvajal.webapp.repositories;

import java.util.HashMap;
import java.util.Map;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;

public class UserRepositoryImpl implements UserRepository {

	private Map<String, AccessToken> accessTokens = new HashMap<String, AccessToken>();
	private Map<String, User> users = new HashMap<String, User>();
	
	public AccessToken getAccessToken(String token) {
		return accessTokens.get(token);
	}
	
	public void addAccessToken(AccessToken accessToken) {
		accessTokens.put(accessToken.getToken(), accessToken);
	}
	
	public void deleteAccessToken(AccessToken accessToken) {
		accessTokens.remove(accessToken.getToken());
	}
	
	public void addUser(String username, String pass, String role) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(pass);
		user.setRole(role);
		
		users.put(username, user);
	}
	
	public User getUser(String username, String pass) {
		User found = null;
		User user = users.get(username);
		if (user != null && user.getPassword().equals(pass)) {
			found = user;
		}
		
		return found;
	}

}
