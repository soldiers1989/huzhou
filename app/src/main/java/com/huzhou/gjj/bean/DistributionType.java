package com.huzhou.gjj.bean;

import java.io.Serializable;

public class DistributionType implements Serializable {


	/**
	 * JobName : 提取预约
	 * JobType : 1
	 */

	private String JobName;
	private String JobType;

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
}
