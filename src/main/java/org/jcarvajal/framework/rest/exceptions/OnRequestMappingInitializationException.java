package org.jcarvajal.framework.rest.exceptions;

public class OnRequestMappingInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5413098367881789388L;

	public OnRequestMappingInitializationException(String message, Object... params) {
		super(String.format(message, params));
	}
}
