package com.healthiq.takehome;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.healthiq.takehome.bo.ActionDetail;
import com.healthiq.takehome.bo.ImpactEntity;
import com.healthiq.takehome.dto.GraphPoint;
import com.healthiq.takehome.enums.ActionEnum;
import com.healthiq.takehome.utils.Utils;

// TODO replace System.out.println with log4j
// Assumption - BloodSugar does not fall below 80
// TODO comment all classes

public class BloodSugarSimulator {
	
	private static final int BASE_GLYCEMIC_INDEX = 80;
	
	private DaoService daoService = new DaoService();
	
	private void loadData() throws FileNotFoundException {
		daoService.loadData();
	}
	
	private static String readNextLine(Scanner scanner) {
		String line = scanner.nextLine();
		return line.replaceAll(" +", " ").trim();
	}

	public Map<Date, Double> generateGraphPoints(List<ActionDetail> actionDetails) {
		Date today = DateUtils.truncate(new Date(), Calendar.DATE);
		System.out.println("today=" + today);
		
		PriorityQueue<ActionDetail> events = new PriorityQueue<>();
		Map<Date, Double> points = new LinkedHashMap<Date, Double>();

		events.add(new ActionDetail(0));
		for (ActionDetail ad : actionDetails) {
			events.add(ad);
			events.add(new ActionDetail(ad.getTimeOffsetInMin() + ad.getAction().getAffectMin()));
		}
		events.add(new ActionDetail(1440));

		double currentGlycemicIndex = BASE_GLYCEMIC_INDEX;
		double glycemicIndexIncreaseRate = 0;
		
		int previousTime = 0;
		while (!events.isEmpty()) {
			ActionDetail ad = events.poll();
			if (ad.getAction() == null) {
				// calculate current values and based on the change
				int elapsedTime = ad.getTimeOffsetInMin() - previousTime;
				currentGlycemicIndex = currentGlycemicIndex + (glycemicIndexIncreaseRate * elapsedTime);
				currentGlycemicIndex = Math.max(BASE_GLYCEMIC_INDEX, currentGlycemicIndex);
				Date currentTime = DateUtils.addMinutes(today, ad.getTimeOffsetInMin());
				points.put(currentTime, currentGlycemicIndex); 
				
				// calculate new rate. if there are multiple events, calculate the net increase rate
				if (previousTime == ad.getTimeOffsetInMin()) {
					
					
				}
				else {
					previousTime = ad.getTimeOffsetInMin();
				}
				
			}
			
			System.out.println(events.poll());
		}
		
		return points;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		BloodSugarSimulator bss = new BloodSugarSimulator();
		bss.loadData();
		
		List<ActionDetail> actionDetails = new ArrayList<ActionDetail>();
		System.out.println("Type \"END\" to end data input");
		Scanner scanner = new Scanner(new File("input/input.dat"));
		String line = readNextLine(scanner);
		
		while (!"end".equalsIgnoreCase(line)) {
			System.out.println("line=" + line + ".");
			if (StringUtils.isEmpty(line)) {
				line = readNextLine(scanner);
				continue;
			}
			
			if ("draw".equals(line)) {
				// draw
			}
			else {
				boolean isInputValid = true;
				String[] actionDetailInput = line.split(" ");
				if (actionDetailInput.length < 3) { // other columns for comments for testing
					isInputValid = false;
					System.out.println("10. invalid input. length < 3");
				}
				
				String time = null;
				ActionEnum action = null;
				ImpactEntity e = null; 

				if (isInputValid) {
					time = actionDetailInput[0];
					isInputValid = Utils.isValidTimeFormat(time);
				}

				if (!isInputValid) {
					System.out.println("invalid time = " + actionDetailInput[0]);
				}
				else {
					action = ActionEnum.getEnum(actionDetailInput[1]);
					if (action == null) {
						isInputValid = false;
						System.out.println("30. invalid action=" + actionDetailInput[1]); // TODO better msg
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
						System.out.println("40. invalid food/exercise"); // TODO better msg
					}
				}

				if (isInputValid) {
					actionDetails.add(new ActionDetail(time, action, e));
				}
			}
			line = readNextLine(scanner);
		}
		System.out.println("Thank You for being Health Conscious!");
		
		bss.generateGraphPoints(actionDetails);
		scanner.close();
	}

}
