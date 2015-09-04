package org.jcarvajal.framework.rest.servlet.controllers.handlers.params;

import java.io.OutputStream;

public class RequestBodyParamResolver extends ParamResolver {

	public RequestBodyParamResolver(int position) {
		super(position);
	}

	@Override
	public Object resolve(String url, OutputStream responseBody) {
		return responseBody.toString();
	}

}
