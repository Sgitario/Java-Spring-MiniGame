package org.jcarvajal.minigame;

import java.io.IOException;

import org.jcarvajal.tiny.TinyServer;
import org.jcarvajal.tiny.config.OnInitWebConfigurationException;

public class Main {

	public static void main(String[] args)
			throws OnInitWebConfigurationException, IOException {
		TinyServer server = new TinyServer();
		server.start();
	}

}
