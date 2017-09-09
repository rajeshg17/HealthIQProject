package com.healthiq.takehome;

import java.io.FileNotFoundException;

// TODO replace System.out.println with log4j
// Assumption - BloodSugar does not fall below 80

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
