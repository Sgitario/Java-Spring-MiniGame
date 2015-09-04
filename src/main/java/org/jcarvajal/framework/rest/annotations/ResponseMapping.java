package org.jcarvajal.framework.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jcarvajal.framework.rest.servlet.controllers.MarshallType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseMapping {
	public String map();
	public MarshallType type() default MarshallType.CSV;
}
