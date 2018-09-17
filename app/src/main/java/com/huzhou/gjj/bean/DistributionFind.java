package com.huzhou.gjj.bean;

import java.io.Serializable;

public class DistributionFind implements Serializable {


	/**
	 * JobName : 建行贷款预约
	 * JobType : 4
	 * QueueNum : 0
	 * WaitTime : 0
	 */

	private String JobName;
	private String JobType;
	private String QueueNum;
	private String WaitTime;

	public String getJobName() {
		return JobName;
	}

	public void setJobName(String JobName) {
		this.JobName = JobName;
	}

	public String getJobType() {
		return JobType;
	}

	public void setJobType(String JobType) {
		this.JobType = JobType;
	}

	public String getQueueNum() {
		return QueueNum;
	}

	public void setQueueNum(String QueueNum) {
		this.QueueNum = QueueNum;
	}

	public String getWaitTime() {
		return WaitTime;
	}

	public void setWaitTime(String WaitTime) {
		this.WaitTime = WaitTime;
	}
}
