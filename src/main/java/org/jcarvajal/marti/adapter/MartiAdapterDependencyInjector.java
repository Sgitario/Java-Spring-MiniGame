package org.jcarvajal.marti.adapter;

import org.jcarvajal.tiny.injector.DependencyInjector;

public class MartiAdapterDependencyInjector implements DependencyInjector {
	private String configFile;

	public void init() {
		
	}
	
	public String getConfigFile() {
		return configFile;
	}
	
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
}
