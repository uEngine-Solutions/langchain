package org.uengine.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.spi.OperableTrigger;

public class SchedulerUtil {

	public static Calendar getCalendarByCronExpression(String cronExpression) throws Exception {
		Trigger trigger = newTrigger().withSchedule(cronSchedule(cronExpression)).startNow().build();
		List<Date> dates = TriggerUtils.computeFireTimes((OperableTrigger) trigger, null, 1);

		Calendar c = new GregorianCalendar();
		c.setTime(dates.get(0));
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	public static void main(String args[]) {
		try {
			Calendar c = SchedulerUtil.getCalendarByCronExpression("0 17 * 22 2 ?");
			System.out.println(c.getTime().toLocaleString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
