package org.jcarvajal.framework.rest.servlet.controllers;

public abstract class ControllerManager {

	public abstract void register(Object controller);
	public abstract Object handle(String url, RequestMethod method);
}
