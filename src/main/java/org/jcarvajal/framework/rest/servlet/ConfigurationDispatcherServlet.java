package org.jcarvajal.framework.rest.servlet;

import static org.jcarvajal.framework.xmlparser.XmlParser.readAttributeValue;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;
import org.jcarvajal.framework.utils.IOUtils;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;
import org.jcarvajal.framework.xmlparser.Parseable;
import org.jcarvajal.framework.xmlparser.StringParseable;
import org.jcarvajal.framework.xmlparser.XmlParser;
import org.w3c.dom.Element;

public class ConfigurationDispatcherServlet extends DispatcherServlet {

	private static final String INJECTOR = "injector";
	private static final String CONTROLLER = "controller";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String PARAM = "param";
	private static final String PARAM_KEY = "param-name";
	private static final String PARAM_VALUE = "param-value";
	private static final Logger LOG = Logger.getLogger(
			ConfigurationDispatcherServlet.class.getName());
	
	private String contextConfigLocation;
	
	public void setContextConfigLocation(String contextConfigLocation) {
		this.contextConfigLocation = contextConfigLocation;
	}

	/**
	 * Read the context config file.
	 * Initialize the injector.
	 * Initialize the controllers.
	 */
	public void init() throws OnRestInitializationException {		
		InputStream is = null;
		XmlParser parser = null;
		try {
			is = this.getFileStream(contextConfigLocation);
			parser = new XmlParser(is);
			
			parseInjector(parser);
			parseControllers(parser);
		} catch (Exception ex) {
			throw new OnRestInitializationException("Exception creating servlet dispatcher. Cause: " + ex.getMessage());
		} finally {
			IOUtils.close(is);
		}
	}
	
	protected InputStream getFileStream(String file) throws OnRestInitializationException {
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			throw new OnRestInitializationException("Servlet config file cannot be found.");
		}
		
		return is;
	}
	
	/**
	 * Parse the injector from the config file.
	 * @param parser
	 */
	private void parseInjector(final XmlParser parser) {
		initializeInjector(parser.readDocValue(INJECTOR, new Parseable<DependencyInjector>() {

			public DependencyInjector parse(Element elem) {
				DependencyInjector injector = null;
				if (elem != null) {
					String className = readAttributeValue(elem, CLASS_ATTRIBUTE);
					if (StringUtils.isNotEmpty(className)) {
						Map<String, String> params = parser
								.mapElementsByTagName(elem, PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE));
						
						injector = ReflectionUtils.createInstance(className, DependencyInjector.class, params);
					} else {
						LOG.warning("Injector class name is empty. Ignoring.");
					}
				}
				
				return injector;
			}
			
		}));
	}
	
	/**
	 * Register a controller.
	 * @param parser
	 */
	private void parseControllers(final XmlParser parser) {
		parser.listElementsByTagName(CONTROLLER, new Parseable<Void>() {

			public Void parse(Element elem) {
				String className = readAttributeValue(elem, CLASS_ATTRIBUTE);
				Map<String, String> params = parser
						.mapElementsByTagName(elem, PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE));
				Object controller = ReflectionUtils.createInstance(className, params);
				if (controller != null) {
					try {
						registerController(controller);
					} catch (OnRestInitializationException e) {
						LOG.severe(String.format("Error registering controller %s. Cause: %s",
								className, e.getMessage()));
					} catch (OnRequestMappingInitializationException e) {
						LOG.severe(String.format("Error registering request handler %s. Cause: %s",
								className, e.getMessage()));
					}
				}
				
				return null;
			}
			
		});
	}

}
