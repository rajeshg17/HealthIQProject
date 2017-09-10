package com.healthiq.takehome.bo;

import com.healthiq.takehome.enums.ActionEnum;

public class Food implements ImpactEntity {

	private ActionEnum ae = ActionEnum.EAT;

	private Integer id;
	private String name;
	private Integer glycemicIndex;
	private double glycemicIndexChangeRate;
	
	public Food(Integer id, String name, Integer glycemicIndex) {
		this.id = id;
		this.name = name;
		this.glycemicIndex = glycemicIndex;
		glycemicIndexChangeRate = (double)glycemicIndex * ae.getImpact() / ae.getAffectMin();

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
	public double getGlycemicIndexChangeRate() {
		return glycemicIndexChangeRate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Food [id=").append(id)
				.append(", name=").append(name)
				.append(", glycemicIndex=").append(glycemicIndex)
				.append(", glycemicIndexChangeRate=").append(glycemicIndexChangeRate)
				.append("]");
		return builder.toString();
	}

}
