package org.jcarvajal.webapp.servlet.impl;

import java.util.HashMap;
import java.util.Map;

public class ViewAndModel {
	private int code;
	private String viewName;
	private Map<String, String> model = new HashMap<String, String>();
	
	public String getViewName() {
		return viewName;
	}
	
	public void setViewName(String view) {
		this.viewName = view;
	}
	
	public Map<String, String> getModel() {
		return model;
	}
	
	public void put(String key, String value) {
		model.put(key, value);
	}

	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
}
