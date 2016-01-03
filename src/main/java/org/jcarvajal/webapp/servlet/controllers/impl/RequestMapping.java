package org.jcarvajal.webapp.servlet.controllers.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jcarvajal.webapp.server.RequestMethod;

/**
 * Will register url request.
 * 
 * @author jhilario
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
	public String url();
	public RequestMethod method();
}
