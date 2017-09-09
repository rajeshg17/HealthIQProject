package com.healthiq.takehome.dao;

import java.io.FileNotFoundException;

import com.healthiq.takehome.bo.Food;

public interface FoodDao extends FileDao<Food> {
	
	void loadFile(String path) throws FileNotFoundException;
	
	Food getEntity(Integer id);

}
