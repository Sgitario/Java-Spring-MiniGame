package org.jcarvajal.framework.rest.exceptions;

/**
 * Exception raised when there is an exception at request mapping initialization.
 * @author jhilario
 *
 */
public class OnRequestMappingInitializationException extends Exception {
	private static final long serialVersionUID = -5413098367881789388L;

	/**
	 * Initializes a new instance of the OnRequestMappingInitializationException class.
	 * @param message
	 * @param params
	 */
	public OnRequestMappingInitializationException(String message, Object... params) {
		super(String.format(message, params));
	}
}
