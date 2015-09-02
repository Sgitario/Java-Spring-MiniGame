package org.jcarvajal.tiny.exceptions;

/**
 * Exception raised at tiny server configuration.
 * @author JoseCH
 *
 */
public class OnInitConfigurationException extends Exception {

	/**
	 * Initializes a new instance of the OnInitWebConfigurationException class.
	 * @param cause
	 */
	public OnInitConfigurationException(String cause) {
		super(cause);
	}
	
	/**
	 * Initializes a new instance of the OnInitWebConfigurationException class.
	 * @param cause
	 */
	public OnInitConfigurationException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7808606804585975870L;

}
