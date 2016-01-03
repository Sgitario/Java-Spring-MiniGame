package org.jcarvajal.webapp.services;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserServiceImplTest {
	private UserServiceImpl service;
	
	private UserRepository mockUserRepository;
	
	private AccessToken actualAccessToken;
	
	@Before
	public void setup() {
		mockUserRepository = Mockito.mock(UserRepository.class);
		
		service = new UserServiceImpl(mockUserRepository);
	}
	
	@Test
	public void getAccessToken_whenTokenNotExist_thenNull() {
		whenAccessToken("xxx1");
		thenAccessTokenIsNull();
	}
	
	@Test
	public void getAccessToken_whenTokenExpired_thenNull() {
		givenAccessTokenExpired("xxx1");
		whenAccessToken("xxx1");
		thenAccessTokenIsNull();
		thenAccessTokenIsDeleted();
	}
	
	private void givenAccessTokenExpired(String token) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -6);
		long createdAt = cal.getTime().getTime();
		
		AccessToken accessToken = Mockito.mock(AccessToken.class);
		Mockito.when(accessToken.getCreatedAt()).thenReturn(createdAt);
		Mockito.when(mockUserRepository.getAccessToken(token)).thenReturn(accessToken);
	}
	
	private void whenAccessToken(String token) {
		actualAccessToken = service.getAccessToken(token);
	}
	
	private void thenAccessTokenIsNull() {
		assertNull(actualAccessToken);
	}
	
	private void thenAccessTokenIsDeleted() {
		Mockito.verify(mockUserRepository, Mockito.times(1)).deleteAccessToken(Mockito.any(AccessToken.class));
	}
}
