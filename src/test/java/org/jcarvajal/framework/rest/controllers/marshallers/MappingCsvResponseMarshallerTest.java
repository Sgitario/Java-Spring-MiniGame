package org.jcarvajal.framework.rest.controllers.marshallers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcarvajal.framework.rest.controllers.marshallers.MappingCsvResponseMarshaller;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.junit.Test;

public class MappingCsvResponseMarshallerTest {
	
	private String mapping;
	private Object param;
	private String actualResponse;
	
	@Test
	public void marshall_whenMarshallAList_thenOutputShouldMatch() 
			throws OnRequestMappingInitializationException {
		givenMapping("-{prop2} vs {prop1} vs {prop2}-");
		givenList(new Item("A1", "B1"),
				new Item("A2", "B2"),
				new Item("A3", "B3"));
		whenMarshall();
		thenResponseIs("-B1 vs A1 vs B1-,-B2 vs A2 vs B2-,-B3 vs A3 vs B3-");
	}
	
	@Test
	public void marshall_whenMarshallAnItem_thenOutputShouldMatch() 
			throws OnRequestMappingInitializationException {
		givenMapping("-{prop2} vs {prop1} vs {prop2}-");
		givenItem(new Item("A1", "B1"));
		whenMarshall();
		thenResponseIs("-B1 vs A1 vs B1-");
	}
	
	private void givenList(Item... items) {
		List<Item> list = new ArrayList<Item>();
		if (items != null) {
			list.addAll(Arrays.asList(items));
		}
		
		param = list;
	}
	
	private void givenItem(Item item) {
		param = item;
	}
	
	private void givenMapping(String mapping) {
		this.mapping = mapping;
	}
	
	private void whenMarshall() throws OnRequestMappingInitializationException {
		MappingCsvResponseMarshaller marshaller = new MappingCsvResponseMarshaller(this.mapping);
		this.actualResponse = new String(marshaller.marshall(param));
	}
	
	private void thenResponseIs(String expected) {
		assertEquals(expected, this.actualResponse);
	}
	
	public class Item {
		private String prop1;
		private String prop2;
		
		public Item(String prop1, String prop2) {
			this.prop1 = prop1;
			this.prop2 = prop2;
		}
		
		public String getProp1() {
			return prop1;
		}
		
		public String getProp2() {
			return prop2;
		}
	}
}
