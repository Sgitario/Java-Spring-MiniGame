package com.jcarvajal.framework.utils;

import static org.junit.Assert.*;

import org.jcarvajal.framework.utils.StringUtils;
import org.junit.Test;

public class StringUtilsTest {
	
	private String value;
	private boolean actual;
	private String actualValue;
	
	@Test
	public void isNotEmpty_whenNull_thenReturnTrue() {
		givenString(null);
		whenIsNotEmpty();
		thenIsEmpty();
	}
	
	@Test
	public void isNotEmpty_whenEmpty_thenReturnTrue() {
		givenString("");
		whenIsNotEmpty();
		thenIsEmpty();
	}
	
	@Test
	public void isNotEmpty_whenOnlySpaces_thenReturnTrue() {
		givenString("  ");
		whenIsNotEmpty();
		thenIsEmpty();
	}
	
	@Test
	public void isNotEmpty_whenLetters_thenReturnFalse() {
		givenString(" aa ");
		whenIsNotEmpty();
		thenIsNotEmpty();
	}
	
	@Test
	public void capitalize_whenCapitalizeEmpty_thenReturnSame() {
		givenString("");
		whenCapitalize();
		thenResultIs("");
	}
	
	@Test
	public void capitalize_whenCapitalize_thenExpectedReturn() {
		givenString("test");
		whenCapitalize();
		thenResultIs("Test");
	}
	
	private void givenString(String val) {
		value = val;
	}
	
	private void whenIsNotEmpty() {
		actual = StringUtils.isNotEmpty(value);
	}
	
	private void whenCapitalize() {
		actualValue = StringUtils.capitalize(value);
	}
	
	private void thenIsEmpty() {
		assertFalse(actual);
	}
	
	private void thenIsNotEmpty() {
		assertTrue(actual);
	}
	
	private void thenResultIs(String expected) {
		assertEquals(expected, actualValue);
	}
}
