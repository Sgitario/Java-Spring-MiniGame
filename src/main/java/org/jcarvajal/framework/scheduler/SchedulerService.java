package org.jcarvajal.framework.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple scheduler service to schedule jobs against an executor service.
 * @author JoseCH
 *
 */
public class SchedulerService {
	
	private final ScheduledExecutorService scheduledExecutorService =
	        Executors.newScheduledThreadPool(2);
	
	public void scheduleJob(Runnable job, String minutes) {
		scheduleJob(job, Integer.valueOf(minutes));
	}
	
	public void scheduleJob(Runnable job, int minutes) {
		scheduledExecutorService.schedule(job,
		    minutes,
		    TimeUnit.MINUTES);
	}
}
