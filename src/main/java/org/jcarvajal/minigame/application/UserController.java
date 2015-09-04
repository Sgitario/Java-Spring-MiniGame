package org.jcarvajal.minigame.application;

import org.jcarvajal.framework.rest.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.controllers.annotations.RequestMapping;
import org.jcarvajal.framework.rest.models.RequestMethod;

public class UserController {
	@RequestMapping(url = "/{userId}/login", method = RequestMethod.GET)
	public String login(@PathVariable(name = "userId") int userId) {
		return "Hola= " + userId;
	}
}
