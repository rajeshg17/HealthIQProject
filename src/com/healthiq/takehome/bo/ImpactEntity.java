package com.healthiq.takehome.bo;

/*
 * Interface for entities that impact glycemic index i.e. Food and Exercise. Contains common methods for all such entities
 */
public interface ImpactEntity {
	
	Integer getId();
	
	double getGlycemicIndexChangeRate();
	
}
