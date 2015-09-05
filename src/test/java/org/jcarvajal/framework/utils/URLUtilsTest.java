package org.jcarvajal.framework.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class URLUtilsTest {
	
	private String url;
	private String actual;
	
	@Test
	public void removeParamsSection_whenUrl_thenParamsShouldBeRemoved() {
		givenUrl("/test/test12?param=1&param2=3");
		whenRemoveParamsSection();
		thenUrlIs("/test/test12");
	}
	
	private void givenUrl(String url) {
		this.url = url;
	}
	
	private void whenRemoveParamsSection() {
		this.actual = URLUtils.removeParamsSection(url);
	}
	
	private void thenUrlIs(String expected) {
		assertEquals(expected, this.actual);
	}
}
