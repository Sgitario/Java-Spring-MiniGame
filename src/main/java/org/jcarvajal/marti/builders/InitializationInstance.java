package org.jcarvajal.marti.builders;

/**
 * The instance is instantiated at the beginning.
 * 
 * @author JoseCH
 */
public class InitializationInstance extends Instance {
	@Override
	public void onInit() {
		initializeInstance();
	}

}
