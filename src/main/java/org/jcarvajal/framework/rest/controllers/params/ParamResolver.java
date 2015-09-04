package org.jcarvajal.framework.rest.controllers.params;

import java.io.InputStream;

/**
 * The ParamResolver class will keep the position in the request method
 * and will resolve the param value depending to the ParamResolver implementation.
 * 
 * @author JoseCH
 *
 */
public abstract class ParamResolver {
	private final int position;
	
	/**
	 * Initializes a new instance of the ParamResolver class.
	 * @param position
	 */
	public ParamResolver(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
	
	/**
	 * Resolve param value.
	 * @param url
	 * @param responseBody
	 * @return
	 */
	public abstract Object resolve(String url, InputStream requestBody);
}
