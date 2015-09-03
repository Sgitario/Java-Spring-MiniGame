package org.jcarvajal.framework.di.exceptions;

/**
 * Exception raised at depedency injection configuration.
 * @author JoseCH
 *
 */
public class OnDependencyInjectionInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3034719530698731910L;

	/**
	 * Initializes a new instance of the OnDependencyInjectionInitializationException class.
	 * @param cause
	 */
	public OnDependencyInjectionInitializationException(String cause) {
		super(cause);
	}
	
	/**
	 * Initializes a new instance of the OnDependencyInjectionInitializationException class.
	 * @param cause
	 */
	public OnDependencyInjectionInitializationException(Throwable cause) {
		super(cause);
	}

}
