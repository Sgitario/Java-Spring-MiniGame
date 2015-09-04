package org.jcarvajal.framework.rest.controllers.marshallers;

import org.jcarvajal.framework.rest.controllers.ResponseMarshaller;

/**
 * Basic implementation of ResponseMarshaller that will marshall everything into a string 
 * and then an array of bytes.
 * 
 * @author JoseCH
 *
 */
public class DefaultResponseMarshaller implements ResponseMarshaller {

	/**
	 * Default implementation of marshaller.
	 * Parse object into string and then byte.
	 */
	public byte[] marshall(Object object) {
		byte[] response = null;
		if (object != null) {
			response = object.toString().getBytes();
		}
		
		return response;
	}

}
