package com.healthiq.takehome.dto;

import java.util.Date;

public class GraphPoint {
	
	private Date time;
	private Double index;
	
	public GraphPoint() {
	}

	public GraphPoint(Date time, Double index) {
		this.time = time;
		this.index = index;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public Double getIndex() {
		return index;
	}
	public void setIndex(Double index) {
		this.index = index;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GraphPoints [time=").append(time)
				.append(", index=").append(index)
				.append("]");
		return builder.toString();
	}

}
