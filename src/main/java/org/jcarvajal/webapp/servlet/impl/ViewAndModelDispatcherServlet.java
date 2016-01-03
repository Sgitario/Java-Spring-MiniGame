package org.jcarvajal.webapp.servlet.impl;

import java.util.Map.Entry;

import org.jcarvajal.webapp.server.Response;
import org.jcarvajal.webapp.servlet.DispatcherServlet;

/**
 * Model view dispatcher servlet to handle views and model.
 * @author JoseCH
 *
 */
public class ViewAndModelDispatcherServlet extends DispatcherServlet {
	
	@Override
	protected Response handleResponse(Object response) throws Exception {
		
		Response result = new Response();
		result.setCode(200);
		
		if (response instanceof ViewAndModel) {
			ViewAndModel viewModel = (ViewAndModel) response;
			String viewContent = getView(viewModel.getViewName());
			
			for (Entry<String, String> attr : viewModel.getModel().entrySet()) {
				viewContent = viewContent.replaceAll("\\{\\{" + attr.getKey() + "\\}\\}", attr.getValue());
			}
			
			result.setCode(viewModel.getCode());
			result.setResponse(viewContent.getBytes());
		} else {
			if (response != null) {
				result.setResponse(response.toString().getBytes());
			}
		}
		
		return result;
	}
}
