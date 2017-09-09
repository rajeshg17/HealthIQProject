package com.healthiq.takehome.dao;

import java.io.FileNotFoundException;

import com.healthiq.takehome.bo.Entity;

public interface FileDao<E extends Entity> {
	
	void loadFile(String path) throws FileNotFoundException;
	
	E getEntity(Integer id);

}
