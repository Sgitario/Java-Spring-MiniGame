package org.jcarvajal.tiny.config;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPathExpressionException;

import org.jcarvajal.utils.XmlParser;
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
	 * @throws OnInitWebConfigurationException
	 */
	public void init() throws OnInitWebConfigurationException {
		XmlParser parser = null;
		try {
			parser = new XmlParser(getFileStream(WEB_FILE));
			
			Map<String, Servlet> servlets = readServlets(parser);
			Map<String, String> mappings = readMappings(parser);
			
			mappingServlets = linkMappingsWithServlets(mappings, servlets);

		} catch (Exception e) {
			throw new OnInitWebConfigurationException(e);
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

	public Map<String, Servlet> getServlets() {
		return mappingServlets;
	}
	
	protected InputStream getFileStream(String file) {
		return getClass().getClassLoader().getResourceAsStream(file);
	}

	private Map<String, Servlet> readServlets(final XmlParser parser)
			throws XPathExpressionException {
		Map<String, Servlet> servlets = parser.mapElementsByTagName(SERVLET_NODES,
				SERVLET_NAME,
				new XmlParser.Parseable<Servlet>() {

					public Servlet parse(Element elem) {
						Servlet servlet = new Servlet();
						servlet.setName(parser.readElemValue(elem, SERVLET_NAME));
						servlet.setClassName(parser.readElemValue(elem, SERVLET_CLASS_NAME));
						servlet.setParams(parser.mapElementsByTagName(SERVLET_PARAMS,
								SERVLET_PARAM_NAME,
								new XmlParser.Parseable<String>() {
				
									public String parse(Element elem) {
										return parser.readElemValue(elem, SERVLET_PARAM_VALUE);
									}
						}));

						return servlet;
					}
		});

		return servlets;
	}

	private Map<String, String> readMappings(final XmlParser parser) {
		return parser.mapElementsByTagName(SERVLET_MAPPINGS,
				SERVLET_MAPPING_URL_PATTERN,
				new XmlParser.Parseable<String>() {

					public String parse(Element elem) {
						return parser.readElemValue(elem, SERVLET_NAME);
					}
		});
	}
	
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
