package org.jcarvajal.framework.rest.servlet.controllers.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.RequestMethod;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.RequestBody;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.RequestMapping;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.RequestParam;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.ResponseMapping;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.ParamResolver;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.PathVariableParamResolver;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.RequestParamResolver;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.RequestBodyParamResolver;
import org.jcarvajal.framework.utils.URLUtils;

public class AnnotationRequestHandler extends RequestHandler {

	private final RequestMapping requestMapping;
	
	public AnnotationRequestHandler(Object controller, Method method, 
			RequestMapping requestMapping) 
			throws OnRequestMappingInitializationException {
		super(controller, method);
		if (requestMapping == null) {
			throw new OnRequestMappingInitializationException("Request mapping cannot be null!");
		}
		
		this.requestMapping = requestMapping;
		
		// Response marshaller
		initResponseMarshaller(method);
	}
	
	@Override
	protected String getMappingUrl() {
		return requestMapping.url();
	}
	
	@Override
	protected RequestMethod getRequestMethod() {
		return requestMapping.method();
	}
	
	protected List<ParamResolver> initParams(String url, Method method) 
			throws OnRequestMappingInitializationException {
		List<ParamResolver> params = new ArrayList<ParamResolver>();
		Annotation[][] methodAnnotations = method.getParameterAnnotations();
		for (int index = 0; index < methodAnnotations.length; index++) {
			Annotation[] paramAnnotations = methodAnnotations[index];
			if (paramAnnotations.length > 0) {
				// Only support one annotation per parameter.
				Annotation annotation = paramAnnotations[0];
				
				// Resolve
				if (annotation.annotationType().equals(RequestParam.class)) {
					RequestParam annotationParam = (RequestParam) annotation;
					
					params.add(new RequestParamResolver(annotationParam.attr(), index));
				} else if (annotation.annotationType().equals(PathVariable.class)) {
					PathVariable annotationParam = (PathVariable) annotation;
					
					params.add(new PathVariableParamResolver(url, annotationParam.name(), index));
				} else if (annotation.annotationType().equals(RequestBody.class)) {
					params.add(new RequestBodyParamResolver(index));
				}
			}
		}
		
		return params;
	}
	
	protected String initRegExpUrl(String url) {
		String regExp = url;
		
		// Discard ?X=Y,Z=H... params
		regExp = URLUtils.removeParamsSection(regExp);
		
		// Replace {...} by anything
		regExp = regExp.replaceAll("\\{[a-zA-Z0-9]+\\}", ".+");
		
		return "^" + regExp + "(\\?.+)?";
	}
	
	private void initResponseMarshaller(Method method) 
			throws OnRequestMappingInitializationException {
		if (method.isAnnotationPresent(ResponseMapping.class)) {
			ResponseMapping responseMapping = method.getAnnotation(ResponseMapping.class);
			setMarshaller(new MappingCsvResponseMarshaller(responseMapping.map()));
		}
	}

}
