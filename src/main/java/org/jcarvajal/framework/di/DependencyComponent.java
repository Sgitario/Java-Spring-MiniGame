package org.jcarvajal.framework.di;

import java.util.Map;

public class DependencyComponent {
	private final String bindTo;
	private final String implementedBy;
	private final Map<String, String> params;
	
	public DependencyComponent(String bindTo, String implementedBy, 
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
