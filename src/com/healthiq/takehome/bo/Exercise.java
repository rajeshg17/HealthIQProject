package com.healthiq.takehome.bo;

public class Exercise implements ImpactEntity {
	
	private Integer id;
	private String name;
	private Integer exerciseIndex;
	
	public Exercise() {
	}
	
	public Exercise(Integer id, String name, Integer exerciseIndex) {
		this.id = id;
		this.name = name;
		this.exerciseIndex = exerciseIndex;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Exercise [id=").append(id)
				.append(", name=").append(name)
				.append(", exerciseIndex=").append(exerciseIndex)
				.append("]");
		return builder.toString();
	}
	
}
