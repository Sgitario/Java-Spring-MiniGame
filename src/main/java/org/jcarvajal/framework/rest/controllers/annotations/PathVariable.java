package org.jcarvajal.framework.rest.controllers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Match the path variable against a parameter.
 * Example
 * @RequestMapping("/test/{val}")
 * public String test(@PathVariable('val) int val) {
 * 	...
 * }
 * 
 * The url: /test/12 will invoke the test method with the val value set to 12.
 * Only supports integers.
 * 
 * @author jhilario
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathVariable {
	public String name();
}
