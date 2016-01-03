package org.jcarvajal.webapp.repositories;

import static org.junit.Assert.*;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserRepositoryImplTest {
	private UserRepositoryImpl repository;
	
	private AccessToken actualAccessToken;
	private User actualUser;
	
	@Before
	public void setup() {
		repository = new UserRepositoryImpl();
	}
	
	@Test
	public void getAccessToken_thenTokenNull() {
		whenGetAccessToken(null);
		thenAccessTokenIsNull();
	}
	
	@Test
	public void addAccessToken_thenTokenNotNull() {
		whenAddAccessToken("xxx1");
		whenGetAccessToken("xxx1");
		thenAccessTokenIs("xxx1");
	}
	
	@Test
	public void deleteAccessToken_thenTokenNull() {
		whenAddAccessToken("xxx1");
		whenDeleteAccessToken("xxx1");
		whenGetAccessToken("xxx1");
		thenAccessTokenIsNull();
	}
	
	@Test
	public void getUser_thenUserNull() {
		whenGetUser("user1", "pass1");
		thenUserIsNull();
	}
	
	@Test
	public void getUser_thenUserNotNull() {
		whenAddUser("user1", "pass1", "role1");
		whenGetUser("user1", "pass1");
		thenUserNotNull();
	}
	
	private void whenAddUser(String username, String pass, String role) {
		repository.addUser(username, pass, role);
	}
	
	private void whenGetUser(String username, String pass) {
		actualUser = repository.getUser(username, pass);
	}
	
	private void whenAddAccessToken(String token) {
		AccessToken mockAccessToken = Mockito.mock(AccessToken.class);
		Mockito.when(mockAccessToken.getToken()).thenReturn(token);
		repository.addAccessToken(mockAccessToken);
	}
	
	private void whenDeleteAccessToken(String token) {
		AccessToken mockAccessToken = Mockito.mock(AccessToken.class);
		Mockito.when(mockAccessToken.getToken()).thenReturn(token);
		repository.deleteAccessToken(mockAccessToken);
	}
	
	private void whenGetAccessToken(String token) {
		actualAccessToken = repository.getAccessToken(token);
	}
	
	private void thenAccessTokenIsNull() {
		assertNull(actualAccessToken);
	}
	
	private void thenAccessTokenIs(String expectedToken) {
		assertEquals(expectedToken, actualAccessToken.getToken());
	}
	
	private void thenUserIsNull() {
		assertNull(actualUser);
	}
	
	private void thenUserNotNull() {
		assertNotNull(actualUser);
	}
}
