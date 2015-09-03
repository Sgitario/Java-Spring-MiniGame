package org.jcarvajal.framework.rest.servlet.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.jcarvajal.minigame.application.UserController;
import org.junit.Before;
import org.junit.Test;

public class AnnotationControllerManagerTest {
	
	private static final int USER_ID = 1265;
	
	private AnnotationControllerManager manager;
	
	private String expectedResponse;
	private Object actualResponse;
	
	private UserController mockController;
	
	@Before
	public void setup() {
		manager = new AnnotationControllerManager();
	}
	
	@Test
	public void register_whenControllerIsRegistered_thenUrlIsMatched() {
		givenRegisterUserController();
		givenExpectedResponseForLogin();
		whenUrlIsForLogin();
		thenResponsesMatch();
	}
	
	private void givenRegisterUserController() {
		mockController = mock(UserController.class);
		manager.register(mockController);
	}
	
	private void givenExpectedResponseForLogin() {
		expectedResponse = UUID.randomUUID().toString();
		when(mockController.login(USER_ID)).thenReturn(expectedResponse);
	}
	
	private void whenUrlIsForLogin() {
		actualResponse = manager.handle("http://localhost:8080/" + USER_ID + "/login", RequestMethod.GET);
	}
	
	private void thenResponsesMatch() {
		assertEquals(expectedResponse, actualResponse);
	}
}
