package org.jcarvajal.framework.rest.config.impl;

import static org.jcarvajal.framework.xmlparser.XmlParser.readElemValue;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPathExpressionException;

import org.jcarvajal.framework.rest.config.WebConfiguration;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.Servlet;
import org.jcarvajal.framework.utils.IOUtils;
import org.jcarvajal.framework.xmlparser.Parseable;
import org.jcarvajal.framework.xmlparser.StringParseable;
import org.jcarvajal.framework.xmlparser.XmlParser;
import org.w3c.dom.Element;

/**
 * Load the web.xml placed in the fixed position (within the jar) and return the list
 * of servlets.
 * @author JoseCH
 *
 */
public class XmlWebConfiguration implements WebConfiguration {

	private final String WEB_FILE = "WEB-INF/web.xml";

	private final String SERVLET_NODES = "servlet";
	private final String SERVLET_NAME = "servlet-name";
	private final String SERVLET_CLASS_NAME = "servlet-class";
	private final String SERVLET_PARAMS = "init-param";
	private final String SERVLET_PARAM_NAME = "param-name";
	private final String SERVLET_PARAM_VALUE = "param-value";
	private final String SERVLET_MAPPINGS = "servlet-mapping";
	private final String SERVLET_MAPPING_URL_PATTERN = "url-pattern";
	
	private Map<String, Servlet> mappingServlets;
	
	/**
	 * Read xml and allocate the servlet into memory.
	 * 
	 * @throws OnRestInitializationException
	 */
	public void init() throws OnRestInitializationException {
		InputStream is = null;
		XmlParser parser = null;
		try {
			is = getFileStream(WEB_FILE);
			parser = new XmlParser(is);
			
			Map<String, Servlet> servlets = parseServlets(parser);
			Map<String, String> mappings = parseMappings(parser);
			
			mappingServlets = linkMappingsWithServlets(mappings, servlets);

		} catch (Exception e) {
			throw new OnRestInitializationException(e, "Error starting xml web configuration");
		} finally {
			IOUtils.close(is);
		}
	}

	public Map<String, Servlet> getServlets() {
		return mappingServlets;
	}
	
	protected InputStream getFileStream(String file) throws OnRestInitializationException {
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			throw new OnRestInitializationException("Web xml %s not found.", file);
		}
		
		return is;
	}

	/**
	 * Parse the list of servlets from xml to Servlet model interface.
	 * @param parser
	 * @return
	 * @throws XPathExpressionException
	 */
	private Map<String, Servlet> parseServlets(final XmlParser parser)
			throws XPathExpressionException {
		Map<String, Servlet> servlets = parser.mapElementsByTagName(SERVLET_NODES,
				SERVLET_NAME,
				new Parseable<Servlet>() {

					public Servlet parse(Element elem) {
						
						String servletName = readElemValue(elem, SERVLET_NAME);
						String servletClass = readElemValue(elem, SERVLET_CLASS_NAME);
						Map<String, String> params = parser.mapElementsByTagName(
								elem, 
								SERVLET_PARAMS,
								SERVLET_PARAM_NAME,
								new StringParseable(SERVLET_PARAM_VALUE));

						return new Servlet(servletName, servletClass, params);
					}
		});

		return servlets;
	}

	/**
	 * Parse mappings URL match -> ServletName.
	 * @param parser
	 * @return
	 */
	private Map<String, String> parseMappings(final XmlParser parser) {
		return parser.mapElementsByTagName(SERVLET_MAPPINGS,
				SERVLET_MAPPING_URL_PATTERN,
				new StringParseable(SERVLET_NAME));
	}
	
	/**
	 * Link the URL match against the proper instance of the servlet.
	 * @param mappings
	 * @param servlets
	 * @return
	 */
	private Map<String, Servlet> linkMappingsWithServlets(
			Map<String, String> mappings, Map<String, Servlet> servlets) {
		Map<String, Servlet> mappingServlets = new LinkedHashMap<String, Servlet>();
		if (mappings != null && servlets != null) {
			for (Entry<String, String> map : mappings.entrySet()) {
				Servlet found = servlets.get(map.getValue());
				mappingServlets.put(map.getKey(), found);
			}
		}
		
		return mappingServlets;
	}
	
}
