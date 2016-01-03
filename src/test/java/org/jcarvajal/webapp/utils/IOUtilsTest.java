package org.jcarvajal.webapp.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {
	
	private InputStream mockIs;
	private InputStream realIs;
	
	private String actualToString;
	
	@Before
	public void setup() {
		mockIs = mock(InputStream.class);
	}
	
	@After
	public void after() { 
		if (realIs != null) {
			try {
				realIs.close();
			} catch (IOException e) {
			}
		}
	}
	
	@Test
	public void toString_whenInputStream_thenResultExpected() throws IOException {
		givenInputStream("This is the content");
		whenToString();
		thenResultIs("This is the content");
	}
	
	@Test
	public void close_whenNotNull_thenCloseInvoked() throws IOException {
		whenClose();
		thenCloseInvoked();
	}
	
	private void givenInputStream(String str) {
	    this.realIs = new ByteArrayInputStream(str.getBytes());
	}
	
	private void whenToString() {
		actualToString = IOUtils.toString(realIs);
	}
	
	private void whenClose() {
		IOUtils.close(mockIs);
	}
	
	private void thenCloseInvoked() throws IOException {
		verify(mockIs, times(1)).close();
	}
	
	private void thenResultIs(String expected) {
		assertEquals(expected, actualToString);
	}
}
