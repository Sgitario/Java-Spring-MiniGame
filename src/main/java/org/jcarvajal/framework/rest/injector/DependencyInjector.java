package org.jcarvajal.framework.rest.injector;

public interface DependencyInjector {
	public void init();
	
	public <T> T get(Class<T> clazz);
}
