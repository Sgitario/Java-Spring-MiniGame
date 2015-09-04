package org.jcarvajal.framework.rest.servlet.injector;

public interface DependencyInjector {
	public void init();
	
	public <T> T get(String className);
}
