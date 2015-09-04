package org.jcarvajal.framework.rest.controllers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Match the path variable against a parameter.
 * Example
 * @RequestMapping("/test")
 * public String test(@RequestParam('val) String val) {
 * 	...
 * }
 * 
 * The url: /test?val=Hi will invoke the test method with the val value set to Hi.
 * 
 * In case of no param provided, the val value will be set to null.
 * Only supports String.
 * 
 * @author jhilario
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
	public String attr();
}
