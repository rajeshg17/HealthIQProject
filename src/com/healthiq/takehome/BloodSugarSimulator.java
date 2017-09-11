package com.healthiq.takehome;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.healthiq.takehome.bo.ActionDetail;
import com.healthiq.takehome.bo.ImpactEntity;
import com.healthiq.takehome.enums.ActionEnum;
import com.healthiq.takehome.utils.Utils;

public class BloodSugarSimulator {
	
	private static final int BASE_GLYCEMIC_INDEX = 80;
	private static final int MINUTES_IN_PERIOD = 1440;
	private static final int NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE = 1; // can go up or down to reach 80
	
	private DaoService daoService = new DaoService();
	
	// load food and exercise data
	private void loadData() throws FileNotFoundException {
		daoService.loadData();
	}
	
	// read line. replace multiple spaces with single space. trim.
	private static String readNextLine(Scanner scanner) {
		String line = scanner.nextLine();
		return line.replaceAll(" +", " ").trim();
	}

	/*
	 * Main logic.
	 * For every action, add the change rate for every minute of the period impacted the action.
	 * 	At the end of processing all the action, the aggregate change will be stored for every minute
	 * 
	 * For every minute, taking previous minute as base, add the aggregated change rate.
	 * 	- if no actions affect the minute, normalize
	 *  - also calculate the glycation in this loop
	 */
	private Map<String, double[]> calculateIndexesByMin(List<ActionDetail> actionDetails) throws IOException {
		double[] glycemicIndexByMin = new double[MINUTES_IN_PERIOD];
		double[] glycationByMin = new double[MINUTES_IN_PERIOD];
		int glycation = 0;
		
		for (ActionDetail action : actionDetails) {
			for (int i = 1; i <= action.getAction().getAffectMin(); i++) {
				glycemicIndexByMin[i] += action.getEntity().getGlycemicIndexChangeRate();
			}
		}
		
		glycemicIndexByMin[0] = BASE_GLYCEMIC_INDEX;
		glycationByMin[0] = 0;

		for (int time = 1; time < MINUTES_IN_PERIOD; time++ ) {
			double glycemicIndex = glycemicIndexByMin[time-1];
			double glycemicIndexChange = glycemicIndexByMin[time];
			
			if (glycemicIndexChange != 0) {
				glycemicIndex = glycemicIndex + glycemicIndexChange;
			}
			else { // if no actions affect, normalize
				if (glycemicIndex > BASE_GLYCEMIC_INDEX) {
					glycemicIndex = Math.max(glycemicIndex - NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE, 80); // if prevGlycemicIndex = 80.5, it should go to 80, not 79.5
				}
				else if (glycemicIndex < BASE_GLYCEMIC_INDEX) {
					glycemicIndex = Math.min(glycemicIndex + NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE, 80); // if prevGlycemicIndex = 79.5, it should go to 80, not 80.5
				}
			}
			glycemicIndexByMin[time] = glycemicIndex;
			if (glycemicIndex > 150) {
				glycation++;
			}
			glycationByMin[time] = glycation;
		}
		
		Map<String, double[]> chartData = new HashMap<String, double[]>();
		chartData.put("Glycemic Index", glycemicIndexByMin);
		chartData.put("Glycation", glycationByMin);
		return chartData;
	}
	
	private List<ActionDetail> readInput(String fileName) throws IOException {
		List<ActionDetail> actionDetails = new ArrayList<ActionDetail>();
		System.out.println("Type \"END\" to end data input");
		Scanner scanner = new Scanner(new File("input/" + fileName));
		String line = readNextLine(scanner);
		
		int lineNo = 0;
		while (!"end".equalsIgnoreCase(line)) {
			lineNo++;
			if (StringUtils.isEmpty(line)) {
				line = readNextLine(scanner);
				continue;
			}
			
			boolean isInputValid = true;
			String[] actionDetailInput = line.split(" ");
			if (actionDetailInput.length < 3) { // other columns for comments for testing
				isInputValid = false;
				System.out.println(lineNo + ": " + line + ". ignoring line. invalid input. length < 3");
			}
			
			String time = null;
			ActionEnum action = null;
			ImpactEntity e = null; 

			if (isInputValid) {
				time = actionDetailInput[0];
				isInputValid = Utils.isValidTimeFormat(time);
			}

			if (!isInputValid) {
				System.out.println(lineNo + ": " + line + ". ignoring line. invalid time = " + actionDetailInput[0]);
			}
			else {
				action = ActionEnum.getEnum(actionDetailInput[1]);
				if (action == null) {
					isInputValid = false;
					System.out.println(lineNo + ": " + line + ". ignoring line. invalid action=" + actionDetailInput[1]);
				}
			}

			if (isInputValid) {
				if (action == ActionEnum.EAT) {
					e = daoService.getFoodDao().getEntity(Integer.parseInt(actionDetailInput[2]));
				}
				else {
					e = daoService.getExerciseDao().getEntity(Integer.parseInt(actionDetailInput[2]));
				}
				if (e == null) {
					isInputValid = false;
					System.out.println(lineNo + ": " + line + ". ignoring line. invalid action");
				}
			}

			if (isInputValid) {
				actionDetails.add(new ActionDetail(time, action, e));
				System.out.println(lineNo + ": " + line + ". valid");
			}
			line = readNextLine(scanner);
		}
		scanner.close();
		System.out.println("Thank You for being Health Conscious!");
		return actionDetails;
	}

	public static void main(String[] args) throws IOException {
		BloodSugarSimulator bss = new BloodSugarSimulator();
		
		bss.loadData();
		List<ActionDetail> actionDetails = bss.readInput("input.dat");
		
		Map<String, double[]> chartData = bss.calculateIndexesByMin(actionDetails);
		double[] glycemicIndexByMin = chartData.get("Glycemic Index");
		double[] glycationByMin = chartData.get("Glycation");
		
		int printStartTime = 0;
		int printEndTime = MINUTES_IN_PERIOD;
		// if (actionDetails.size() > 0) {
		//		printStartTime = actionDetails.get(0).getTimeOffsetInMin();
		//		printEndTime = printStartTime + 360;
		//	}
		BufferedWriter bw = new BufferedWriter(new FileWriter("output/output.dat", false));
		for (int i = printStartTime; i < printEndTime; i++) {
			bw.append(Utils.getTimeFromOffset(i) + "\t" + glycemicIndexByMin[i]+ "\t" + glycationByMin[i] + "\n");
		}
		bw.flush();
		bw.close();
		
		new TimeSeriesChart(glycemicIndexByMin, glycationByMin);
	}

}
