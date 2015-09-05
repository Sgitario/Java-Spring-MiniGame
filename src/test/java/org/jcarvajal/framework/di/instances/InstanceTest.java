package org.jcarvajal.framework.di.instances;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.junit.Before;
import org.junit.Test;

/**
 * We're using InitializationInstance class to test Instance class.
 * @author JoseCH
 *
 */
public class InstanceTest {
	
	private static final String PARAMETER_VALUE = "VALUE";
	
	private DependencyInjectorBase mockInjector;
	
	private InitializationInstance instance;
	
	@Before
	public void setup() {
		mockInjector = mock(DependencyInjectorBase.class);
	}
	
	/**
	 * When create instance with autowired, params and init annotations.
	 * Then the instance should be properly init.
	 * @throws InstantiationException
	 */
	@Test
	public void bind_whenClassHasAutowired_thenAutowiredAreCreated() 
			throws InstantiationException {
		givenCalculatedClassRegistered();
		whenCreateInstance();
		thenInjectorReturnsExpected();
	}
	
	private void givenCalculatedClassRegistered() {
		when(mockInjector.get(Calculated.class.getName())).thenReturn(new Calculated());
	}
	
	private void whenCreateInstance() throws InstantiationException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parameter", PARAMETER_VALUE);
		
		instance = new InitializationInstance(
				Interface.class.getName(), 
				Implementation.class.getName(), 
				params, 
				mockInjector);
	}
	
	private void thenInjectorReturnsExpected() {
		Implementation actual = (Implementation) instance.instance();
		assertNotNull(actual);
		assertEquals(PARAMETER_VALUE, actual.getParameter());
		assertNotNull(actual.getCalculated());
		assertEquals(1, actual.getTimesRunMethodCalled());
	}
}
