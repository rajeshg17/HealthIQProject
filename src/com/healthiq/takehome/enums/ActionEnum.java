package com.healthiq.takehome.enums;

public enum ActionEnum {

	EAT (2),
	EXERCISE (2),
	;
	
	private int affectHours;
	
	private ActionEnum(int hours) {
		this.affectHours = hours;
	}
	
	public int getAffectHours() {
		return affectHours;
	}
	
}
