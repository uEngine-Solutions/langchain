package org.uengine.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.uengine.kernel.GlobalContext;

public class JobScheduler extends GenericServlet {
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	private Scheduler sched = null;
	
	public void init() throws ServletException {
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
			sched.start();

			JobKey jobKey = new JobKey("waitJob", "uEngineJobs");
			TriggerKey triggerKey = new TriggerKey("waitTrigger");

			JobDetail jobDetail = sched.getJobDetail(jobKey);
			Trigger trigger = sched.getTrigger(triggerKey);

			if (jobDetail == null && jobDetail == null) {
				jobDetail = newJob(WaitJob.class).withIdentity(jobKey).build();
				trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule("0 * * * * ?")).forJob(jobKey).build();
				sched.scheduleJob(jobDetail, trigger);
			}

		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public void schedulerShutDown() {
		try {
			sched.shutdown();
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
	}
	
}
