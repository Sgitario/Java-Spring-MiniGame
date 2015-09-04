package org.jcarvajal.minigame.service.impl;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.di.annotations.Init;
import org.jcarvajal.framework.scheduler.SchedulerService;
import org.jcarvajal.minigame.infrastructure.SessionRepository;
import org.jcarvajal.minigame.service.SessionService;

public class SessionServiceImpl implements SessionService {
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private SchedulerService scheduler;
	
	private String sessionExpiredMinutes;
	
	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}
	
	public void setSessionRepository(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	public String getSessionExpiredMinutes() {
		return sessionExpiredMinutes;
	}
	
	public void setSessionExpiredMinutes(String sessionExpiredMinutes) {
		this.sessionExpiredMinutes = sessionExpiredMinutes;
	}
	
	public SchedulerService getScheduler() {
		return scheduler;
	}
	
	public void setScheduler(SchedulerService scheduler) {
		this.scheduler = scheduler;
	}
	
	@Init
	public void removeSessionsRegister() {
		scheduler.scheduleJob(new Runnable() {

			public void run() {
				// Call to remove sessions method.
			}
			
		}, sessionExpiredMinutes);
	}
}
