package com.healthiq.takehome;

import java.io.FileNotFoundException;

public class BloodSugarSimulator {
	
	private DaoService daoService = new DaoService();
	
	private void loadData() throws FileNotFoundException {
		daoService.loadData();
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Hello, World!");
		
		BloodSugarSimulator bss = new BloodSugarSimulator();
		
		bss.loadData();
		
	}

}
