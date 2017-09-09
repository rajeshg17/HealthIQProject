package com.healthiq.takehome;

import java.io.FileNotFoundException;

// TODO replace System.out.println with log4j

public class BloodSugarSimulator {
	
	private DaoService daoService = new DaoService();
	
	private void loadData() throws FileNotFoundException {
		daoService.loadData();
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		BloodSugarSimulator bss = new BloodSugarSimulator();
		bss.loadData();
	}

}
