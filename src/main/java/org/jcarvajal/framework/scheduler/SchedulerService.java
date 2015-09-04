package org.jcarvajal.framework.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerService {
	
	private final ScheduledExecutorService scheduledExecutorService =
	        Executors.newScheduledThreadPool(2);
	
	public void scheduleJob(Runnable job, String minutes) {
		scheduledExecutorService.schedule(job,
		    Integer.valueOf(minutes),
		    TimeUnit.MINUTES);
	}
	
	public void scheduleJob(Runnable job, int minutes) {
		scheduledExecutorService.schedule(job,
		    minutes,
		    TimeUnit.MINUTES);
	}
}
