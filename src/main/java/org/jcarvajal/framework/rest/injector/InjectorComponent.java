package org.jcarvajal.framework.rest.injector;

import java.util.Map;

public class InjectorComponent {
	private final String className;
	private final Map<String, String> params;
	
	public InjectorComponent(String className, 
			Map<String, String> params) {
		this.className = className;
		this.params = params;
	}
	
	public String getClassName() {
		return className;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
}
