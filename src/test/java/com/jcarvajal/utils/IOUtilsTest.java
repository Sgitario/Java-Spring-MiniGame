package com.jcarvajal.utils;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.jcarvajal.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {
	
	private InputStream is;
	
	@Before
	public void setup() {
		is = mock(InputStream.class);
	}
	
	@Test
	public void close_whenNotNull_thenCloseInvoked() throws IOException {
		whenClose();
		thenCloseInvoked();
	}
	
	private void whenClose() {
		IOUtils.close(is);
	}
	
	private void thenCloseInvoked() throws IOException {
		verify(is, times(1)).close();
	}
}
