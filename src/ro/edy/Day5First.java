package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day5First {

	private String input = null;

	public Day5First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day5First().processInput());
	}

	private String processInput() {
		// Logic here is: mark the combining elementes as '-' (not to use string, substring or any other heavy processing), and when we find a match go back the
		// index to recheck the elements, doing it in a loop to catch all chain reactions
		char[] inputArray = input.toCharArray();
		for (int i = 0; i < inputArray.length; i++) {
			boolean isReacted = false;
			do {
				int nextIndex = getNextIndex(i, inputArray);
				if (nextIndex == -1) {
					break;
				}
				if (areReacting(i, nextIndex, inputArray)) {
					inputArray[i] = '-';
					inputArray[nextIndex] = '-';
					int previousIndex = getPreviousIndex(i, inputArray);
					if (previousIndex != -1) {
						i = previousIndex;
					}
					isReacted = true;
				} else {
					isReacted = false;
				}
			} while (isReacted);
		}
		int numberOfElements = 0;
		for (int i = 0; i < inputArray.length; i++) {
			if (inputArray[i] != '-') {
				++numberOfElements;
			}
		}

		return "Number of elements remained: " + numberOfElements;
	}

	private int getPreviousIndex(int index, char[] inputArray) {
		if (index < 0) {
			return -1;
		}
		for (int j = index; j >= 0; j--) {
			if (inputArray[j] != '-') {
				return j;
			}
		}
		return -1;
	}

	private int getNextIndex(int index, char[] inputArray) {
		if (index + 1 >= inputArray.length) {
			return -1;
		}
		for (int j = index + 1; j < inputArray.length; j++) {
			if (inputArray[j] != '-') {
				return j;
			}
		}
		return -1;
	}

	private boolean areReacting(int i, int j, char[] inputArray) {
		if (i > inputArray.length || j > inputArray.length) {
			return false;
		}
		String first = String.valueOf(inputArray[i]);
		String second = String.valueOf(inputArray[j]);
		if (first.equalsIgnoreCase(second) && !first.equals(second)) {
			return true;
		}
		return false;
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getInputLocation() {
		return "inputs/day_5.txt";
	}

}
