package org.jcarvajal.framework.rest.exceptions;

public class OnRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7849955044290200700L;
	
	public OnRequestException(String message, Exception ex) {
		super(message, ex);
	}

}
