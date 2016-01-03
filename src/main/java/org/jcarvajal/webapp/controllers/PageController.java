package org.jcarvajal.webapp.controllers;

import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.RequestMethod;
import org.jcarvajal.webapp.servlet.controllers.impl.RequestMapping;
import org.jcarvajal.webapp.servlet.controllers.impl.Role;
import org.jcarvajal.webapp.servlet.impl.ViewAndModel;

public class PageController {
	
	@RequestMapping(url = "/page1", method = RequestMethod.GET)
	@Role(name = "PAG_1")
	public ViewAndModel getPage1(RequestContext request) {
		ViewAndModel viewModel = new ViewAndModel();
		viewModel.setViewName("page1");
		viewModel.put("username", request.getPrincipal());
		
		return viewModel;
	}
	
	@RequestMapping(url = "/page2", method = RequestMethod.GET)
	@Role(name = "PAG_2")
	public ViewAndModel getPage2(RequestContext request) {
		ViewAndModel viewModel = new ViewAndModel();
		viewModel.setViewName("page2");
		viewModel.put("username", request.getPrincipal());
		
		return viewModel;
	}
	
	@RequestMapping(url = "/page3", method = RequestMethod.GET)
	@Role(name = "PAG_3")
	public ViewAndModel getPage3(RequestContext request) {
		ViewAndModel viewModel = new ViewAndModel();
		viewModel.setViewName("page3");
		viewModel.put("username", request.getPrincipal());
		
		return viewModel;
	}

}
