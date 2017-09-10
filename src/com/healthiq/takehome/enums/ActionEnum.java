package com.healthiq.takehome.enums;

import java.util.HashMap;
import java.util.Map;

public enum ActionEnum {

	EAT ("F", 120, 1),
	EXERCISE ("X", 60, -1),
	;
	
	private String code;
	private int affectMin;
	private int impact;
	
	private static Map<String, ActionEnum> map;
	
	static {
		map = new HashMap<String, ActionEnum>();
		for (ActionEnum ae : ActionEnum.values()) {
			map.put(ae.code, ae);
		}
	}
	private ActionEnum(String code, int min, int impact) {
		this.code = code;
		this.affectMin = min;
		this.impact = impact;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getAffectMin() {
		return affectMin;
	}
	
	public int getImpact() {
		return impact;
	}
	
	public static ActionEnum getEnum(String code) {
		return map.get(code);
	}
	
}
