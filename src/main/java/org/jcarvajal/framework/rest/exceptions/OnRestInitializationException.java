package org.jcarvajal.framework.rest.exceptions;

/**
 * Exception raised at rest server configuration.
 * @author JoseCH
 *
 */
public class OnRestInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7808606804585975870L;
	
	/**
	 * Initializes a new instance of the OnRestInitializationException class.
	 * @param cause
	 */
	public OnRestInitializationException(String cause) {
		super(cause);
	}
	
	/**
	 * Initializes a new instance of the OnRestInitializationException class.
	 * @param cause
	 */
	public OnRestInitializationException(Throwable cause) {
		super(cause);
	}

}
