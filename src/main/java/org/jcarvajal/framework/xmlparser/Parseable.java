package org.jcarvajal.framework.xmlparser;

import org.w3c.dom.Element;

/**
 * Define operation to parse an element into a generic type.
 * @author JoseCH
 *
 * @param <T>
 */
public interface Parseable<T> {
	public T parse(Element elem);
}
