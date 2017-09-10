package com.healthiq.takehome;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
	
	// utility method to add action to list of actions at the minute. if no actions, create a new list at the minute and add to the list
	private static void addActionAtOffset(List<ActionDetail>[] actionsByMin, int time, ActionDetail action) {
		List<ActionDetail> actions = actionsByMin[time];
		if (actions == null) {
			actions = new LinkedList<ActionDetail>();
			actionsByMin[time] = actions;
		}
		actions.add(action);
	}

	/*
	 * Main logic.
	 * For each minute of the day (denoted by the index of the array of 1440 elements),
	 * 	- maintain a list of actions that affect the glycemic index
	 * 		- this is calculated by adding the action to every minute where its effect is felt i.e from startTime+1 + 1 or 2 hours
	 * 
	 * For every minute, taking previous minute as base, calculate the effect of every action.
	 * 	- if no actions affect the minute, normalize
	 *  - also calculate the glycation in this loop
	 */
	public double[] generateGraphPoints(List<ActionDetail> actionDetails) throws IOException {
		// this data structure contains actions affecting the minute 
		List<ActionDetail>[] actionsByMin = new List[MINUTES_IN_PERIOD];

		double[] glycemicIndexByMin = new double[MINUTES_IN_PERIOD];
		double[] glycationByMin = new double[MINUTES_IN_PERIOD];
		int glycation = 0;
		
		for (ActionDetail action : actionDetails) {
			int startTime = action.getTimeOffsetInMin();
			for (int i = 1; i <= action.getAction().getAffectMin(); i++) {
				addActionAtOffset(actionsByMin, startTime+i, action);
			}
		}
		
		glycemicIndexByMin[0] = BASE_GLYCEMIC_INDEX;
		glycationByMin[0] = 0;

		for (int time = 1; time < MINUTES_IN_PERIOD; time++ ) {
			double glycemicIndex = glycemicIndexByMin[time-1];
			
			List<ActionDetail> actions = actionsByMin[time];
			
			// if no actions affect, normalize
			if (actions == null || actions.isEmpty()) {
				if (glycemicIndex > BASE_GLYCEMIC_INDEX) {
					glycemicIndex = Math.max(glycemicIndex - NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE, 80); // if prevGlycemicIndex = 80.5, it should go to 80, not 79.5
				}
				else if (glycemicIndex < BASE_GLYCEMIC_INDEX) {
					glycemicIndex = Math.min(glycemicIndex + NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE, 80); // if prevGlycemicIndex = 79.5, it should go to 80, not 80.5
				}
			}
			else {
				for (ActionDetail action : actions) {
					glycemicIndex = glycemicIndex + action.getEntity().getGlycemicIndexChangeRate();
				}
			}
			glycemicIndexByMin[time] = glycemicIndex;
			if (glycemicIndex > 150) {
				glycation++;
			}
			glycationByMin[time] = glycation;
			
		}
		
		int printStartTime = 0;
		int printEndTime = MINUTES_IN_PERIOD;
//		if (actionDetails.size() > 0) {
//			printStartTime = actionDetails.get(0).getTimeOffsetInMin();
//			printEndTime = printStartTime + 360;
//		}
		BufferedWriter bw = new BufferedWriter(new FileWriter("output/output.dat", false));
		for (int i = printStartTime; i < printEndTime; i++) {
			bw.append(Utils.getTimeFromOffset(i) + "\t" + glycemicIndexByMin[i]+ "\t" + glycationByMin[i] + "\n");
		}
		bw.flush();
		bw.close();
		return glycemicIndexByMin;
	}
	
	public static void main(String[] args) throws IOException {
		BloodSugarSimulator bss = new BloodSugarSimulator();
		bss.loadData();
		
		List<ActionDetail> actionDetails = new ArrayList<ActionDetail>();
		System.out.println("Type \"END\" to end data input");
		Scanner scanner = new Scanner(new File("input/input1.dat"));
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
					e = bss.daoService.getFoodDao().getEntity(Integer.parseInt(actionDetailInput[2]));
				}
				else {
					e = bss.daoService.getExerciseDao().getEntity(Integer.parseInt(actionDetailInput[2]));
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
		System.out.println("Thank You for being Health Conscious!");
		
		bss.generateGraphPoints(actionDetails);
		scanner.close();
	}

}
