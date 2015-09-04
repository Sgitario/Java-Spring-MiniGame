package org.jcarvajal.framework.di;

import static org.jcarvajal.framework.xmlparser.XmlParser.readAttributeValue;

import java.io.InputStream;
import java.util.logging.Logger;

import org.jcarvajal.framework.di.exceptions.OnDependencyInjectionInitializationException;
import org.jcarvajal.framework.rest.servlet.ConfigurationDispatcherServlet;
import org.jcarvajal.framework.utils.IOUtils;
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
public class ConfigDependencyInjectorImpl extends DependencyInjectorBase {	
	private static final Logger LOG = Logger.getLogger(
			ConfigurationDispatcherServlet.class.getName());

	private static final String COMPONENT_ELEM = "component";
	private static final String NAME = "name";
	private static final String IMPLEMENTED_BY = "implementedBy";
	private static final String PARAM = "param";
	private static final String PARAM_KEY = "param-name";
	private static final String PARAM_VALUE = "param-value";
	
	private String configFile;
	
	public void init() {
		InputStream is = null;
		try {
			is = getFileStream(configFile);
			XmlParser parser = new XmlParser(is);
			
			parseComponents(parser);
		} catch (Exception ex) {
			LOG.severe("Error when init Config Dependency Injector. Cause: " + ex.getMessage());
		} finally {
			IOUtils.close(is);
		}
	}

	public String getConfigFile() {
		return configFile;
	}
	
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	protected InputStream getFileStream(String file) throws OnDependencyInjectionInitializationException {
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			throw new OnDependencyInjectionInitializationException("Dependency Injection Config file cannot be found.");
		}
		
		return is;
	}
	
	private synchronized void parseComponents(final XmlParser parser) {
		parser.listElementsByTagName(COMPONENT_ELEM, new Parseable<Void>() {

			public Void parse(Element elem) {
				
				String bindTo = readAttributeValue(elem, NAME);
				if (!isRegistered(bindTo)) {
					String implementedBy = readAttributeValue(elem, IMPLEMENTED_BY);
					
					bind(bindTo, implementedBy, 
							parser.mapElementsByTagName(elem, PARAM, PARAM_KEY, new StringParseable(PARAM_VALUE)));
					
				} else {
					LOG.warning(String.format("Componenty %s duplicated in config file", bindTo));
				}
				
				return null;
			}
			
		});
	}
}
