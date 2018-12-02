package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
public class Day2First {

	private String input = null;

	public Day2First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day2First().processInput());
	}

	protected String processInput() {
		List<String> inputIdsList = Arrays.asList(input.split("\n"));

		int numberOfTwoOccurences = 0;
		int numberOfThreeOccurences = 0;
		Map<Character, Integer> occurenceCounterMap = new HashMap<>();
		for (String id : inputIdsList) {
			for (int i = 0; i < id.length(); i++) {
				char charAtPosition = id.charAt(i);
				if (occurenceCounterMap.get(charAtPosition) == null) {
					occurenceCounterMap.put(charAtPosition, 1);
				} else {
					occurenceCounterMap.put(charAtPosition, occurenceCounterMap.get(charAtPosition) + 1);
				}
			}

			boolean alreadyHadTwo = false;
			boolean alreadyHadThree = false;
			for (Entry<Character, Integer> entry : occurenceCounterMap.entrySet()) {
				if (!alreadyHadTwo && entry.getValue().equals(Integer.valueOf(2))) {
					alreadyHadTwo = true;
					numberOfTwoOccurences++;
				}
				if (!alreadyHadThree && entry.getValue().equals(Integer.valueOf(3))) {
					alreadyHadThree = true;
					numberOfThreeOccurences++;
				}
			}

			occurenceCounterMap.clear();
		}

		return (numberOfTwoOccurences * numberOfThreeOccurences) + "";
	}

	public void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected String getInputLocation() {
		return "inputs/day_2.txt";
	}

}
