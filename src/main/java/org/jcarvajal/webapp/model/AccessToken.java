package org.jcarvajal.webapp.model;

import java.util.Date;
import java.util.UUID;

public class AccessToken {
	private final String token = UUID.randomUUID().toString();
	private final User user;
	private final long createdAt;
	
	public AccessToken(User user) {
		this.user = user;
		this.createdAt = new Date().getTime();
	}
	
	public String getToken() {
		return token;
	}
	
	public User getUser() {
		return user;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
}
