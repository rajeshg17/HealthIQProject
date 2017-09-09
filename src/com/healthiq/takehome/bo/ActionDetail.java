package com.healthiq.takehome.bo;

import com.healthiq.takehome.enums.ActionEnum;

public class ActionDetail {

	private ActionEnum action;
	private Entity entity;
	private String time;
	
	public ActionDetail(ActionEnum action, Entity entity, String time) {
		this.action = action;
		this.entity = entity;
		this.time = time;
	}

	public ActionEnum getAction() {
		return action;
	}
	public void setAction(ActionEnum action) {
		this.action = action;
	}

	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
