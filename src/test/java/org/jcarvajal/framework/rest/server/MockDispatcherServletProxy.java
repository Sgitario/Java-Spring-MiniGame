package org.jcarvajal.framework.rest.server;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.DispatcherServlet;

public class MockDispatcherServletProxy extends DispatcherServlet {
	
	private boolean initInvoked = false;
	
	@Override
	public void init() throws OnRestInitializationException {
		initInvoked = true;
	}

	public boolean getInitHasBeenInvoked() {
		return initInvoked;
	}

}
