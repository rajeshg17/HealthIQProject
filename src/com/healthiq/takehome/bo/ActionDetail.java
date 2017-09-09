package com.healthiq.takehome.bo;

import org.apache.commons.lang3.StringUtils;

import com.healthiq.takehome.enums.ActionEnum;

public class ActionDetail implements Comparable<ActionDetail> {

	private String time;
	private ActionEnum action;
	private ImpactEntity entity;
	private Integer timeOffsetInMin;
	
	public ActionDetail(Integer timeOffsetInMin) {
		setTimeOffsetInMin(timeOffsetInMin);
	}
	
	public ActionDetail(String time, ActionEnum action, ImpactEntity entity) {
		setTime(time);
		this.action = action;
		this.entity = entity;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
		int hh = Integer.parseInt(time.substring(0, 2));
		int mi = Integer.parseInt(time.substring(3));
		this.timeOffsetInMin = hh*60 + mi;
	}
	
	public void setTimeOffsetInMin(int timeOffsetInMin) {
		this.timeOffsetInMin = timeOffsetInMin;
		int hh = timeOffsetInMin / 60;
		int mi = timeOffsetInMin % 60;
		this.time = StringUtils.leftPad(Integer.toString(hh), 2, '0') + ":" + StringUtils.leftPad(Integer.toString(mi), 2, '0');
	}
	public int getTimeOffsetInMin() {
		return timeOffsetInMin;
	}

	public ActionEnum getAction() {
		return action;
	}
	public void setAction(ActionEnum action) {
		this.action = action;
	}

	public ImpactEntity getEntity() {
		return entity;
	}
	public void setEntity(ImpactEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public int compareTo(ActionDetail other) {
		return timeOffsetInMin.compareTo(((ActionDetail)other).timeOffsetInMin);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionDetail [time=").append(time)
				.append(", timeOffsetInMin=").append(timeOffsetInMin)
				.append(", entity=").append(entity)
				.append(", action=").append(action)
				.append("]");
		return builder.toString();
	}
	
}
