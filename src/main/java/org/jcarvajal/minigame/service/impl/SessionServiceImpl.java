package org.jcarvajal.minigame.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.minigame.entities.Session;
import org.jcarvajal.minigame.exceptions.UserNotFoundException;
import org.jcarvajal.minigame.infrastructure.SessionRepository;
import org.jcarvajal.minigame.service.SessionService;

/**
 * Session service implementation.
 * 
 * @author JoseCH
 *
 */
public class SessionServiceImpl implements SessionService {
	@Autowired
	private SessionRepository sessionRepository;
	
	private int sessionExpiredMinutes;
	
	public void setSessionRepository(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	public void setSessionExpiredMinutes(String sessionExpiredMinutes) {
		this.sessionExpiredMinutes = Integer.valueOf(sessionExpiredMinutes);
	}
	
	public String getSessionKey(int userId) {
		Session session = sessionRepository.getSessionByUserId(userId);
		if (session == null || isSessionExpired(session)) {
			// If session not found or it has expired, recreate it.
			session = new Session();
			session.setUserId(userId);
			session.setSessionKey(renewSessionKey());
			session.setCreatedAt(new Date().getTime());
			
			sessionRepository.saveSession(session);
		}
		
		return session.getSessionKey();
	}
	
	public boolean containsSessionKey(String sessionKey) {
		Session session = sessionRepository.getSession(sessionKey);
		return session != null;
	}
	
	public int getUserIdBySessionKey(String sessionKey) 
			throws UserNotFoundException {
		Session session = sessionRepository.getSession(sessionKey);
		if (session == null) {
			throw new UserNotFoundException("Session key %s cannot be found. ", sessionKey);
		}
		
		return session.getUserId();
	}
	
	public int removeExpiredSessions() {
		int numDeletes = 0;
		List<Session> sessions = new ArrayList<Session>(sessionRepository.getSessions());
		for (Session session : sessions) {
			if (isSessionExpired(session)) {
				sessionRepository.deleteSession(session);
				numDeletes++;
			}
		}
		
		return numDeletes;
	}
	
	private String renewSessionKey() {
		return UUID.randomUUID().toString();
	}
	
	private boolean isSessionExpired(Session session) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, - sessionExpiredMinutes);
		long expiredSession = cal.getTime().getTime();
		return session != null && session.getCreatedAt() <= expiredSession; 
	}
}
