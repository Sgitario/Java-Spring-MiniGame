package org.jcarvajal.webapp.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
	
	/**
	 * Check whether a string is empty or not.
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}
	
	public static Map<String, String> map(String text, String paramSep, String fieldSep) {
		Map<String, String> params = new HashMap<String, String>();
		if (text != null) {
			String[] paramsSplit = text.split(paramSep);
			for (String param : paramsSplit) {
				String[] fields = param.split(fieldSep);
				String value = null;
				if (fields.length > 1) {
					value = fields[1];
				}
				
				params.put(fields[0].trim(), value);
			}
		}
		
		return params;
	}
}
