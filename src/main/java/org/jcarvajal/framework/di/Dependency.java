package org.jcarvajal.framework.di;

import java.util.Map;

/**
 * Model interface to register components via DependencyInjectorBase.
 * 
 * @author jhilario
 *
 */
public class Dependency {
	private final String bindTo;
	private final String implementedBy;
	private final Map<String, String> params;
	
	/**
	 * Initializes a new instance of the DependencyComponent class 
	 * using the bind class and with no params.
	 * @param bindTo
	 */
	public Dependency(String bindTo) {
		this(bindTo, bindTo, null);
	}
	
	/**
	 * Initializes a new instance of the DependencyComponent class.
	 * @param bindTo
	 * @param implementedBy
	 * @param params
	 */
	public Dependency(String bindTo, String implementedBy, 
			Map<String, String> params) {
		this.bindTo = bindTo;
		this.implementedBy = implementedBy;
		this.params = params;
	}
	
	public String getBindTo() {
		return bindTo;
	}
	
	public String getImplementedBy() {
		return implementedBy;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
}
