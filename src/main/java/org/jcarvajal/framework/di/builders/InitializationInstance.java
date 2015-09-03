package org.jcarvajal.framework.di.builders;

import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.exceptions.InstantiationException;

/**
 * The instance is instantiated at the beginning.
 * 
 * @author JoseCH
 */
public class InitializationInstance extends Instance {
	public InitializationInstance(DependencyInjectorBase injector) {
		super(injector);
	}

	@Override
	public void onInit() throws InstantiationException {
		initializeInstance();
	}

}
