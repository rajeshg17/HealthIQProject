package com.healthiq.takehome.bo;

import com.healthiq.takehome.enums.ActionEnum;

public class ActionDetail {

	private String time;
	private ActionEnum action;
	private ImpactEntity entity;
	
	public ActionDetail() {
	}
	
	public ActionDetail(String time, ActionEnum action, ImpactEntity entity) {
		this.time = time;
		this.action = action;
		this.entity = entity;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionDetail [time=").append(time)
				.append(", entity=").append(entity)
				.append(", action=").append(action).append("]");
		return builder.toString();
	}
	
	
}
