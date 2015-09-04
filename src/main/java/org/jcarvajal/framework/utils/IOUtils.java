package org.jcarvajal.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Utility class for IO operations.
 * @author JoseCH
 *
 */
public class IOUtils {
	
	private static final Logger LOG = Logger.getLogger(
			IOUtils.class.getName());
	
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
	
	/**
	 * Read the input stream into a String. 
	 * @param is
	 * @return
	 */
	public static String toString(InputStream is) {
		String data = null;
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder out = new StringBuilder();
	        String line;
	        try {
				while ((line = reader.readLine()) != null) {
				    out.append(line);
				}
			} catch (IOException e) {
				LOG.warning("Error reading from the input stream");
			}
	        
	        data = out.toString();
		}
        
        return data;
	}
}
