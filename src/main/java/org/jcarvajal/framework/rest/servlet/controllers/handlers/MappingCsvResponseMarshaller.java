package org.jcarvajal.framework.rest.servlet.controllers.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;

public class MappingCsvResponseMarshaller implements ResponseMarshaller {
	
	private static final String FIELD_FORMAT = "{%s}";
	private static final String DELIM = ",";
	
	private final String mapping;
	
	public MappingCsvResponseMarshaller(String mapping) 
			throws OnRequestMappingInitializationException {
		if (!StringUtils.isNotEmpty(mapping)) {
			throw new OnRequestMappingInitializationException("Response mapping cannot be null!");
		}
		
		this.mapping = mapping;
	}
	
	@SuppressWarnings("unchecked")
	public byte[] marshall(Object object) 
			throws OnRequestMappingInitializationException {
		byte[] result = null;
		if (object != null) {
			List<Field> fieldsPresent = resolveFieldPresent(object);
			StringBuffer buffer = new StringBuffer();
			if (object instanceof Iterable) {
				Iterable<Object> iterable = (Iterable<Object>) object;
				Iterator<Object> iter = iterable.iterator();
				while (iter.hasNext()) {
					if (buffer.length() > 0) {
						buffer.append(DELIM);
					}
					
					buffer.append(marshallItem(fieldsPresent, iter.next()));
				}
			} else {
				buffer.append(marshallItem(fieldsPresent, object));
			}
			
			result = buffer.toString().getBytes();
		}
		
		return result;
	}
	
	private List<Field> resolveFieldPresent(Object object) {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : object.getClass().getDeclaredFields()) {
			if (mapping.contains(getFieldTemplate(field.getName()))) {
				fields.add(field);
			}
		}
		
		return fields;
	}

	private String marshallItem(List<Field> fieldsPresent, Object item) 
			throws OnRequestMappingInitializationException {
		String itemResult = mapping;
		
		try {
			for (Field field : fieldsPresent) {
				itemResult = itemResult.replaceAll(
						Pattern.quote(getFieldTemplate(field.getName())), 
						"" + ReflectionUtils.invokeGetField(item, field.getName()));
			}
		
		} catch (Exception e) {
			throw new OnRequestMappingInitializationException("Error in response marshall. Cause: %s", e.getMessage());
		} 
		
		return itemResult;
	}
	
	private String getFieldTemplate(String fieldName) {
		return String.format(FIELD_FORMAT, fieldName);
	}
}
