package org.jcarvajal.framework.rest.controllers.params;

import java.io.InputStream;

import org.jcarvajal.framework.utils.IOUtils;

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
	public Object resolve(String url, InputStream requestBody) {
		Integer value = null;
		String body = IOUtils.toString(requestBody);
		if (body != null) {
			value = Integer.valueOf(body);
		}
		
		return value;
	}

}
