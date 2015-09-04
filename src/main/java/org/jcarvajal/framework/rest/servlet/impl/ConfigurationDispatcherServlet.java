package org.jcarvajal.framework.rest.servlet.impl;

import static org.jcarvajal.framework.xmlparser.XmlParser.readAttributeValue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.DispatcherServlet;
import org.jcarvajal.framework.rest.servlet.injector.InjectorComponent;
import org.jcarvajal.framework.utils.IOUtils;
import org.jcarvajal.framework.xmlparser.Parseable;
import org.jcarvajal.framework.xmlparser.StringParseable;
import org.jcarvajal.framework.xmlparser.XmlParser;
import org.w3c.dom.Element;

/**
 * Initializes the current dispatcher based on xml config file.
 * The initialization involves:
 * - Register an dependency injector.
 * - Register controllers.
 * 
 * @author jhilario
 *
 */
public class ConfigurationDispatcherServlet extends DispatcherServlet {

	private static final String INJECTOR = "injector";
	private static final String CONTROLLER = "controller";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String PARAM = "param";
	private static final String PARAM_KEY = "param-name";
	private static final String PARAM_VALUE = "param-value";
	
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
			
			initInjector(parseInjector(parser));
			initControllers(parseControllers(parser));
		} catch (Exception ex) {
			throw new OnRestInitializationException(ex, "Exception creating servlet dispatcher. ");
		} finally {
			IOUtils.close(is);
		}
	}

	protected InputStream getFileStream(String file) throws OnRestInitializationException {
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			throw new OnRestInitializationException("Servlet config file %s cannot be found.", file);
		}
		
		return is;
	}
	
	/**
	 * Parse the injector from the config file.
	 * @param parser
	 */
	private InjectorComponent parseInjector(final XmlParser parser) {
		return parser.readDocValue(INJECTOR, new Parseable<InjectorComponent>() {

			public InjectorComponent parse(Element elem) {
				
				String className = readAttributeValue(elem, CLASS_ATTRIBUTE);
				Map<String, String> params = parser
						.mapElementsByTagName(elem, PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE));
				
				return new InjectorComponent(className, params);
			}
			
		});
	}
	
	/**
	 * Register a controller.
	 * @param parser
	 */
	private List<String> parseControllers(final XmlParser parser) {
		return parser.listElementsByTagName(CONTROLLER, new Parseable<String>() {

			public String parse(Element elem) {
				return readAttributeValue(elem, CLASS_ATTRIBUTE);
			}
			
		});
	}

}
