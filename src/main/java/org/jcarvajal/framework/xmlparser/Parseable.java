package org.jcarvajal.framework.xmlparser;

import org.w3c.dom.Element;

public interface Parseable<T> {
	public T parse(Element elem);
}
