package org.jcarvajal.framework.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	/**
	 * Close a stream silently.
	 * @param is
	 */
	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				
			}
		}
	}
}
