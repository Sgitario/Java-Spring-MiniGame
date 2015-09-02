package org.jcarvajal.utils.xmlparser;

import org.w3c.dom.Element;

public interface Parseable<T> {
	public T parse(Element elem);
}
