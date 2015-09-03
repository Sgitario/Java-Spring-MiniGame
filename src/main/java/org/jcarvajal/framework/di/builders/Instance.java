package org.jcarvajal.framework.di.builders;

import java.util.Map;

import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.di.exceptions.InstantiationException;

public abstract class Instance {
	
	private Class<?> bindClazz;
	private String implClazz;
	private Map<String, String> params;
	private Object instance;
	
	public Object instance() {
		return instance;
	}
	
	public String getImplClazz() {
		return implClazz;
	}
	
	public String getBindClazzName() {
		return bindClazz.getName();
	}
	
	public void onInit() throws InstantiationException {
		
	}
	
	public void onInvoked()throws InstantiationException {
		
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
	}
}
