package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day1Second {

	private String input = null;

	public Day1Second() {
		initializeInput();
	}

	public static void main(String[] args) {
		long t0 = System.currentTimeMillis();
		System.out.println(new Day1Second().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took " + (t1 - t0) + " millis");
	}

	protected String processInput() {
		List<Integer> integerInputsList = Arrays.stream(input.split("\n")).map(Integer::parseInt)
				.collect(Collectors.toList());
		// Sorted set allows for faster searching in frequency history - duhhh :)
		Set<Integer> frequencyHistoryList = new TreeSet<>();

		int frequency = 0;
		// use of 100_000_000 is as safety measure for infinite loop only
		for (int i = 0; i < 100_000_000; i++) {
			Integer integer = integerInputsList.get(i % integerInputsList.size());
			if (integer == null) {
				continue;
			}

			frequency += integer;
			if (frequencyHistoryList.contains(frequency)) {
				return frequency + "";
			}
			frequencyHistoryList.add(frequency);
		}
		return "No duplicate found in 100_000_000 checks";
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
		return "inputs/day_1_2.txt";
	}

}
