package org.jcarvajal.minigame.service;

import org.jcarvajal.minigame.exceptions.UserNotFoundException;

/**
 * Session services with operations for:
 * - retrieve the session key for user id.
 * - check whether the user has a valid session id.
 * - get the user id by session key.
 * 
 * @author JoseCH
 *
 */
public interface SessionService {

	/**
	 * Get a session key for the specified user id.
	 * In case of the session key does not exist, a new session key will be generated.
	 * 
	 * @param userId
	 * @return
	 */
	String getSessionKey(int userId);
	
	/**
	 * Check whether the specified session key is registered.
	 * @param sessionKey
	 * @return
	 */
	boolean containsSessionKey(String sessionKey);
	
	/**
	 * Get the user id by an existing session key.
	 * @param sessionKey
	 * @return
	 * @throws UserNotFoundException 
	 */
	int getUserIdBySessionKey(String sessionKey) throws UserNotFoundException;
	
	/**
	 * Remove the expired sessions.
	 * @return
	 */
	public int removeExpiredSessions();

}
