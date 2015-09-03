package org.jcarvajal.framework.di;

import static org.jcarvajal.framework.xmlparser.XmlParser.readAttributeValue;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.builders.InitializationInstance;
import org.jcarvajal.framework.di.builders.Instance;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.di.exceptions.OnDependencyInjectionInitializationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;
import org.jcarvajal.framework.rest.servlet.ConfigurationDispatcherServlet;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.xmlparser.Parseable;
import org.jcarvajal.framework.xmlparser.StringParseable;
import org.jcarvajal.framework.xmlparser.XmlParser;
import org.w3c.dom.Element;

/**
 * Read config file to bind all components and services.
 * Instantiate autowired annotations.
 * @author JoseCH
 *
 */
public class ConfigDependencyInjectorImpl implements DependencyInjector {	
	private static final Logger LOG = Logger.getLogger(
			ConfigurationDispatcherServlet.class.getName());

	private static final String REPOSITORY_ELEM = "repository";
	private static final String SERVICE_ELEM = "service";
	private static final String NAME = "name";
	private static final String IMPLEMENTED_BY = "implementedBy";
	private static final String PARAM = "param";
	private static final String PARAM_KEY = "param-name";
	private static final String PARAM_VALUE = "param-value";
	
	private String configFile;
	private final Map<String, Instance> repository = new LinkedHashMap<String, Instance>();
	
	public void init() {
		InputStream is = null;
		try {
			is = getFileStream(configFile);
			XmlParser parser = new XmlParser(is);
			
			parseComponents(REPOSITORY_ELEM, parser);
			parseComponents(SERVICE_ELEM, parser);
			
			resolveAutowired();
		} catch (Exception ex) {
			LOG.severe("Error when init Config Dependency Injector.");
		}
	}

	public String getConfigFile() {
		return configFile;
	}
	
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
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
	
	protected InputStream getFileStream(String file) throws OnDependencyInjectionInitializationException {
		if (file == null) {
			throw new OnDependencyInjectionInitializationException("Config file cannot be null.");
		}
		
		return this.getClass().getResourceAsStream(file);
	}
	
	private synchronized void parseComponents(final String elem, final XmlParser parser) {
		parser.listElementsByTagName(elem, new Parseable<Void>() {

			public Void parse(Element elem) {
				
				String bind = readAttributeValue(elem, NAME);
				if (!repository.containsKey(bind)) {
					Class<?> bindClazz = ReflectionUtils.createClass(bind);
					if (bindClazz != null) {
						String implementedBy = readAttributeValue(elem, IMPLEMENTED_BY);
						
						InitializationInstance instance = new InitializationInstance();
						try {
							instance.bind(bindClazz, implementedBy, 
									parser.mapElementsByTagName(PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE)));
						} catch (InstantiationException e) {
							e.printStackTrace();
							LOG.severe("Instance cannot be created. Cause: " + e.getMessage());
						}
						
						repository.put(bind, instance);
						
					} else {
						LOG.warning(String.format("Class %s not found. Ignoring.", bind));
					}
					
				} else {
					LOG.warning(String.format("Componenty %s duplicated in config file", bind));
				}
				
				return null;
			}
			
		});
	}
	
	private void resolveAutowired() {
		
	}
}
