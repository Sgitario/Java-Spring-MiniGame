package org.jcarvajal.framework.di.exceptions;

import org.jcarvajal.framework.di.builders.Instance;

public class InstantiationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214899930750642730L; 

	/**
	 * Initializes a new instance of the InstantiationException class.
	 * @param instance Instance that cannot be created.
	 */
	public InstantiationException(Instance instance) {
		this("", instance);
	}
	
	/**
	 * Initializes a new instance of the InstantiationException class.
	 * @param instance Instance that cannot be created.
	 */
	public InstantiationException(String message, Instance instance) {
		super(String.format("%sInstance of %s cannot be created for %s", message, instance.getImplClazz(), instance.getBindClazzName()));
	}
}
