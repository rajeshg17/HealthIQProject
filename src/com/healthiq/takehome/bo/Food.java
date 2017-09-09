package com.healthiq.takehome.bo;

public class Food implements Entity {

	private Integer id;
	private String name;
	private Integer glycemicIndex;
	
	public Food() {
	}
	
	public Food(Integer id, String name, Integer glycemicIndex) {
		this.id = id;
		this.name = name;
		this.glycemicIndex = glycemicIndex;
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

	public Integer getGlycemicIndex() {
		return glycemicIndex;
	}
	public void setGlycemicIndex(Integer glycemicIndex) {
		this.glycemicIndex = glycemicIndex;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Food [id=").append(id)
				.append(", name=").append(name)
				.append(", glycemicIndex=").append(glycemicIndex)
				.append("]");
		return builder.toString();
	}

}
