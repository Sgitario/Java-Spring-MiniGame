package org.jcarvajal.framework.rest.controllers;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;

public interface ResponseMarshaller {
	public byte[] marshall(Object object) throws OnRequestMappingInitializationException;
}
