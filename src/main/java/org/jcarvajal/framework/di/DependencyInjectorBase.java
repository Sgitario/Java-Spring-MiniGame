package org.jcarvajal.framework.di;

import static org.jcarvajal.framework.xmlparser.XmlParser.readAttributeValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.builders.InitializationInstance;
import org.jcarvajal.framework.di.builders.Instance;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.xmlparser.StringParseable;

public abstract class DependencyInjectorBase implements DependencyInjector {
	
	private static final Logger LOG = Logger.getLogger(
			DependencyInjectorBase.class.getName());
	
	private final Map<String, Instance> repository = new LinkedHashMap<String, Instance>();
	
	@SuppressWarnings("unchecked")
	public <T> T get(String className) {
		T object = null;
		Instance facade = repository.get(className);
		if (facade != null) {
			Object objectInRepository = facade.instance();
			if (objectInRepository != null 
					&& facade.getBindClazz().isAssignableFrom(objectInRepository.getClass())) {
				object = (T) objectInRepository;
			}
		} else {
			// if not found, try to register it
			
		}
		
		return object;
	}
	
	protected synchronized boolean isRegistered(String bind) {
		return repository.containsKey(bind);
	}
	
	protected void bind(String bindTo, String implementedBy,
			Map<String, String> params) {
		Class<?> bindClazz = ReflectionUtils.createClass(bindTo);
		if (bindClazz != null) {
			InitializationInstance instance = new InitializationInstance(this);
			try {
				instance.bind(bindClazz, implementedBy, params);
			} catch (InstantiationException e) {
				LOG.severe("Instance cannot be created. Cause: " + e.getMessage());
			}
			
			repository.put(bindClazz.getName(), instance);
			
		} else {
			LOG.warning(String.format("Class %s not found. Ignoring.", bindTo));
		}
	}
}
