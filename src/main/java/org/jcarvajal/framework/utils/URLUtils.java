package org.jcarvajal.framework.utils;

public class URLUtils {
	private static final String PARAM_DELIM = "?";
	
	public static final String removeParamsSection(String url) {
		String result = url;
		int paramDelimIndex = result.indexOf(PARAM_DELIM);
		if (paramDelimIndex >= 0) {
			result = result.substring(0, paramDelimIndex);
		}
		
		return result;
	}
}
