package org.jcarvajal.minigame.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jcarvajal.minigame.exceptions.UserNotFoundException;
import org.jcarvajal.minigame.infrastructure.SessionRepository;
import org.jcarvajal.minigame.infrastructure.entities.Session;
import org.junit.Before;
import org.junit.Test;

public class SessionServiceImplTest {
	private static int USER_ID = 1;
	private static String SESSION_EXPIRED_KEY = UUID.randomUUID().toString();
	private static String SESSION_NOT_EXPIRED_KEY = UUID.randomUUID().toString();
	
	private SessionRepository mockRepository;
	
	private SessionServiceImpl service;
	
	private List<Session> expectedSessions;
	private String expectedSessionKey;
	private String actualSessionKey;
	private boolean actualContains;
	private int actualUserId;
	private int actualExpiredSessionDeletes;
	
	@Before
	public void setup() {
		expectedSessions = new ArrayList<Session>();
		
		mockRepository = mock(SessionRepository.class);
		when(mockRepository.getSessions()).thenReturn(expectedSessions);
		
		service = new SessionServiceImpl();
		service.setSessionRepository(mockRepository);
		service.setSessionExpiredSeconds("15000");
	}
	
	@Test
	public void getSessionKey_whenNotExisting_ReturnNewSession() {
		whenGetSessionKey();
		thenSessionKeyNotNull();
	}
	
	@Test
	public void getSessionKey_whenSessionExpired_ReturnNewSession() {
		givenSessionExpired();
		whenGetSessionKey();
		thenSessionKeyIsNotExpired();
	}
	
	@Test
	public void getSessionKey_whenSessionNotExpired_ReturnExistingSession() {
		givenSessionNotExpired();
		whenGetSessionKey();
		thenSessionKeyIsTheExisting();
	}
	
	@Test
	public void containsSessionKey_whenSessionExists_ReturnTrue() {
		givenSessionNotExpired();
		whenContainsSessionKey();
		thenContainsSessionReturnsTrue();
	}
	
	@Test
	public void containsSessionKey_whenSessionExpired_ReturnFalse() {
		givenSessionExpired();
		whenContainsSessionKey();
		thenContainsSessionReturnsFalse();
	}
	
	@Test
	public void containsSessionKey_whenSessionDoesNotExist_ReturnFalse() {
		whenContainsSessionKey();
		thenContainsSessionReturnsFalse();
	}
	
	@Test(expected = UserNotFoundException.class)
	public void getUserIdBySessionKey_whenSessionDoesNotExist_ReturnException() 
			throws UserNotFoundException {
		whenGetUserIdBySessionKey();
	}
	
	@Test(expected = UserNotFoundException.class)
	public void getUserIdBySessionKey_whenSessionExpired_ReturnException() 
			throws UserNotFoundException {
		givenSessionExpired();
		whenGetUserIdBySessionKey();
	}
	
	@Test
	public void getUserIdBySessionKey_whenSessionDoesExist_ReturnUserId() 
			throws UserNotFoundException {
		givenSessionNotExpired();
		whenGetUserIdBySessionKey();
		thenUserIdExpected();
	}
	
	@Test
	public void removeExpiredSessions_whenNoSessions_ReturnZero() 
			throws UserNotFoundException {
		whenRemoveExpiredSessions();
		thenExpiredSessionsDeleted(0);
	}
	
	@Test
	public void removeExpiredSessions_whenNoExpiredSession_ReturnZero() 
			throws UserNotFoundException {
		givenSessionNotExpired();
		whenRemoveExpiredSessions();
		thenExpiredSessionsDeleted(0);
	}
	
	@Test
	public void removeExpiredSessions_whenExpiredSession_ReturnOne() 
			throws UserNotFoundException {
		givenSessionExpired();
		whenRemoveExpiredSessions();
		thenExpiredSessionsDeleted(1);
		thenDeleteSessionIsCalled(1);
	}
	
	private void givenSessionNotExpired() {
		Session session = new Session();
		session.setCreatedAt(new Date().getTime());
		session.setSessionKey(SESSION_NOT_EXPIRED_KEY);
		session.setUserId(USER_ID);
		givenSession(session);
	}
	
	private void givenSessionExpired() {
		service.setSessionExpiredSeconds("1");
		
		Session expired = new Session();
		expired.setCreatedAt(new Date().getTime());
		expired.setSessionKey(SESSION_EXPIRED_KEY);
		expired.setUserId(USER_ID);
		givenSession(expired);

		// Wait two seconds to properly expire the session.
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

		}
	}
	
	private void givenSession(Session session) {
		expectedSessionKey = session.getSessionKey();
		when(mockRepository.getSessionByUserId(session.getUserId())).thenReturn(session);
		when(mockRepository.getSession(session.getSessionKey())).thenReturn(session);
		
		expectedSessions.add(session);
	}
	
	private void whenContainsSessionKey() {
		this.actualContains = service.containsSessionKey(expectedSessionKey);
	}
	
	private void whenGetUserIdBySessionKey() throws UserNotFoundException {
		this.actualUserId = service.getUserIdBySessionKey(expectedSessionKey);
	}
	
	private void whenGetSessionKey() {
		this.actualSessionKey = service.getSessionKey(USER_ID);
	}
	
	private void whenRemoveExpiredSessions() {
		this.actualExpiredSessionDeletes = service.removeExpiredSessions();
	}
	
	private void thenSessionKeyNotNull() {
		assertNotNull(this.actualSessionKey);
	}
	
	private void thenSessionKeyIsNotExpired() {
		thenSessionKeyNotNull();
		assertFalse(SESSION_EXPIRED_KEY.equals(actualSessionKey));
	}
	
	private void thenSessionKeyIsTheExisting() {
		thenSessionKeyNotNull();
		assertTrue(SESSION_NOT_EXPIRED_KEY.equals(actualSessionKey));
	}
	
	private void thenContainsSessionReturnsTrue() {
		assertTrue(actualContains);
	}
	
	private void thenContainsSessionReturnsFalse() {
		assertFalse(actualContains);
	}
	
	private void thenUserIdExpected() {
		assertEquals(USER_ID, actualUserId);
	}
	
	private void thenExpiredSessionsDeleted(int expected) {
		assertEquals(expected, actualExpiredSessionDeletes);
	}
	
	private void thenDeleteSessionIsCalled(int times) {
		verify(mockRepository, times(times)).deleteSession(any(Session.class));
	}
}
