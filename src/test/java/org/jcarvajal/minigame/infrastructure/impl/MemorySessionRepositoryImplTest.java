package org.jcarvajal.minigame.infrastructure.impl;

import static org.junit.Assert.*;

import java.util.UUID;

import org.jcarvajal.minigame.entities.Session;
import org.junit.Before;
import org.junit.Test;

public class MemorySessionRepositoryImplTest {
	private MemorySessionRepositoryImpl repository;
	
	private String sessionKey;
	private int userId;
	private Session actualSession;
	
	@Before
	public void setup() {
		this.repository = new MemorySessionRepositoryImpl();
	}
	
	/**
	 * When session does not exist.
	 * Then get session should return null.
	 */
	@Test
	public void getSession_whenSessionNotExist_thenReturnNull() {
		givenRandomSessionKey();
		whenGetSession();
		thenSessionIsNull();
	}
	
	/**
	 * When session is saved
	 * Then get session should return the saved session.
	 */
	@Test
	public void saveSession_whenSessionExist_thenReturnSession() {
		givenRandomSessionKey();
		givenUserId(1);
		whenSaveSession();
		whenGetSession();
		thenSessionIsExpected();
	}
	
	/**
	 * When session is saved
	 * Then get session by user id should return the saved session.
	 */
	@Test
	public void getSessionByUserId_whenSessionExist_thenReturnSession() {
		givenRandomSessionKey();
		givenUserId(1);
		whenSaveSession();
		whenGetSessionByUserId();
		thenSessionIsExpected();
	}
	
	/**
	 * When session does exist.
	 * When delete the session.
	 * Then get session should return null.
	 */
	@Test
	public void deleteSession_whenSessionExist_thenReturnNull() {
		givenRandomSessionKey();
		givenUserId(1);
		whenSaveSession();
		whenDeleteSession();
		whenGetSession();
		thenSessionIsNull();
	}
	
	private void givenUserId(int userId) {
		this.userId = userId;
	}
	
	private void givenRandomSessionKey() {
		this.sessionKey = UUID.randomUUID().toString();
	}
	
	private void whenGetSessionByUserId() {
		this.actualSession = this.repository.getSessionByUserId(userId);
	}
	
	private void whenSaveSession() {
		Session session = new Session();
		session.setSessionKey(sessionKey);
		session.setUserId(userId);
		this.repository.saveSession(session);
	}
	
	private void whenDeleteSession() {
		Session session = new Session();
		session.setSessionKey(sessionKey);
		session.setUserId(userId);
		this.repository.deleteSession(session);
	}
	
	private void whenGetSession() {
		this.actualSession = this.repository.getSession(sessionKey);
	}
	
	private void thenSessionIsNull() {
		assertNull(this.actualSession);
	}
	
	private void thenSessionIsExpected() {
		assertNotNull(this.actualSession);
		assertEquals(this.sessionKey, this.actualSession.getSessionKey());
		assertEquals(this.userId, this.actualSession.getUserId());
	}
}
