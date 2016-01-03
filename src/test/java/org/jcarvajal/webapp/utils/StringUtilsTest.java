package org.jcarvajal.webapp.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {
	
	private String value;
	private boolean actual;
	
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
	
	private void givenString(String val) {
		value = val;
	}
	
	private void whenIsNotEmpty() {
		actual = StringUtils.isNotEmpty(value);
	}
	
	private void thenIsEmpty() {
		assertFalse(actual);
	}
	
	private void thenIsNotEmpty() {
		assertTrue(actual);
	}
}
