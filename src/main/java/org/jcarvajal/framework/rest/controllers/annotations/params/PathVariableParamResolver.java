package org.jcarvajal.framework.rest.controllers.annotations.params;

import java.io.InputStream;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.utils.StringUtils;
import org.jcarvajal.framework.utils.URLUtils;

/**
 * Resolve param value for path variables.
 * @author JoseCH
 *
 */
public class PathVariableParamResolver extends ParamResolver {

	private static final String FORMAT = "{%s}";
	private static final char PATH_DELIM = '/';
	
	private final int indexPathInUrl;
	
	public PathVariableParamResolver(String originalUrl, String name, int position) 
			throws OnRequestMappingInitializationException {
		super(position);
		
		int pathIndex = originalUrl.indexOf(String.format(FORMAT, name));
		if (pathIndex >= 0) {
			indexPathInUrl = StringUtils.countCharacters(originalUrl.substring(0, pathIndex), PATH_DELIM);
		} else {
			throw new OnRequestMappingInitializationException("Path variable %s not found in %s.", name, originalUrl);
		}
	}

	@Override
	public Object resolve(String url, InputStream requestBody) {
		String[] paths = URLUtils.removeParamsSection(url)
				.split("" + PATH_DELIM);
		
		return Integer.valueOf(paths[indexPathInUrl]);
	}

}