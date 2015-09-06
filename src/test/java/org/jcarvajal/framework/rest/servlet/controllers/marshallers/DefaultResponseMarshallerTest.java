package org.jcarvajal.framework.rest.servlet.controllers.marshallers;

import static org.junit.Assert.*;

import org.jcarvajal.framework.rest.servlet.controllers.marshallers.DefaultResponseMarshaller;
import org.junit.Before;
import org.junit.Test;

public class DefaultResponseMarshallerTest {
	private DefaultResponseMarshaller marshaller;
	
	private Object request;
	private byte[] response;
	
	@Before
	public void setup() {
		marshaller = new DefaultResponseMarshaller();
	}
	
	@Test
	public void marshall_givenInteger_thenExpected() {
		givenRequest(4);
		whenMarshall();
		thenResponse("4");
	}
	
	@Test
	public void marshall_givenString_thenExpected() {
		givenRequest("Test1");
		whenMarshall();
		thenResponse("Test1");
	}
	
	private void givenRequest(Object request) {
		this.request = request;
	}
	
	private void whenMarshall() {
		response = marshaller.marshall(request);
	}
	
	private void thenResponse(String expected) {
		assertEquals(expected, new String(response));
	}
}
