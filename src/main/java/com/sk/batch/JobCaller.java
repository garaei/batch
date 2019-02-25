package com.sk.batch;

import org.springframework.batch.core.JobExecution;

public interface JobCaller {
	String getCallerName();
	void jobStarted(JobExecution exec);
	void jobFinished(JobExecution exec);
}