package org.jcarvajal.minigame.application;

import org.jcarvajal.framework.rest.annotations.PathVariable;
import org.jcarvajal.framework.rest.annotations.RequestMapping;
import org.jcarvajal.framework.rest.servlet.controllers.RequestMethod;

public class UserController {
	@RequestMapping(url = "{userId}/login", method = RequestMethod.GET)
	public String login(@PathVariable int userId) {
		return "Hola";
	}
}
