package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day4First {

	private String input = null;
	private List<GuardEntry> inputGuardEntries = null;

	public Day4First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day4First().processInput());
	}

	private String processInput() {
		Integer guardId = -1;
		int fallAsleepTime = -1;
		int wakeUpTime = -1;
		Map<Integer, List<SleepInteval>> sleepIntervalsMap = new HashMap<>();
		for (GuardEntry guardEntry : inputGuardEntries) {
			String rawInstructionEntry = guardEntry.getRawInstructionEntry();

			if (rawInstructionEntry.indexOf("Guard #") != -1) {
				if (guardId != -1) {
					loadGuardEntry(guardId, fallAsleepTime, wakeUpTime, sleepIntervalsMap);
				}

				// reset times
				fallAsleepTime = -1;
				wakeUpTime = -1;

				int indexOfGuardId = rawInstructionEntry.indexOf("Guard #") + "Guard #".length();
				guardId = Integer.parseInt(rawInstructionEntry.substring(indexOfGuardId, rawInstructionEntry.indexOf(" ", indexOfGuardId)));
				continue;
			}

			if (rawInstructionEntry.indexOf("falls asleep") != -1) {
				fallAsleepTime = Math.toIntExact(guardEntry.getTime());
			}

			if (rawInstructionEntry.indexOf("wakes up") != -1) {
				wakeUpTime = Math.toIntExact(guardEntry.getTime());
				loadGuardEntry(guardId, fallAsleepTime, wakeUpTime, sleepIntervalsMap);
				fallAsleepTime = -1;
				wakeUpTime = -1;
			}
		}

		Integer guardMaxSlept = null;
		int maxSleptTime = -1;
		for (Entry<Integer, List<SleepInteval>> entry : sleepIntervalsMap.entrySet()) {
			int sleptTime = calculateSleptTime(entry.getValue());
			if (sleptTime > maxSleptTime) {
				maxSleptTime = sleptTime;
				guardMaxSlept = entry.getKey();
			}
		}

		if (guardMaxSlept == null) {
			// this shouldn't happen
			return "Failed to identify guard with max slept time";
		}

		List<SleepInteval> guardSleepIntervals = sleepIntervalsMap.get(guardMaxSlept);
		Map<Integer, Integer> minutesSleepMap = new HashMap<>();
		for (int i = 0; i <= 59; i++) {
			minutesSleepMap.put(i, 0);
		}
		for (SleepInteval sleepInteval : guardSleepIntervals) {
			for (int i = sleepInteval.getStart(); i < sleepInteval.getEnd(); i++) {
				minutesSleepMap.put(i, minutesSleepMap.get(i) + 1);
			}
		}
		int maxMinuteSlept = -1;
		int maxMinute = -1;
		for (Entry<Integer, Integer> sleepInteval : minutesSleepMap.entrySet()) {
			if (sleepInteval.getValue() > maxMinuteSlept) {
				maxMinuteSlept = sleepInteval.getValue();
				maxMinute = sleepInteval.getKey();
			}
		}

		return "The guard " + guardMaxSlept + " slept a total of " + maxSleptTime + " minutes and in the minute " + maxMinute + " slept a maximum of "
				+ maxMinuteSlept + " times. Multiply result is " + (guardMaxSlept * maxMinute);
	}

	private void loadGuardEntry(Integer guardId, int fallAsleepTime, int wakeUpTime, Map<Integer, List<SleepInteval>> sleepIntervalsMap) {
		// we were already processing a guard
		// if the guard didn't wake up or fall asleep in the 1 hour interval consider the 0 and 60th minute
		if (fallAsleepTime == -1 && wakeUpTime == -1) {
			// already processed.
			return;
		}

		if (fallAsleepTime == -1) {
			fallAsleepTime = 0;
		}
		if (wakeUpTime == -1) {
			wakeUpTime = 60;
		}

		if (sleepIntervalsMap.get(guardId) != null) {
			sleepIntervalsMap.get(guardId).add(new SleepInteval(fallAsleepTime, wakeUpTime));
		} else {
			List<SleepInteval> sleepList = new ArrayList<>();
			sleepList.add(new SleepInteval(fallAsleepTime, wakeUpTime));
			sleepIntervalsMap.put(guardId, sleepList);
		}
	}

	private int calculateSleptTime(List<SleepInteval> value) {
		if (value == null || value.size() == 0) {
			return 0;
		}
		int total = 0;
		for (SleepInteval sleepInteval : value) {
			total += sleepInteval.getEnd() - sleepInteval.getStart();
		}
		return total;
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			inputGuardEntries = new ArrayList<>();

			// just for fun don't use Dates
			List<String> inputRawRowsList = Arrays.asList(input.split("\n"));
			for (String rawRow : inputRawRowsList) {
				GuardEntry parsedGuardEntry = parseGuardEntry(rawRow);
				if (parsedGuardEntry == null) {
					// parse failed we should log or something but no time
					continue;
				}

				inputGuardEntries.add(parsedGuardEntry);
			}

			Collections.sort(inputGuardEntries);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private GuardEntry parseGuardEntry(String rawRow) {
		if (rawRow == null || rawRow.indexOf("[") == -1 || rawRow.indexOf("]") == -1 || rawRow.indexOf(":") == -1) {
			System.out.println("Invalid row: " + rawRow);
			return null;
		}
		int indexOpenPar = rawRow.indexOf("[");
		int indexClosePar = rawRow.indexOf("]", indexOpenPar);
		int indexDateTimeSeparator = rawRow.indexOf(" ", indexOpenPar);
		String dateText = rawRow.substring(indexOpenPar + 1, indexDateTimeSeparator).replace("-", "");
		String timeText = rawRow.substring(indexDateTimeSeparator + 1, indexClosePar).replace(":", "");
		String instructionText = rawRow.substring(indexClosePar + 1).trim();
		Long parsedDate = Long.parseLong(dateText);
		Long parsedTime = Long.parseLong(timeText);

		GuardEntry guardEntry = new GuardEntry(parsedDate, parsedTime, instructionText);
		return guardEntry;
	}

	private String getInputLocation() {
		return "inputs/day_4.txt";
	}

	class SleepInteval {
		private int start;
		private int end;

		public SleepInteval() {
		}

		public SleepInteval(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

	}

	class GuardEntry implements Comparable<GuardEntry> {
		private Long date;
		private Long time;
		private String rawInstructionEntry;

		public GuardEntry(Long date, Long time, String rawInstructionEntry) {
			super();
			this.date = date;
			this.time = time;
			this.rawInstructionEntry = rawInstructionEntry;
		}

		@Override
		public int compareTo(GuardEntry guardTwo) {
			// no null checks, no time
			int dateCompareValue = date.compareTo(guardTwo.getDate());
			if (dateCompareValue == 0) {
				return time.compareTo(guardTwo.getTime());
			} else {
				return dateCompareValue;
			}
		}

		public Long getDate() {
			return date;
		}

		public void setDate(Long date) {
			this.date = date;
		}

		public Long getTime() {
			return time;
		}

		public void setTime(Long time) {
			this.time = time;
		}

		public String getRawInstructionEntry() {
			return rawInstructionEntry;
		}

		public void setRawInstructionEntry(String rawInstructionEntry) {
			this.rawInstructionEntry = rawInstructionEntry;
		}

	}

}
