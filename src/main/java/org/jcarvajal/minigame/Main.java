package org.jcarvajal.minigame;

import java.io.IOException;

import org.jcarvajal.tiny.TinyServer;
import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;

public class Main {

	public static void main(String[] args)
			throws OnInitConfigurationException, IOException {
		TinyServer server = new TinyServer();
		server.start();
	}

}
