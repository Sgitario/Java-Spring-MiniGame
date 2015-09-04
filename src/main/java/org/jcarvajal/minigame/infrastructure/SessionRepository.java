package org.jcarvajal.minigame.infrastructure;

import java.util.Collection;

import org.jcarvajal.minigame.entities.Session;

public interface SessionRepository {

	Collection<Session> getSessions();
	Session getSession(String sessionKey);
	Session getSessionByUserId(int userId);
	void saveSession(Session session);
	void deleteSession(Session session);

}
