package org.jcarvajal.minigame.infrastructure;

import java.util.Collection;

import org.jcarvajal.minigame.infrastructure.entities.Session;

/**
 * Session repository.
 * @author JoseCH
 *
 */
public interface SessionRepository {

	/**
	 * @return get all sessions in the repository.
	 */
	Collection<Session> getSessions();
	
	/**
	 * Get the session linked to the specified session key.
	 * If no session is found, then return null.
	 * @param sessionKey
	 * @return
	 */
	Session getSession(String sessionKey);
	
	/**
	 * Get the session linked to the specified user id.
	 * If no session is found, then return null.
	 * @param userId
	 * @return
	 */
	Session getSessionByUserId(int userId);
	
	/**
	 * Save a session into the repository.
	 * @param session
	 */
	void saveSession(Session session);
	
	/**
	 * Delete a session into the repository.
	 * @param session
	 */
	void deleteSession(Session session);

}
