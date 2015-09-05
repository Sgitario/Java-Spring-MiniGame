package org.jcarvajal.framework.di;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.di.instances.InitializationInstance;
import org.jcarvajal.framework.di.instances.Instance;
import org.jcarvajal.framework.rest.servlet.injector.DependencyInjector;

/**
 * The DependencyInjectorBase provides an implementation of the DependencyInjector
 * interface by the rest api.
 * 
 * @author jhilario
 *
 */
public abstract class DependencyInjectorBase implements DependencyInjector {
	
	private static final Logger LOG = Logger.getLogger(
			DependencyInjectorBase.class.getName());
	
	private final Map<String, Instance> repository = new LinkedHashMap<String, Instance>();
	
	/**
	 * Inject the class from the repository.
	 * In case of the class is not registered, the api will try to create it.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String className) {
		T object = null;
		Instance facade = repository.get(className);
		
		// if not found, try to register it
		if (facade == null) {
			facade = initComponent(new Dependency(className));
		}
		
		// If finally found, then instantiate it.
		if (facade != null) {
			object = (T) facade.instance();
		}
		
		return object;
	}
	
	/**
	 * Register a list of components into the repository.
	 * @param components
	 * @throws InstantiationException 
	 */
	protected final synchronized void initComponents(List<Dependency> components) {
		if (components != null) {
			for (Dependency component : components) {
				initComponent(component);
			}
		}
	}
	
	/**
	 * Register a component into the repository.
	 * @param components
	 * @throws InstantiationException 
	 */
	protected final synchronized Instance initComponent(Dependency component) {
		Instance instance = null;
		if (component != null) {
			String bindTo = component.getBindTo();
			if (!repository.containsKey(bindTo)) {				
				try {
					instance = new InitializationInstance(bindTo, 
							component.getImplementedBy(), 
							component.getParams(), 
							this);
					repository.put(bindTo, instance);
				} catch (InstantiationException e) {
					LOG.warning(String.format("Instance %s cannot be resolved", bindTo));
				}				
			} 
		}
		
		return instance;
	}
}
