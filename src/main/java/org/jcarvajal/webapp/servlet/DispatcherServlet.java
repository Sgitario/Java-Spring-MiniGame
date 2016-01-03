package org.jcarvajal.webapp.servlet;

import java.util.List;

import org.jcarvajal.webapp.utils.IOUtils;
import org.jcarvajal.webapp.utils.ImmutableObservable;
import org.jcarvajal.webapp.utils.StringUtils;
import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.Response;
import org.jcarvajal.webapp.servlet.controllers.ControllerManager;
import org.jcarvajal.webapp.servlet.controllers.handlers.RequestHandler;
import org.jcarvajal.webapp.servlet.controllers.impl.AnnotationControllerManager;

/**
 * Dispatcher base class to register controllers.
 * 
 * @author JoseCH
 *
 */
public abstract class DispatcherServlet extends ImmutableObservable {	
	private static final String REDIRECT = "redirect";
	
	private String viewPath;
	private String redirectToLogin;
	private ControllerManager controllerManager = new AnnotationControllerManager();
	
	public DispatcherServlet setViewPath(String viewPath) {
		this.viewPath = viewPath;
		
		return this;
	}
	
	public DispatcherServlet setRedirectToLogin(String redirectToLogin) {
		this.redirectToLogin = redirectToLogin;
		
		return this;
	}
	
	public DispatcherServlet setControllerManager(ControllerManager controllerManager) {
		this.controllerManager = controllerManager;
		
		return this;
	}
	
	/**
	 * Handle the response into a Response entity.
	 * @param response
	 * @return
	 */
	protected abstract Response handleResponse(Object response) throws Exception;
	
	/**
	 * Handle an incoming HTTP request and returns a response.
	 * @return
	 * @throws Exception 
	 * @throws OnRequestException 
	 */
	public final Response handle(RequestContext context) throws Exception {		
		RequestHandler handler = controllerManager.getHandler(
				context.getRequestURI().toString(), 
				context.getRequestMethod());
		if (handler != null) {
			// Notify observers
			notifyObservers(context);
			
			// Authorization
			Response redirect = checkAuthorization(handler, context);
			if (redirect != null) {
				return redirect;
			}
			
			// Handle response
			return handleResponseInternal(context, handler.invoke(context));			
		}
		
		return null;
	}

	/**
	 * Init controllers.
	 * @param controllers
	 */
	public final void initControllers(List<Object> controllers) {
		if (controllers != null) {
			for (Object controller : controllers) {
				controllerManager.register(controller);
			}
		}
	}
	
	/**
	 * Parse the target view.
	 * @param viewName
	 * @return
	 */
	protected String getView(String viewName) {
		return IOUtils.toString(this.getClass().getResourceAsStream(viewPath + "/" + viewName + ".html"));
	}
	
	/**
	 * Handle the response redirecting to the proper page if required.
	 * @param context
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private Response handleResponseInternal(RequestContext context, Object response) 
			throws Exception {
		
		String redirectIfAuth = context.getCookie(REDIRECT);
		if (StringUtils.isNotEmpty(redirectIfAuth) && context.getPrincipal() != null) {
			Response redirect = new Response();
			redirect.setRedirect(redirectIfAuth);
			context.clearCookie(REDIRECT);
			return redirect;
		} else {
			return handleResponse(response);
		}
	}
	
	/**
	 * Chech whether the principal user has credentials to view the request.
	 * @param handler
	 * @param context
	 * @return
	 */
	private Response checkAuthorization(RequestHandler handler, RequestContext context) {
		Response response = null;
		if (handler.requiresRole()) {
			if (context.getPrincipal() == null) {
				// go to login
				response = new Response();
				response.setRedirect(redirectToLogin);
				
				if (context.getRequestURI() != null) {
					context.setCookie(REDIRECT, context.getRequestURI().getPath());
				}
			} else if (!handler.getRequiredRole().equals(context.getUserRole())) {
				response = new Response();
				response.setCode(403);
			}
		}
		
		return response;
	}
}
