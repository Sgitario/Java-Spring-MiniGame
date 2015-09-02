package org.jcarvajal.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ReflectionUtils {
	
	private static final String SET = "set";
	
	private static final Logger LOG = Logger.getLogger(
			ReflectionUtils.class.getName());
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(String className, 
			Class<T> clazz, Map<String, String> params) {
		T instance = null;
		Class<?> clazzInstance;
		try {
			clazzInstance = Class.forName(className);
			if (clazz.isAssignableFrom(clazzInstance)) {
				instance = (T) clazzInstance.newInstance();
				
				if (params != null) {
					for (Entry<String, String> param : params.entrySet()) {
						invokeSetField(clazzInstance, instance, param.getKey(), param.getValue());
					}
				}
			}
		} catch (Exception e) {
			LOG.severe(String.format("Class %s cannot be instantiated. Cause: %s", className, e.getMessage()));
			instance = null;
		}
	
		return instance;
	}
	
	private static void invokeSetField(Class<?> clazz, Object instance, 
			String fieldName, Object value) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException {
		if (value != null) {
			Method method = clazz.getMethod(String.format("%s%s", 
					SET, StringUtils.capitalize(fieldName)), value.getClass());
			if (method != null) {
				method.invoke(instance, value);
			}
		}
	}
}
