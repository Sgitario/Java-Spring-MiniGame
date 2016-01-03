package org.jcarvajal.webapp.services;

import java.util.Calendar;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;
import org.jcarvajal.webapp.repositories.UserRepository;
import org.jcarvajal.webapp.utils.StringUtils;

public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private int minutesToExpire = 5;
	
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}
	
	public void setMinutesToExpire(int minutesToExpire) {
		this.minutesToExpire = minutesToExpire;
	}
	
	public AccessToken getAccessToken(String token) {
		AccessToken found = null;
		if (StringUtils.isNotEmpty(token)) {
			AccessToken accessToken = repository.getAccessToken(token);
			if (accessToken != null && !isExpired(accessToken.getCreatedAt())) {
				found = accessToken;
			} else if (accessToken != null) {
				// delete expired token
				repository.deleteAccessToken(accessToken);
			}
		}
		
		return found;
	}
	
	public void deleteAccessToken(String token) {
		AccessToken accessToken = repository.getAccessToken(token);
		if (accessToken != null) {
			repository.deleteAccessToken(accessToken);
		}
	}
	
	public AccessToken loginUser(String username, String pass) {
		AccessToken accessToken = null;
		User user = repository.getUser(username, pass);
		if (user != null) {
			accessToken = new AccessToken(user);
			repository.addAccessToken(accessToken);
		}
		
		return accessToken;
	}

	private boolean isExpired(long createdAt) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, - minutesToExpire);
		long expiredSession = cal.getTime().getTime();
		return createdAt <= expiredSession; 
	}
}
