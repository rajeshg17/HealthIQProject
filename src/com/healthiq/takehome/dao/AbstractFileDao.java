package com.healthiq.takehome.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.healthiq.takehome.bo.ImpactEntity;


public abstract class AbstractFileDao<E extends ImpactEntity> implements FileDao<E> {
	
	Map<Integer, E> entities = new HashMap<Integer, E>();

	abstract E populateEntity(String line);

	public void loadFile(String path) throws FileNotFoundException {
		// TODO change this for BufferedFileReader
		Scanner scanner = new Scanner(new File(path));
		scanner.nextLine(); // header. ignore
		
		int i = 0; // excluding header
		String entityName = null;
		while (scanner.hasNextLine()) {
			String data = scanner.nextLine();
			if (data != null && !data.trim().equals("")) {
				E e = populateEntity(data);
				entities.put(e.getId(), e);
				i++;
				entityName = e.getClass().getSimpleName();
			}
		}
		System.out.println("loaded " + i + " entities(" + entityName + ")");
		scanner.close();
	}
	
	@Override
	public E getEntity(Integer id) {
		return entities.get(id);
	}

}
