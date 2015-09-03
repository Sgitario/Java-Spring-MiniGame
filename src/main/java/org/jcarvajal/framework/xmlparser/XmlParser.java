package org.jcarvajal.framework.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jcarvajal.framework.utils.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {
	
	private final InputStream currentStream;
	private final Document doc;
	
	public XmlParser(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		currentStream = is;

		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(currentStream);
	}
	
	public void close() {
		IOUtils.close(currentStream);
	}
	
	public <T> List<T> listElementsByTagName(String elementName,
			Parseable<T> parseable) {
		
		List<T> map = new ArrayList<T>(); 
		NodeList nodes = doc.getElementsByTagName(elementName);
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				T value = parseable.parse(elem);
				map.add(value);
			}
		}
		
		return map;
	}
	
	public <T> Map<String, T> mapElementsByTagName(Element root, String elementName,
			String key, Parseable<T> parseable) {
		
		Map<String, T> map = new LinkedHashMap<String, T>(); 
		NodeList nodes = root.getElementsByTagName(elementName);
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				String keyValue = readElemValue(elem, key);
				T value = parseable.parse(elem);
				map.put(keyValue, value);
			}
		}
		
		return map;
	}
	
	public <T> Map<String, T> mapElementsByTagName(String elementName,
			String key, Parseable<T> parseable) {
		
		Map<String, T> map = new LinkedHashMap<String, T>(); 
		NodeList nodes = doc.getElementsByTagName(elementName);
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				String keyValue = readElemValue(elem, key);
				T value = parseable.parse(elem);
				map.put(keyValue, value);
			}
		}
		
		return map;
	}
	
	public <T> T readDocValue(String paramName, Parseable<T> parser) {
		T value = null;
		NodeList node = doc.getElementsByTagName(paramName);
		if (node.getLength() > 0) {
			Element elemParam = (Element) node.item(0);
			value = parser.parse(elemParam);
		}
		
		return value;
	}
	
	public static String readAttributeValue(Element element, String attrName) {
		return element.getAttribute(attrName);
	}
	
	public static String readElemValue(Element element, String paramName) {		
		return readElemValue(element, paramName, new StringParseable());
	}
	
	public static <T> T readElemValue(Element element, String paramName, Parseable<T> parser) {
		T value = null;
		NodeList node = element.getElementsByTagName(paramName);
		if (node.getLength() > 0) {
			Element elemParam = (Element) node.item(0);
			value = parser.parse(elemParam);
		}
		
		return value;
	}
}
