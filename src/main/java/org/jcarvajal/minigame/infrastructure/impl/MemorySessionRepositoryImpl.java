package org.jcarvajal.minigame.infrastructure.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jcarvajal.minigame.entities.Session;
import org.jcarvajal.minigame.infrastructure.SessionRepository;

/**
 * Implementation of SessionRepository.
 * This class is using a concurrent hash map to enable concurrent usage of the sessions
 * instead of using synchronized locks at method level. 
 * 
 * We're assuming that the getSession (by session key) is being more used than 
 * getSessionByUserId, so the key is session key (lookup O(1)). Therefore, the
 * getSessionByUserId will have a complecity of lookup O(n) worst case.
 * 
 * @author JoseCH
 *
 */
public class MemorySessionRepositoryImpl implements SessionRepository {
	
	private Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	public Session getSession(String sessionKey) {
		return sessions.get(sessionKey);
	}
	
	public Collection<Session> getSessions() {
		return sessions.values();
	}
	
	public Session getSessionByUserId(int userId) {
		Session found = null;
		for (Session session : getSessions()) {
			if (session.getUserId() == userId) {
				found = session;
				break;
			}
		}
		
		return found;
	}
	
	public void saveSession(Session session) {
		if (session != null) {
			sessions.put(session.getSessionKey(), session);
		}
	}
	
	public void deleteSession(Session session) {
		if (session != null) {
			sessions.remove(session.getSessionKey());
		}
	}
}
