package org.jcarvajal.framework.rest.controllers.params;

import java.io.OutputStream;

/**
 * Resolve param value for request body (POST requests).
 * @author JoseCH
 *
 */
public class RequestBodyParamResolver extends ParamResolver {

	public RequestBodyParamResolver(int position) {
		super(position);
	}

	@Override
	public Object resolve(String url, OutputStream responseBody) {
		Integer value = null;
		if (responseBody != null) {
			String body = responseBody.toString();
			
			value = Integer.valueOf(body);
		}
		
		return value;
	}

}
