package org.jcarvajal.framework.rest.servlet.controllers.annotations.params;

import java.io.InputStream;

/**
 * Resolve param value for request parameters.
 * @author JoseCH
 *
 */
public class RequestParamResolver extends ParamResolver {

	private final String START_WITH_FORMAT = "%s=";
	private final char END_WITH = '&';
	
	private final String attrName;
	
	public RequestParamResolver(String attrName, int position) {
		super(position);
		
		this.attrName = attrName;
	}

	@Override
	public Object resolve(String url, InputStream requestBody) {
		String value = null;
		if (url != null) {
			String startWith = String.format(START_WITH_FORMAT, this.attrName);
			int index = url.indexOf(startWith);
			if (index > 0) {
				StringBuffer buffer = new StringBuffer();
				index = index + startWith.length();
				while (index < url.length()) {
					char currentChar = url.charAt(index);
					if (currentChar == END_WITH) {
						break;
					}
					
					buffer.append(currentChar);
					
					index++;
				}
				
				value = buffer.toString();
			}
		}
		
		return value;
	}

}
