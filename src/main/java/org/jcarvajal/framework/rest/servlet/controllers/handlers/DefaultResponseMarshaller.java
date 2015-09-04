package org.jcarvajal.framework.rest.servlet.controllers.handlers;

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
