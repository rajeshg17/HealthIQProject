package com.healthiq.takehome;

import java.io.FileNotFoundException;

import com.healthiq.takehome.dao.ExerciseDao;
import com.healthiq.takehome.dao.ExerciseDaoImpl;
import com.healthiq.takehome.dao.FoodDao;
import com.healthiq.takehome.dao.FoodDaoImpl;

public class DaoService {
	
	// this is typically done through Spring
	private static final FoodDao foodDao = new FoodDaoImpl();
	private static final ExerciseDao exerciseDao = new ExerciseDaoImpl();
	
	
	public void loadData() throws FileNotFoundException {
		foodDao.loadFile("data/food.dat");
		exerciseDao.loadFile("data/exercise.dat");
	}

}
