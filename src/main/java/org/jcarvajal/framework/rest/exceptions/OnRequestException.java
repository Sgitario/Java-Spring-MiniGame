package org.jcarvajal.framework.rest.exceptions;

/**
 * Exception raised when the request cannot be handled.
 * @author jhilario
 *
 */
public class OnRequestException extends Exception {

	private static final long serialVersionUID = 7849955044290200700L;

	/**
	 * Initializes a new instance of the OnRequestException class.
	 * @param ex
	 * @param message
	 * @param args
	 */
	public OnRequestException(Exception ex, String message, Object... args) {
		super(String.format(message, args), ex);
	}

}
