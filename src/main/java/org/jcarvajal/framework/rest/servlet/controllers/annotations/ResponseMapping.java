package org.jcarvajal.framework.rest.servlet.controllers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Will marshall the response into a proper response.
 * 
 * public class Person {
 * 	public int getAge() {...}
 *  public int getName() {...}
 * }
 * 
 * @ResponseMapping(map="{name} is {age} years old")
 * public List<Person> test() {
 * 	...
 * } 
 * 
 * The output will return "Jose is 12 years old,Antonio is 19 years old, ...". 
 * 
 * For collections, it will only support CSV format.
 * @author jhilario
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseMapping {
	public String map();
}
