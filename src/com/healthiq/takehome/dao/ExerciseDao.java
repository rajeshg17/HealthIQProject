package com.healthiq.takehome.dao;

import java.io.FileNotFoundException;

import com.healthiq.takehome.bo.Exercise;

public interface ExerciseDao extends FileDao<Exercise> {
	
	void loadFile(String path) throws FileNotFoundException;
	
	Exercise getEntity(Integer id);

}
