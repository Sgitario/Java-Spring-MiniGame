package org.jcarvajal.webapp.app;

import org.jcarvajal.webapp.controllers.LoginController;
import org.jcarvajal.webapp.controllers.PageController;
import org.jcarvajal.webapp.repositories.UserRepository;
import org.jcarvajal.webapp.repositories.UserRepositoryImpl;
import org.jcarvajal.webapp.server.Server;
import org.jcarvajal.webapp.services.UserService;
import org.jcarvajal.webapp.services.UserServiceImpl;
import org.jcarvajal.webapp.filters.security.AuthenticationProviderFilter;
import org.jcarvajal.webapp.servlet.impl.ViewAndModelDispatcherServlet;

@SuppressWarnings("restriction")
public class Main {
	
	public static void main(String[] args) {
		// Users
		UserRepository userRepository = new UserRepositoryImpl();
		userRepository.addUser("user1", "pass1", "PAG_1");
		userRepository.addUser("user2", "pass2", "PAG_2");
		userRepository.addUser("user3", "pass3", "PAG_3");
		
		// Services
		UserService userService = new UserServiceImpl(userRepository);
		
		// Server
		Server server = new Server();
		
		server.addFilter(new AuthenticationProviderFilter(userService));
		server.setServlet(new ViewAndModelDispatcherServlet()
				.setViewPath("/views")
				.setRedirectToLogin("login"));
		server.addController(new PageController());
		server.addController(new LoginController(userService));
		
		server.start();
	}

}
