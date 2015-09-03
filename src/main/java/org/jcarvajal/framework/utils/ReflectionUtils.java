package org.jcarvajal.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ReflectionUtils {
	
	private static final String SET = "set";
	private static final String GET = "get";
	
	private static final Logger LOG = Logger.getLogger(
			ReflectionUtils.class.getName());
	
	public static Class<?> createClass(String className) {
		Class<?> clazz = null;
		
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			LOG.severe(String.format("Class %s cannot be found. Cause: %s", className, e.getMessage()));
		}
		
		return clazz;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(String className, 
			Class<T> clazz, Map<String, String> params) {
		T instance = null;
		try {
			Class<?> clazzInstance = createClass(className);
			if (clazz.isAssignableFrom(clazzInstance)) {
				instance = (T) clazzInstance.newInstance();
				
				if (params != null) {
					for (Entry<String, String> param : params.entrySet()) {
						invokeSetField(instance, param.getKey(), param.getValue());
					}
				}
			}
		} catch (Exception e) {
			LOG.severe(String.format("Class %s cannot be instantiated. Cause: %s", className, e.getMessage()));
			instance = null;
		}
	
		return instance;
	}
	
	public static void invokeSetField(Object instance, 
			String fieldName, Object value) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException {
		if (value != null) {
			invokeSetField(instance, fieldName, value, value.getClass());
		}
	}
	
	public static void invokeSetField(Object instance, 
			String fieldName, Object value, Class<?> valueInterface) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException {
		if (value != null && instance != null) {
			Method method = instance.getClass().getMethod(String.format("%s%s", 
					SET, StringUtils.capitalize(fieldName)), valueInterface);
			if (method != null) {
				method.invoke(instance, value);
			}
		}
	}
	
	public static Object invokeGetField(Object instance, 
			String fieldName) throws NoSuchMethodException, SecurityException, 
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object value = null;
		if (instance != null) {
			Method method = instance.getClass().getMethod(String.format("%s%s", 
					GET, StringUtils.capitalize(fieldName)));
			if (method != null) {
				value = method.invoke(instance);
			}
		}
		
		return value;
	}
}
