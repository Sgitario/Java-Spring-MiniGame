package org.jcarvajal.framework.rest.controllers;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;

/**
 * Interface to marshall the responses of the request handlers.
 * @author JoseCH
 *
 */
public interface ResponseMarshaller {
	/**
	 * Marshall the object into an array of bytes.
	 * @param object
	 * @return
	 * @throws OnRequestMappingInitializationException
	 */
	public byte[] marshall(Object object) 
			throws OnRequestMappingInitializationException;
}
