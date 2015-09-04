package org.jcarvajal.framework.di.instances;

import java.util.Map;

import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.exceptions.InstantiationException;

/**
 * The instance is instantiated at the beginning.
 * 
 * @author JoseCH
 */
public class InitializationInstance extends Instance {
	public InitializationInstance(String bindClassName, String implClassName, Map<String, String> params, 
			DependencyInjectorBase injector) throws InstantiationException {
		super(bindClassName, implClassName, params, injector);
	}

	@Override
	public void onInit() throws InstantiationException {
		createInstance();
	}

}
