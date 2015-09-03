package org.jcarvajal.framework.rest.servlet.controllers.handlers;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;

public interface RequestHandler {
	public boolean satisfy(String url, String method);
	public byte[] invoke(String url) throws OnRequestException;
}
