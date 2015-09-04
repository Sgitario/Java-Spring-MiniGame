package org.jcarvajal.framework.di;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.builders.InitializationInstance;
import org.jcarvajal.framework.di.builders.Instance;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;

public abstract class DependencyInjectorBase implements DependencyInjector {
	
	private static final Logger LOG = Logger.getLogger(
			DependencyInjectorBase.class.getName());
	
	private final Map<String, Instance> repository = new LinkedHashMap<String, Instance>();
	
	@SuppressWarnings("unchecked")
	public <T> T get(String className) {
		T object = null;
		Instance facade = repository.get(className);
		
		// if not found, try to register it
		if (facade == null) {
			facade = bind(className, className, Collections.EMPTY_MAP);
		}
		
		// If finally found, then instantiate it.
		if (facade != null) {
			Object objectInRepository = facade.instance();
			if (objectInRepository != null 
					&& facade.getBindClazz().isAssignableFrom(objectInRepository.getClass())) {
				object = (T) objectInRepository;
			}
		}
		
		return object;
	}
	
	protected synchronized boolean isRegistered(String bind) {
		return repository.containsKey(bind);
	}
	
	protected synchronized void initComponents(List<DependencyComponent> components) {
		if (components != null) {
			for (DependencyComponent component : components) {
				if (!isRegistered(component.getBindTo())) {
					String implementedBy = component.getImplementedBy();
					if (!StringUtils.isNotEmpty(implementedBy)) {
						// If implementedBy not present, use name attribute.
						implementedBy = component.getBindTo();
					}
					
					bind(component.getBindTo(), implementedBy, component.getParams());
					
				} else {
					LOG.warning(String.format("Componenty %s duplicated in config file", component.getBindTo()));
				}
			}
		}
		
	}
	
	private synchronized Instance bind(String bindTo, String implementedBy,
			Map<String, String> params) {
		Instance instance = null;
		Class<?> bindClazz = ReflectionUtils.createClass(bindTo);
		if (bindClazz != null) {
			instance = new InitializationInstance(this);
			try {
				instance.bind(bindClazz, implementedBy, params);
			} catch (InstantiationException e) {
				LOG.severe("Instance cannot be created. Cause: " + e.getMessage());
			}
			
			repository.put(bindClazz.getName(), instance);
			
		} else {
			LOG.warning(String.format("Class %s not found. Ignoring.", bindTo));
		}
		
		return instance;
	}
}
