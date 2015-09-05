package org.jcarvajal.framework.utils;

import static org.junit.Assert.*;

import org.jcarvajal.framework.utils.StringUtils;
import org.junit.Test;

public class StringUtilsTest {
	
	private String value;
	private boolean actual;
	private String actualValue;
	private int actualCount;
	
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
	
	@Test
	public void countCharacters_whenCountTest_thenShouldReturn2() {
		givenString("test");
		whenCountCharacters('t');
		thenCountIs(2);
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
	
	private void whenCountCharacters(char character) {
		actualCount = StringUtils.countCharacters(value, character);
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
	
	private void thenCountIs(int expected) {
		assertEquals(expected, actualCount);
	}
}
