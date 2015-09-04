package org.jcarvajal.framework.rest.controllers.marshallers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jcarvajal.framework.rest.controllers.ResponseMarshaller;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;

/**
 * This implementation of the response marshaller will map an entity into a simple text format.
 * 
 * @see @ResponseMapping documentation.
 * @author JoseCH
 *
 */
public class MappingCsvResponseMarshaller implements ResponseMarshaller {
	
	private static final String FIELD_FORMAT = "{%s}";
	private static final String DELIM = ",";
	
	private final String mapping;
	
	/**
	 * Initializes a new instance of the MappingCsvResponseMarshaller class.
	 * @param mapping
	 * @throws OnRequestMappingInitializationException
	 */
	public MappingCsvResponseMarshaller(String mapping) 
			throws OnRequestMappingInitializationException {
		if (!StringUtils.isNotEmpty(mapping)) {
			throw new OnRequestMappingInitializationException("Response mapping cannot be null!");
		}
		
		this.mapping = mapping;
	}
	
	/**
	 * Marshall an object or a list of objects.
	 */
	@SuppressWarnings("unchecked")
	public byte[] marshall(Object object) 
			throws OnRequestMappingInitializationException {
		byte[] result = null;
		if (object != null) {
			List<Field> fieldsPresent = resolveFieldPresent(object);
			if (!fieldsPresent.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				if (object instanceof Iterable) {
					// If it's a collection.
					marshallList((Iterable<Object>) object, fieldsPresent, buffer);
				} else {
					// Marshall only an item.
					marshallItem(object, fieldsPresent, buffer);
				}
				
				result = buffer.toString().getBytes();
			}
		}
		
		return result;
	}
	
	/**
	 * Resolve field presents in the response mapping.
	 * @param object
	 * @return
	 */
	private List<Field> resolveFieldPresent(Object object) {
		List<Field> fields = new ArrayList<Field>();
		
		Class<?> clazz = resolveItemClass(object);
		if (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (mapping.contains(getFieldTemplate(field.getName()))) {
					fields.add(field);
				}
			}
		}
		
		return fields;
	}
	
	@SuppressWarnings("unchecked")
	private Class<?> resolveItemClass(Object object) {
		Class<?> clazz = null;
		if (object instanceof Iterable) {
			Iterator<Object> it = ((Iterable<Object>) object).iterator();
			if (it.hasNext()) {
				Object item = it.next();
				if (item != null) {
					clazz = item.getClass();
				}
			}
		} else {
			clazz = object.getClass();
		}
		
		return clazz;
	}
	
	/**
	 * Marshall a list of objects.
	 * @param fieldsPresent
	 * @param item
	 * @return
	 * @throws OnRequestMappingInitializationException
	 */
	private void marshallList(Iterable<Object> list, List<Field> fieldsPresent, StringBuffer buffer) 
			throws OnRequestMappingInitializationException {
		
		if (list != null) {
			Iterator<Object> iter = list.iterator();
			while (iter.hasNext()) {
				if (buffer.length() > 0) {
					buffer.append(DELIM);
				}
				
				marshallItem(iter.next(), fieldsPresent, buffer);
			}
		}
	}
	
	/**
	 * Marshall only an item.
	 * @param fieldsPresent
	 * @param item
	 * @return
	 * @throws OnRequestMappingInitializationException
	 */
	private void marshallItem(Object item, List<Field> fieldsPresent, StringBuffer buffer) 
			throws OnRequestMappingInitializationException {		
		try {
			String itemText = mapping;
			
			for (Field field : fieldsPresent) {
				itemText = itemText.replaceAll(
						Pattern.quote(getFieldTemplate(field.getName())), 
						"" + ReflectionUtils.invokeGetField(item, field.getName()));
			}
			
			buffer.append(itemText);
		
		} catch (Exception e) {
			throw new OnRequestMappingInitializationException("Error in response marshall. Cause: %s", e.getMessage());
		} 
	}
	
	/**
	 * @param fieldName
	 * @return get field template.
	 */
	private String getFieldTemplate(String fieldName) {
		return String.format(FIELD_FORMAT, fieldName);
	}
}
