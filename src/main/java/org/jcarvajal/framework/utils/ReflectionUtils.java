package org.jcarvajal.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * Utility class for reflection operations.
 * @author JoseCH
 *
 */
public class ReflectionUtils {
	
	private static final String SET = "set";
	private static final String GET = "get";
	
	private static final Logger LOG = Logger.getLogger(
			ReflectionUtils.class.getName());
	
	/**
	 * Search the class name in the classpath. 
	 * @param className
	 * @return 'Null' if the class is not found.
	 */
	public static Class<?> createClass(String className) {
		Class<?> clazz = null;
		
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			LOG.severe(String.format("Class %s cannot be found. Cause: %s", className, e.getMessage()));
		}
		
		return clazz;
	}
	
	/**
	 * Create instance and instantiate it against a specified type.
	 * @param className
	 * @param params
	 * @param clazzTo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createInstanceSafely(String className, 
			Map<String, String> params, Class<T> clazzTo) {
		T result = null;
		Object instance = createInstance(className, params);
		if (instance != null) {
			if (clazzTo.isAssignableFrom(instance.getClass())) {
				result = (T) instance;
			} else {
				LOG.severe(String.format("Class %s cannot be instantiated to %s", className, clazzTo.getName()));
			}
		}
	
		return result;
	}
	
	/**
	 * Invoke the set method of a field using the value class
	 * as the parameter type.
	 * @param instance
	 * @param fieldName
	 * @param value
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void invokeSetField(Object instance, 
			String fieldName, Object value) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException {
		if (value != null) {
			invokeSetField(instance, fieldName, value, value.getClass());
		}
	}
	
	/**
	 * Invoke a set method of a field.
	 * @param instance
	 * @param fieldName
	 * @param value
	 * @param valueInterface
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void invokeSetField(Object instance, 
			String fieldName, Object value, Class<?> valueInterface) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException {
		if (value != null && instance != null) {
			Method method = instance.getClass().getMethod(String.format("%s%s", 
					SET, StringUtils.capitalize(fieldName)), valueInterface);
			if (method != null) {
				invokeMethod(instance, method, value);
			}
		}
	}
	
	/**
	 * Invoke the Get method of a field.
	 * @param instance
	 * @param fieldName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeGetField(Object instance, 
			String fieldName) throws NoSuchMethodException, SecurityException, 
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object value = null;
		if (instance != null) {
			Method method = instance.getClass().getMethod(String.format("%s%s", 
					GET, StringUtils.capitalize(fieldName)));
			if (method != null) {
				value = invokeMethod(instance, method);
			}
		}
		
		return value;
	}
	
	/**
	 * Invoke a method using reflection.
	 * @param instance
	 * @param method
	 * @param params
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Object instance, Method method, Object... params) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException {
		Object value = null;
		if (method != null) {
			value = method.invoke(instance, params);
		}
		
		return value;
	}
	
	/**
	 * Create an instance and set the param values.
	 * The param values only supports String fields.
	 * 
	 * @param className
	 * @param params
	 * @return
	 */
	private static Object createInstance(String className, 
			Map<String, String> params) {
		Object instance = null;
		try {
			Class<?> clazzInstance = createClass(className);
			instance = clazzInstance.newInstance();
			
			if (params != null) {
				for (Entry<String, String> param : params.entrySet()) {
					invokeSetField(instance, param.getKey(), param.getValue());
				}
			}
		} catch (Exception e) {
			LOG.severe(String.format("Class %s cannot be instantiated. Cause: %s", className, e.getMessage()));
			instance = null;
		}
	
		return instance;
	}
}
