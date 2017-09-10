package com.healthiq.takehome;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.healthiq.takehome.bo.ActionDetail;
import com.healthiq.takehome.bo.ImpactEntity;
import com.healthiq.takehome.enums.ActionEnum;
import com.healthiq.takehome.utils.Utils;

// TODO replace System.out.println with log4j
// TODO comment all classes

public class BloodSugarSimulator {
	
	private static final int BASE_GLYCEMIC_INDEX = 80;
	private static final int MINUTES_IN_PERIOD = 1440;
	private static final int NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE = 1; // can go up or down to reach 80
	
	private DaoService daoService = new DaoService();
	
	private void loadData() throws FileNotFoundException {
		daoService.loadData();
	}
	
	private static String readNextLine(Scanner scanner) {
		String line = scanner.nextLine();
		return line.replaceAll(" +", " ").trim();
	}

	public double[] generateGraphPoints(List<ActionDetail> actionDetails) {
		double[] glycemicIndexAtOffset = new double[MINUTES_IN_PERIOD];
		// ArrayList<List<ActionDetail>> actionsImpactingAtOffset = new ArrayList<List<ActionDetail>>(MINUTES_IN_PERIOD);
		
		for (int i = 0; i < MINUTES_IN_PERIOD; i++) {
			glycemicIndexAtOffset[i] = BASE_GLYCEMIC_INDEX;
		}
		
		Collections.sort(actionDetails);
		
		ActionDetail prevEatAction = null;
		ActionDetail prevExerciseAction = null;
		for (ActionDetail currentAction : actionDetails) {
			normalize(prevEatAction, prevExerciseAction, currentAction, glycemicIndexAtOffset);
			int startTime = currentAction.getTimeOffsetInMin();
			for (int i = 1; i <= currentAction.getAction().getAffectMin(); i++) {
				if (glycemicIndexAtOffset[startTime+i] == BASE_GLYCEMIC_INDEX) {
					glycemicIndexAtOffset[startTime+i] = glycemicIndexAtOffset[startTime] + (i * currentAction.getEntity().getGlycemicIndexChangeRate());
				}
				else {
					glycemicIndexAtOffset[startTime+i] = glycemicIndexAtOffset[startTime+i] + (i * currentAction.getEntity().getGlycemicIndexChangeRate());
				}
			}
			if (currentAction.getAction() == ActionEnum.EAT) {
				prevEatAction = currentAction;
			}
			if (currentAction.getAction() == ActionEnum.EXERCISE) {
				prevExerciseAction = currentAction;
			}
		}
		normalize(prevEatAction, prevExerciseAction, null, glycemicIndexAtOffset);

		int printStartTime = 0;
		if (actionDetails.size() > 0) {
			printStartTime = actionDetails.get(0).getTimeOffsetInMin();
		}
		for (int i = printStartTime; i < printStartTime + 360; i++) {
			System.out.println(Utils.getTimeFromOffset(i) + "\t" + glycemicIndexAtOffset[i]);
		}
		
		return glycemicIndexAtOffset;
	}
	
	private void normalize(ActionDetail prevEatAction, ActionDetail prevExerciseAction, ActionDetail currentAction, double[] timeGlycemicIndex) {
		// array was initialized with BASE_GLYCEMIC_INDEX. no actions means glycemicIndex will be maintained at 80
		if (prevEatAction == null && prevExerciseAction == null) {
			return;
		}
		
		int normalizationStartTime = 0;
		if (prevEatAction == null && prevExerciseAction != null) {
			normalizationStartTime = prevExerciseAction.getTimeOffsetInMin() + prevExerciseAction.getAction().getAffectMin();
		}
		else if (prevEatAction != null && prevExerciseAction == null) {
			normalizationStartTime = prevEatAction.getTimeOffsetInMin() + prevEatAction.getAction().getAffectMin();
		}
		else if (prevEatAction != null && prevExerciseAction != null) {
			 normalizationStartTime = Math.max(prevEatAction.getTimeOffsetInMin() + prevEatAction.getAction().getAffectMin(),
					  prevExerciseAction.getTimeOffsetInMin() + prevExerciseAction.getAction().getAffectMin());
		}
		
		int normalizationEndTime = MINUTES_IN_PERIOD - 1;
		if (currentAction != null) {
			normalizationEndTime = currentAction.getTimeOffsetInMin();
		}
		
		if (normalizationStartTime < normalizationEndTime) {
			for (int i = normalizationStartTime+1; i <= normalizationEndTime; i++) {
				if (timeGlycemicIndex[i-1] > BASE_GLYCEMIC_INDEX) {
					timeGlycemicIndex[i] = timeGlycemicIndex[i-1] - NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE;
				}
				else if (timeGlycemicIndex[i-1] < BASE_GLYCEMIC_INDEX) {
					timeGlycemicIndex[i] = timeGlycemicIndex[i-1] + NO_ACTION_GLYCEMIC_INDEX_CHANGE_RATE;
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		BloodSugarSimulator bss = new BloodSugarSimulator();
		bss.loadData();
		
		List<ActionDetail> actionDetails = new ArrayList<ActionDetail>();
		System.out.println("Type \"END\" to end data input");
		Scanner scanner = new Scanner(new File("input/input3.dat"));
		String line = readNextLine(scanner);
		
		while (!"end".equalsIgnoreCase(line)) {
			System.out.println("line=" + line + ".");
			if (StringUtils.isEmpty(line)) {
				line = readNextLine(scanner);
				continue;
			}
			
			boolean isInputValid = true;
			String[] actionDetailInput = line.split(" ");
			if (actionDetailInput.length < 3) { // other columns for comments for testing
				isInputValid = false;
				System.out.println("10. invalid input. length < 3"); // TODO better msg
			}
			
			String time = null;
			ActionEnum action = null;
			ImpactEntity e = null; 

			if (isInputValid) {
				time = actionDetailInput[0];
				isInputValid = Utils.isValidTimeFormat(time);
			}

			if (!isInputValid) {
				System.out.println("invalid time = " + actionDetailInput[0]); // TODO better msg
			}
			else {
				action = ActionEnum.getEnum(actionDetailInput[1]);
				if (action == null) {
					isInputValid = false;
					System.out.println("invalid action=" + actionDetailInput[1]); // TODO better msg
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
					System.out.println("invalid food/exercise"); // TODO better msg
				}
			}

			if (isInputValid) {
				actionDetails.add(new ActionDetail(time, action, e));
			}
			line = readNextLine(scanner);
		}
		System.out.println("Thank You for being Health Conscious!");
		
		bss.generateGraphPoints(actionDetails);
		scanner.close();
	}

}
