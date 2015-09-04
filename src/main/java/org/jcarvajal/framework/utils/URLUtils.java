package org.jcarvajal.framework.utils;

/**
 * Utility class for URL operations.
 * @author JoseCH
 *
 */
public class URLUtils {
	private static final String PARAM_DELIM = "?";
	
	/**
	 * Remove the params section (?attr=value..) from the URL.
	 * @param url
	 * @return
	 */
	public static final String removeParamsSection(String url) {
		String result = url;
		int paramDelimIndex = result.indexOf(PARAM_DELIM);
		if (paramDelimIndex >= 0) {
			result = result.substring(0, paramDelimIndex);
		}
		
		return result;
	}
}
