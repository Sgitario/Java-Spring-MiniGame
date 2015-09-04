package org.jcarvajal.minigame.application;

import org.jcarvajal.framework.rest.servlet.controllers.RequestMethod;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.RequestMapping;

public class UserController {
	@RequestMapping(url = "/{userId}/login", method = RequestMethod.GET)
	public String login(@PathVariable(name = "userId") int userId) {
		return "Hola= " + userId;
	}
}
