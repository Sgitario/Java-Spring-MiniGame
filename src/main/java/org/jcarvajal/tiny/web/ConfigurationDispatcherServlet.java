package org.jcarvajal.tiny.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.jcarvajal.tiny.injector.DependencyInjector;
import org.jcarvajal.utils.IOUtils;
import org.jcarvajal.utils.ReflectionUtils;
import org.jcarvajal.utils.StringUtils;
import org.jcarvajal.utils.xmlparser.Parseable;
import org.jcarvajal.utils.xmlparser.StringParseable;
import org.jcarvajal.utils.xmlparser.XmlParser;
import org.w3c.dom.Element;

public class ConfigurationDispatcherServlet extends DispatcherServlet {

	private static final String TINY_INJECTOR = "injector";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String PARAM = "param";
	private static final String PARAM_KEY = "param-name";
	private static final String PARAM_VALUE = "param-value";
	private static final Logger LOG = Logger.getLogger(
			ConfigurationDispatcherServlet.class.getName());
	
	protected String contextConfigLocation;

	/**
	 * Read the context config file.
	 * Initialize the injector.
	 * Initialize the controllers.
	 */
	public void init() throws OnInitConfigurationException {		
		InputStream is = null;
		XmlParser parser = null;
		try {
			is = this.getFileStream(contextConfigLocation);
			parser = new XmlParser(is);
			
			parseInjector(parser);
			parseControllers(parser);
		} catch (Exception ex) {
			throw new OnInitConfigurationException("Exception creating tiny dispatcher. Cause: " + ex.getMessage());
		} finally {
			IOUtils.close(is);
		}
	}

	private void parseControllers(XmlParser parser) {
		// TODO Auto-generated method stub
		
	}

	public byte[] handle(URI requestURI, String requestMethod,
			OutputStream responseBody) {
		
		
		
		return null;
	}
	
	protected InputStream getFileStream(String file) throws OnInitConfigurationException {
		if (file == null) {
			throw new OnInitConfigurationException("Config file cannot be null.");
		}
		
		return this.getClass().getResourceAsStream(file);
	}
	
	/**
	 * Parse the injector from the config file.
	 * @param parser
	 */
	private void parseInjector(final XmlParser parser) {
		initializeInjector(parser.readDocValue(TINY_INJECTOR, new Parseable<DependencyInjector>() {

			public DependencyInjector parse(Element elem) {
				DependencyInjector injector = null;
				if (elem != null) {
					String className = elem.getAttribute(CLASS_ATTRIBUTE);
					if (StringUtils.isNotEmpty(className)) {
						Map<String, String> params = parser
								.mapElementsByTagName(PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE));
						
						injector = ReflectionUtils.createInstance(className, DependencyInjector.class, params);
					} else {
						LOG.warning("Injector class name is empty. Ignoring.");
					}
				}
				
				return injector;
			}
			
		}));
	}

}