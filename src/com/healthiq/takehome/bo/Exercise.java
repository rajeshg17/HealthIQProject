package com.healthiq.takehome.bo;

import com.healthiq.takehome.enums.ActionEnum;

public class Exercise implements ImpactEntity {
	
	private ActionEnum ae = ActionEnum.EXERCISE;

	private Integer id;
	private String name;
	private Integer exerciseIndex;
	private double glycemicIndexChangeRate;
	
	public Exercise(Integer id, String name, Integer exerciseIndex) {
		this.id = id;
		this.name = name;
		this.exerciseIndex = exerciseIndex;
		glycemicIndexChangeRate = (double)exerciseIndex * ae.getImpact() / ae.getAffectMin();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getExerciseIndex() {
		return exerciseIndex;
	}
	public void setExerciseIndex(Integer exerciseIndex) {
		this.exerciseIndex = exerciseIndex;
	}

	@Override
	public double getGlycemicIndexChangeRate() {
		return glycemicIndexChangeRate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Exercise [id=").append(id)
				.append(", name=").append(name)
				.append(", exerciseIndex=").append(exerciseIndex)
				.append(", glycemicIndexChangeRate=").append(glycemicIndexChangeRate)
				.append("]");
		return builder.toString();
	}
	
}
