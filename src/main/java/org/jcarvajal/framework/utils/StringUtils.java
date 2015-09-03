package org.jcarvajal.framework.utils;

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

	/**
	 * Count the number of characters in the string.
	 * Example:
	 * str= test
	 * character= 't'
	 * 
	 * result = 2
	 * 
	 * @param str
	 * @param character
	 * @return
	 */
	public static int countCharacters(String str, char character) {
		int count = 0;
		if (isNotEmpty(str)) {
			for (int index = 0; index < str.length(); index++) {
				if (str.charAt(index) == character) {
					count++;
				}
			}
		}
		
		return count;
	}
}
