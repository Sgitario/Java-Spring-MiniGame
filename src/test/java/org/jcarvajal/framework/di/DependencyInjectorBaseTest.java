package org.jcarvajal.framework.di;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.junit.Before;
import org.junit.Test;

public class DependencyInjectorBaseTest {
	
	private static final String MODIFY = "MODIFIED";
	
	private DependencyInjectorBase injector;
	
	private Collection<Object> actualInstance;
	private List<Dependency> expectedDependencies;
	
	@Before
	public void setup() {
		expectedDependencies = new ArrayList<Dependency>();
		
		injector = spy(new DummyDependencyInjectorBase());
	}
	
	/**
	 * When a class is trying to be injected and it's not found, the injector
	 * should try to create it. 
	 * When this class is an interface, so it cannot be instantiated.
	 * Then internally there will be an exception and the injector will return null.
	 */
	@Test
	public void get_whenClassNotRegisteredAndCannotBeCreated_thenReturnNull() {
		whenGetAList();
		thenInstanceIsNull();
	}
	
	/**
	 * When a class is trying to be injected and it's not found, the injector
	 * should try to create it. 
	 * When this class is an concrete class with no constructors, so it can be instantiated.
	 * Then the injector will return a new instance of this class.
	 */
	@Test
	public void get_whenClassNotRegisteredAndCanBeCreated_thenReturnNotNull() {
		whenGetAnArrayList();
		thenInstanceIsNotNull();
	}
	
	/**
	 * When a class is trying to be injected and it's not found, the injector
	 * should try to create it. 
	 * When this class is an concrete class with no constructors, so it can be instantiated.
	 * Then the injector will register this instance.
	 * When the same class is asked again.
	 * Then the injector should return the same instance.
	 */
	@Test
	public void get_whenClassRegistered_thenReturnSameInstance() {
		whenGetAnArrayList();
		whenModifyTheArrayList();
		whenGetAnArrayList();
		thenTheInstanceIsModified();
	}
	
	/**
	 * When a list of dependencies are registered into the repository.
	 * Then instances should be initialized.
	 * @throws InstantiationException 
	 */
	@Test
	public void initComponent_whenListOfDependenciesToInit_thenDependenciesAreCreated() {
		givenADependency(List.class, ArrayList.class);
		givenADependency(Set.class, LinkedHashSet.class);
		whenInitDependencies();
		whenGetAList();
		thenInstanceIsNotNull();
		whenGetASet();
		thenInstanceIsNotNull();
	}
	
	private void givenADependency(Class<?> bindTo, Class<?> implementedBy) {
		expectedDependencies.add(new Dependency(
				bindTo.getName(),
				implementedBy.getName(),
				null));
	}
	
	private void whenGetAnArrayList() {
		actualInstance = (Collection<Object>) injector.get(ArrayList.class.getName());
	}
	
	private void whenGetAList() {
		actualInstance = (Collection<Object>) injector.get(List.class.getName());
	}
	
	private void whenGetASet() {
		actualInstance = (Collection<Object>) injector.get(Set.class.getName());
	}
	
	private void whenInitDependencies() {
		injector.initComponents(expectedDependencies);
	}
	
	private void whenModifyTheArrayList() {
		actualInstance.add(MODIFY);
	}
	
	private void thenInstanceIsNull() {
		assertNull(actualInstance);
	}
	
	private void thenInstanceIsNotNull() {
		assertNotNull(actualInstance);
	}
	
	private void thenTheInstanceIsModified() {
		assertTrue(actualInstance.contains(MODIFY));
	}
	
	/**
	 * Using mockito, the fields are not initialized.
	 * @author JoseCH
	 *
	 */
	public class DummyDependencyInjectorBase extends DependencyInjectorBase {

		public void init() throws OnRestInitializationException {
			
		}
		
	}
}
