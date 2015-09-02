package org.jcarvajal.marti.builders;

import java.util.Map;

import org.jcarvajal.utils.ReflectionUtils;

public abstract class Instance {
	
	private Class<?> bindClazz;
	private String implClazz;
	private Map<String, String> params;
	private Object instance;
	
	public Object instance() {
		return instance;
	}
	
	public void onInit() {
		
	}
	
	public void onInvoked() {
		
	}
	
	public void bind(Class<?> bindClazz, String implClazz, Map<String, String> params) {
		this.bindClazz = bindClazz;
		this.implClazz = implClazz;
		this.params = params;
		
		onInit();
	}
	
	protected void initializeInstance() {
		this.instance = ReflectionUtils.createInstance(implClazz, bindClazz, params);
	}
}
