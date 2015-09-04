package org.jcarvajal.minigame.exceptions;

/**
 * This exception is raised when the session key cannot be found.
 * @author JoseCH
 *
 */
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 8168785738762824147L;
	/**
	 * Initializes a new instance of the UserNotFoundException class.
	 * @param ex
	 * @param message
	 * @param args
	 */
	public UserNotFoundException(String message, Object... args) {
		super(String.format(message, args));
	}
}
