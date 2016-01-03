package org.jcarvajal.webapp.controllers;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;
import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.services.UserService;
import org.jcarvajal.webapp.servlet.impl.ViewAndModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LoginControllerTest {
	private LoginController controller;
	
	private UserService mockUserService;
	private RequestContext mockRequestContext;
	
	private String expectedUser;
	private String expectedPass;
	private ViewAndModel actual;
	
	@Before
	public void setup() {		
		mockUserService = Mockito.mock(UserService.class);
		mockRequestContext = Mockito.mock(RequestContext.class);
		
		controller = new LoginController(mockUserService);
	}
	
	@Test
	public void getLoginPage_whenUserNotLogIn_thenLoginPage() {
		whenGetLoginPage();
		thenViewName("loginPage");
	}
	
	@Test
	public void getLoginPage_whenUserLogIn_thenLoginPage() {
		givenUserLogIn();
		whenGetLoginPage();
		thenViewName("index");
	}
	
	@Test
	public void postLogin_whenUserNotFound_thenUnAuthorized() {
		givenRequestBody("name", "val1");
		givenUserNotFound();
		whenPostLoginPage();
		thenViewName("unauthorized");
		thenCode(401);
	}
	
	@Test
	public void postLogin_whenUserFound_thenIndexPage() {
		givenRequestBody("name", "val1");
		givenUserFound();
		whenPostLoginPage();
		thenViewName("index");
	}
	
	@Test
	public void logout_thenTokenDeleted() {
		whenLogoutPage();
		thenViewName("loginPage");
		thenTokenDeleted();
	}
	
	private void givenUserLogIn() {
		Mockito.when(mockRequestContext.getPrincipal()).thenReturn("username");
	}
	
	private void givenRequestBody(String user, String pass) {
		expectedUser = user;
		expectedPass = pass;
		String body = String.format("user=%s&pass=%s", user, pass);
		
		Mockito.when(mockRequestContext.getRequestBody())
			.thenReturn(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
		
	}
	
	private void givenUserNotFound() {
		Mockito.when(mockUserService.loginUser(expectedUser, expectedPass)).thenReturn(null);
	}
	
	private void givenUserFound() {
		
		Mockito.when(mockUserService.loginUser(expectedUser, expectedPass)).thenReturn(new AccessToken(new User()));
	}
	
	private void whenGetLoginPage() {
		actual = controller.getLoginPage(mockRequestContext);
	}
	
	private void whenPostLoginPage() {
		actual = controller.postLogin(mockRequestContext);
	}
	
	private void whenLogoutPage() {
		actual = controller.logout(mockRequestContext);
	}
	
	private void thenViewName(String expected) {
		assertEquals(expected, actual.getViewName());
	}
	
	private void thenCode(int code) {
		assertEquals(code, actual.getCode());
	}
	
	private void thenTokenDeleted() {
		Mockito.verify(mockUserService, Mockito.times(1)).deleteAccessToken(Mockito.anyString());
	}
}
