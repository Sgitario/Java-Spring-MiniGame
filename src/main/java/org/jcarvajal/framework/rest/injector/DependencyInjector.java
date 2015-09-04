package org.jcarvajal.framework.rest.injector;

public interface DependencyInjector {
	public void init();
	
	public <T> T get(String className);
}
