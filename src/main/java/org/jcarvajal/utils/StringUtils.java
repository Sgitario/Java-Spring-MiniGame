package org.jcarvajal.utils;

public class StringUtils {
	/**
	 * Check whether a string is empty or not.
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * Capitalize a string.
	 * @param fieldName
	 * @return
	 */
	public static String capitalize(String fieldName) {
		String val = fieldName;
		
		if (isNotEmpty(fieldName)) {
			String first = "" + fieldName.charAt(0);
			val = first.toUpperCase() + fieldName.substring(1);
		}
		
		return val;
	}
}
