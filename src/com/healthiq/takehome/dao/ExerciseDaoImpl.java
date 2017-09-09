package com.healthiq.takehome.dao;

import com.healthiq.takehome.bo.Exercise;

public class ExerciseDaoImpl extends AbstractFileDao<Exercise> implements ExerciseDao {

	@Override
	Exercise populateEntity(String data) {
		String[] dataArr = data.split("\t");
		Exercise e = new Exercise(Integer.parseInt(dataArr[0]), dataArr[1], Integer.parseInt(dataArr[2]));
		return e;
	}

}
