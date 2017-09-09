package com.healthiq.takehome.enums;

import java.util.HashMap;
import java.util.Map;

public enum ActionEnum {

	EAT ("E", 2),
	EXERCISE ("X", 2),
	;
	
	private String code;
	private int affectHours;
	
	private static Map<String, ActionEnum> map;
	
	static {
		map = new HashMap<String, ActionEnum>();
		for (ActionEnum ae : ActionEnum.values()) {
			map.put(ae.code, ae);
		}
	}
	private ActionEnum(String code, int hours) {
		this.code = code;
		this.affectHours = hours;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getAffectHours() {
		return affectHours;
	}
	
	public static ActionEnum getEnum(String code) {
		return map.get(code);
	}
	
}
