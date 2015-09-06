package org.jcarvajal.framework.rest.servlet.controllers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jcarvajal.framework.rest.RequestMethod;

/**
 * Will register the method and url request.
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
