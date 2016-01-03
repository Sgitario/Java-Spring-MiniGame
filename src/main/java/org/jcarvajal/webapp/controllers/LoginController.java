package org.jcarvajal.webapp.controllers;

import java.util.Map;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.services.UserService;
import org.jcarvajal.webapp.utils.IOUtils;
import org.jcarvajal.webapp.utils.StringUtils;
import org.jcarvajal.webapp.filters.security.AuthenticationProviderFilter;
import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.RequestMethod;
import org.jcarvajal.webapp.servlet.controllers.impl.RequestMapping;
import org.jcarvajal.webapp.servlet.impl.ViewAndModel;

public class LoginController {
	
	private UserService userService;
	
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(url = "/login", method = RequestMethod.GET)
	public ViewAndModel getLoginPage(RequestContext request) {
		ViewAndModel viewModel = new ViewAndModel();
		if (request.getPrincipal() != null) {
			viewModel.setViewName("index");
		} else {
			viewModel.setViewName("loginPage");
		}
		
		return viewModel;
	}
	
	@RequestMapping(url = "/login", method = RequestMethod.POST)
	public ViewAndModel postLogin(RequestContext request) {
		// Parse request
		Map<String, String> params 
			= StringUtils.map(IOUtils.toString(request.getRequestBody()), "&", "=");
		
		String user = params.get("user");
		String pass = params.get("pass");
		
		// Login
		AccessToken accessToken = this.userService.loginUser(user, pass);
		
		// Process
		ViewAndModel viewModel = new ViewAndModel();
		if (accessToken != null) {
			request.setPrincipal(user, accessToken.getUser().getRole());
			request.setCookie(AuthenticationProviderFilter.TOKEN, accessToken.getToken());
			
			viewModel.setViewName("index");
		} else {
			viewModel.setCode(401);
			viewModel.put("message", "User " + user + " not found");
			viewModel.setViewName("unauthorized");
		}
		
		return viewModel;
	}
	
	@RequestMapping(url = "/logout", method = RequestMethod.POST)
	public ViewAndModel logout(RequestContext request) {
		
		String token = request.clearCookie(AuthenticationProviderFilter.TOKEN);
		userService.deleteAccessToken(token);
		
		ViewAndModel viewModel = new ViewAndModel();
		viewModel.setViewName("loginPage");
		
		return viewModel;
	}
	
	
}
