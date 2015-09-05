package org.jcarvajal.minigame.application;

import static org.mockito.Mockito.*;

import org.jcarvajal.framework.scheduler.SchedulerService;
import org.jcarvajal.minigame.service.SessionService;
import org.junit.Before;
import org.junit.Test;

public class UserControllerTest {
	
	private static final int USER_ID = 1;
	
	private SessionService mockSessionService;
	private SchedulerService mockSchedulerService;
	
	private UserController controller;
	
	@Before
	public void setup() {
		this.mockSessionService = mock(SessionService.class);
		this.mockSchedulerService = mock(SchedulerService.class);
		
		this.controller = new UserController();
		this.controller.setScheduler(mockSchedulerService);
		this.controller.setSessionService(mockSessionService);
	}
	
	@Test
	public void login_thenServiceIsInvoked() {
		whenLogin();
		thenGetSessionKeyIsInvoked();
	}
	
	@Test
	public void scheduleRemoveSessionsJob_thenSchedulerIsInvoked() {
		whenScheduleRemoveSessionsJob();
		thenSchedulerIsInvoked();
	}
	
	private void whenLogin() {
		this.controller.login(USER_ID);
	}
	
	private void whenScheduleRemoveSessionsJob() {
		this.controller.scheduleRemoveSessionsJob();
	}
	
	private void thenGetSessionKeyIsInvoked() {
		verify(mockSessionService, times(1)).getSessionKey(USER_ID);
	}
	
	private void thenSchedulerIsInvoked() {
		verify(mockSchedulerService, times(1)).scheduleJob(any(Runnable.class));
	}
}
