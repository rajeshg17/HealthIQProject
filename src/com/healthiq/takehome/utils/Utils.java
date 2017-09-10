package com.healthiq.takehome.utils;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	/*
	 * Format should HH24:MI
	 */
	public static boolean isValidTimeFormat(String time) {
		if (time.length() != 5) {
			return false;
		}
		if (time.charAt(2) != ':') {
			return false;
		}
		try {
			int hh = Integer.parseInt(time.substring(0, 2));
			int mi = Integer.parseInt(time.substring(3));
			if (hh < 0 || hh > 23) {
				return false;
			}
			if (mi < 0 || mi > 59) {
				return false;
			}
		}
		catch (Exception e) { // not just NumberFormatException. It could also be string length???
			return false;
		}
		
		return true;
	}

	/*
	 * returns HH24:MI
	 */
	public static String getTimeFromOffset(int offsetInMin) {
		int hh = offsetInMin / 60;
		int mi = offsetInMin % 60;
		return StringUtils.leftPad(Integer.toString(hh), 2, '0') + ":" + StringUtils.leftPad(Integer.toString(mi), 2, '0');
	}
	
}
