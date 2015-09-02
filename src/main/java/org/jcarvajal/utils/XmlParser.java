package org.jcarvajal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
	
	public interface Parseable<T> {
		public T parse(Element elem);
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
	
	public String readElemValue(Element element, String paramName) {
		String value = null;
		NodeList node = element.getElementsByTagName(paramName);
		if (node.getLength() > 0) {
			Element elemParam = (Element) node.item(0);
			NodeList textFNList = elemParam.getChildNodes();
			value = ((Node) textFNList.item(0)).getNodeValue().trim();
		}
		
		return value;
	}
}
