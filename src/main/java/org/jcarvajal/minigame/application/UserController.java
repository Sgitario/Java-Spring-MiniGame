package org.jcarvajal.minigame.application;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.di.annotations.Init;
import org.jcarvajal.framework.rest.RequestMethod;
import org.jcarvajal.framework.rest.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.controllers.annotations.RequestMapping;
import org.jcarvajal.framework.scheduler.SchedulerService;
import org.jcarvajal.minigame.service.SessionService;

/**
 * Controller for User operations:
 * - Login
 * @author JoseCH
 *
 */
public class UserController {
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private SchedulerService scheduler;
	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	public void setScheduler(SchedulerService scheduler) {
		this.scheduler = scheduler;
	}
	
	@Init
	public void scheduleRemoveSessionsJob() {
		scheduler.scheduleJob(new Runnable() {

			public void run() {
				sessionService.removeExpiredSessions();
			}
			
		});
	}
	
	/**
	 * This function returns a session key in the form of a string (without spaces or “strange” characters) 
	 * which shall be valid for use with the other functions for 10 minutes.
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(url = "/{userId}/login", method = RequestMethod.GET)
	public String login(@PathVariable(name = "userId") int userId) {
		return sessionService.getSessionKey(userId);
	}
}
