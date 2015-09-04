package org.jcarvajal.framework.di.exceptions;

/**
 * Exception raises when the dependency instance cannot be created. 
 * @author jhilario
 *
 */
public class InstantiationException extends Exception {
	
	private static final long serialVersionUID = -2214899930750642730L; 

	/**
	 * Initializes a new instance of the InstantiationException class.
	 * @param instance Instance that cannot be created.
	 */
	public InstantiationException(String message, Object ... args) {
		super(String.format(message, args));
	}
}
