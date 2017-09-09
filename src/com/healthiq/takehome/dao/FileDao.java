package com.healthiq.takehome.dao;

import java.io.FileNotFoundException;

import com.healthiq.takehome.bo.ImpactEntity;

public interface FileDao<E extends ImpactEntity> {
	
	void loadFile(String path) throws FileNotFoundException;
	
	E getEntity(Integer id);

}
