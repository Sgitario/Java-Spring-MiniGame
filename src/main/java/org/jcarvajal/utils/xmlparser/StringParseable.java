package org.jcarvajal.utils.xmlparser;

import org.jcarvajal.utils.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StringParseable implements Parseable<String> {

	private String elemName;
	
	/**
	 * Get first element.
	 */
	public StringParseable() {
		
	}
	
	public StringParseable(String elemName) {
		this.elemName = elemName;
	}
	
	public String parse(Element elem) {
		String value = null;
		if (elem != null && elem.getChildNodes().getLength() > 0) {
			if (StringUtils.isNotEmpty(elemName)) {
				value = XmlParser.readElemValue(elem, elemName);
			} else {
				// First node
				NodeList textFNList = elem.getChildNodes();
				value = ((Node) textFNList.item(0)).getNodeValue().trim();
			}
		}
		
		return value;
	}
	
}
