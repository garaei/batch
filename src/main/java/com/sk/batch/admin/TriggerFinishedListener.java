package com.sk.batch.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class TriggerFinishedListener implements JobExecutionListener {
	private Logger logger = LoggerFactory.getLogger(TriggerFinishedListener.class);
	
	private TriggerJobList triggerJobList;
	private JobScheduler jobScheduler;
	private TriggerJobInfo triggerJobInfo;
	private String heatbeat;
	
	public TriggerFinishedListener(TriggerJobList list) {
		this.triggerJobList = list;
	}
	
	public void setProperties(JobScheduler scheduler, TriggerJobInfo jobInfo, String cron) {
		this.jobScheduler = scheduler;
		this.triggerJobInfo = jobInfo;
		this.heatbeat = cron;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		//do nothing
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			for(TriggerJobInfo jobInfo : triggerJobList) {
				if(jobInfo.isRegistered()) {
					triggerJobInfo.setRegistered(true);
				    triggerJobInfo.setCron(heatbeat);
					jobScheduler.setCron(triggerJobInfo, heatbeat);
					logger.info("@@@ REGIST TO HEARTBEAT JOB=" + triggerJobInfo.toString());
					return;
				}
			}
		}
		else {
			logger.info("@@@ REGIST NEEDS MORE TRIES JOB=" + triggerJobInfo.toString());
		}
	}
}
