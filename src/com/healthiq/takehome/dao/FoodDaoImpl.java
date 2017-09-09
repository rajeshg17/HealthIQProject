package com.healthiq.takehome.dao;

import com.healthiq.takehome.bo.Food;

public class FoodDaoImpl extends AbstractFileDao<Food> implements FoodDao {

	@Override
	Food populateEntity(String data) {
		String[] dataArr = data.split("\t");
		Food f = new Food(Integer.parseInt(dataArr[0]), dataArr[1], Integer.parseInt(dataArr[2]));
		return f;
	}

}
