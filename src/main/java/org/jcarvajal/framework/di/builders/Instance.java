package org.jcarvajal.framework.di.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.di.annotations.Init;
import org.jcarvajal.framework.di.exceptions.InstantiationException;

public abstract class Instance {
	
	private Class<?> bindClazz;
	private String implClazz;
	private Map<String, String> params;
	private Object instance;
	private final DependencyInjectorBase injector;
	
	/**
	 * Initializes a new instance of the Instance class.
	 * @param injector
	 */
	public Instance(DependencyInjectorBase injector) {
		this.injector = injector;
	}
	
	public Object instance() {
		return instance;
	}
	
	public String getImplClazz() {
		return implClazz;
	}
	
	public Class<?> getBindClazz() {
		return bindClazz;
	}
	
	public String getBindClazzName() {
		return bindClazz.getName();
	}
	
	public void onInit() throws InstantiationException {
		
	}
	
	public void onInvoked() throws InstantiationException {
		
	}
	
	public void bind(Class<?> bindClazz, String implClazz, Map<String, String> params) throws InstantiationException {
		this.bindClazz = bindClazz;
		this.implClazz = implClazz;
		this.params = params;
		
		onInit();
	}
	
	protected void initializeInstance() throws InstantiationException {
		this.instance = ReflectionUtils.createInstance(implClazz, bindClazz, params);
		if (this.instance == null) {
			throw new InstantiationException(this);
		}
		
		resolveAutowiredFields();
		runInitMethods();
	}
	
	private void resolveAutowiredFields() throws InstantiationException {
		if (this.instance != null) {
			Field[] fields = this.instance.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Autowired.class)) {
					// Find
					Object value = this.injector.get(field.getType().getName());
					if (value == null) {
						throw new InstantiationException(
								String.format("Field %s cannot be resolved. ", field.getName()), 
								this);
					}
					
					// Set
					try {
						ReflectionUtils.invokeSetField(this.instance, field.getName(), value, field.getType());
					} catch (Exception e) {
						throw new InstantiationException(
								String.format("Field %s has not set method or is not visible. ", field.getName()), 
								this);
					}
				}
			}
		}
	}
	
	private void runInitMethods() throws InstantiationException {
		if (this.instance != null) {
			Method[] methods = this.instance.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Init.class)) {					
					// Set
					try {
						ReflectionUtils.invokeMethod(this.instance, method);
					} catch (Exception e) {
						throw new InstantiationException(
								String.format("Method %s cannot be executed at init stage. The method should be public and no-args. ", method.getName()), 
								this);
					}
				}
			}
		}
	}
}
