package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day2Second {

	private String input = null;

	public Day2Second() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day2Second().processInput());
	}

	private String processInput() {
		List<String> inputIdsList = Arrays.asList(input.split("\n"));

		for (int i = 0; i < inputIdsList.size(); i++) {
			String currentId = inputIdsList.get(i);

			for (int j = i + 1; j < inputIdsList.size(); j++) {
				String idToCheck = inputIdsList.get(j);
				int extractedStringDifferencePosition = extractStringDifferencePosition(currentId, idToCheck);
				if (extractedStringDifferencePosition != -1) {
					return currentId.substring(0, extractedStringDifferencePosition)
							+ currentId.substring(extractedStringDifferencePosition + 1);
				}
			}
		}

		return "No match";
	}

	/**
	 * Test if two strings are similar, different by only one char at same position.
	 * 
	 * @param currentId - The first string
	 * @param idToCheck - The second string
	 * @return Returns -1 if the string are NOT similar or in case they are similar
	 *         return the different character position.
	 */
	public int extractStringDifferencePosition(String currentId, String idToCheck) {
		if (currentId.length() != idToCheck.length()) {
			return -1;
		}
		Integer differentChar = null;
		for (int i = 0; i < currentId.length(); i++) {
			char charFirst = currentId.charAt(i);
			char charSecond = idToCheck.charAt(i);

			if (charFirst == charSecond) {
				// chars are the same, ignore them, we do not search for duplicate IDs
				continue;
			}
			if (differentChar != null) {
				// Already have one char different, this is the second, no match
				return -1;
			}
			differentChar = i;
		}
		return differentChar;
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
		return "inputs/day_2.txt";
	}

}
