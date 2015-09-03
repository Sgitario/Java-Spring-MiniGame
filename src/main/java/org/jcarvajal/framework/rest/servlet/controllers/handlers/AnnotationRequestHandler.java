package org.jcarvajal.framework.rest.servlet.controllers.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jcarvajal.framework.rest.annotations.PathVariable;
import org.jcarvajal.framework.rest.annotations.RequestMapping;
import org.jcarvajal.framework.rest.annotations.RequestParam;
import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.RequestMethod;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.ParamResolver;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.PathVariableParamResolver;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.RequestParamResolver;
import org.jcarvajal.framework.utils.URLUtils;

public class AnnotationRequestHandler implements RequestHandler {
	
	private final Object controller;
	private final Method method;
	private final RequestMethod requestMethod;
	private final String originalUrl;
	private final String regExpUrl;
	private final ResponseMarshaller marshaller = new StringResponseMarshaller();
	private List<ParamResolver> params = new ArrayList<ParamResolver>();
	
	public AnnotationRequestHandler(Object controller, Method method) throws OnRequestMappingInitializationException {
		if (controller == null 
				|| !method.isAnnotationPresent(RequestMapping.class)) {
			throw new OnRequestMappingInitializationException("Method is not a request mapping!");
		}
		
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);		
		this.controller = controller;
		this.method = method;
		this.originalUrl = requestMapping.url();
		this.requestMethod = requestMapping.method();
		this.regExpUrl = registerUrl(this.originalUrl, method);
		this.params = registerParams(method);
	}

	public boolean satisfy(String url, String method) {
		return url != null && url.matches(regExpUrl) && this.requestMethod.equals(method);
	}

	public Object invoke(String url) throws OnRequestException {
		Object response = null;
		try {
			Object[] params = resolveParams(url);
			
			response = this.method.invoke(controller, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OnRequestException("Error handling request. ", e);
		}
		
		return response;
	}
	
	private String registerUrl(String url, Method method) {
		String regExp = url;
		
		// Discard ?X=Y,Z=H... params
		regExp = URLUtils.removeParamsSection(regExp);
		
		// Replace {...} by anything
		regExp = regExp.replaceAll("\\{[a-zA-Z0-9]+\\}", ".+");
		
		return "^" + regExp + "(\\?.+)?";
	}
	
	private List<ParamResolver> registerParams(Method method) 
			throws OnRequestMappingInitializationException {
		List<ParamResolver> params = new ArrayList<ParamResolver>();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int index = 0; index < annotations.length; index++) {
			Annotation[] annotation = annotations[index];
			if (annotation.length > 0) {
				if (annotation[0].annotationType().equals(RequestParam.class)) {
					RequestParam annotationParam = (RequestParam) annotation[0];
					
					params.add(new RequestParamResolver(annotationParam.attr(), index));
				} else if (annotation[0].annotationType().equals(PathVariable.class)) {
					PathVariable annotationParam = (PathVariable) annotation[0];
					
					params.add(new PathVariableParamResolver(originalUrl, annotationParam.name(), index));
				}
			}
		}
		
		return params;
	}
	
	private Object[] resolveParams(String url) {
		Object[] paramValues = new Object[params.size()];
		for (ParamResolver paramResolver : params) {
			paramValues[paramResolver.getPosition()] = paramResolver.resolve(url);
		}
		
		return paramValues;
	}

}
