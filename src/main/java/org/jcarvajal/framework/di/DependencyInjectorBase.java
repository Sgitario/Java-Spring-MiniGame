package org.jcarvajal.framework.di;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.builders.InitializationInstance;
import org.jcarvajal.framework.di.builders.Instance;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;

public abstract class DependencyInjectorBase implements DependencyInjector {
	
	private static final Logger LOG = Logger.getLogger(
			DependencyInjectorBase.class.getName());
	
	private final Map<String, Instance> repository = new LinkedHashMap<String, Instance>();
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		T object = null;
		Instance facade = repository.get(clazz.getName());
		if (facade != null) {
			Object objectInRepository = facade.instance();
			if (objectInRepository != null 
					&& clazz.isAssignableFrom(objectInRepository.getClass())) {
				object = (T) objectInRepository;
			}
		}
		
		return object;
	}
	
	protected synchronized boolean isRegistered(String bind) {
		return repository.containsKey(bind);
	}
	
	protected void bind(Class<?> bindClazz, String implementedBy,
			Map<String, String> params) {
		InitializationInstance instance = new InitializationInstance(this);
		try {
			instance.bind(bindClazz, implementedBy, params);
		} catch (InstantiationException e) {
			LOG.severe("Instance cannot be created. Cause: " + e.getMessage());
		}
		
		repository.put(bindClazz.getName(), instance);
	}
}
