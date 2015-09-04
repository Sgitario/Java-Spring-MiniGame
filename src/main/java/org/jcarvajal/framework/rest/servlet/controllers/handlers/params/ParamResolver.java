package org.jcarvajal.framework.rest.servlet.controllers.handlers.params;

import java.io.OutputStream;

public abstract class ParamResolver {
	private final int position;
	public ParamResolver(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
	
	public abstract Object resolve(String url, OutputStream responseBody);
}
