package org.jcarvajal.minigame;

import java.io.IOException;

import org.jcarvajal.framework.rest.RestServer;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;

public class Main {

	public static void main(String[] args)
			throws OnRestInitializationException, IOException {
		RestServer server = new RestServer();
		server.start();
	}

}
