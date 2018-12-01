package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1First {

	private String input = null;

	public Day1First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println(new Day1First().processInput());
	}

	protected String processInput() {
		List<Integer> ints = Arrays.stream(input.split("\n")).map(Integer::parseInt).collect(Collectors.toList());

		int frequency = 0;
		for (Integer integer : ints) {
			if (integer == null) {
				continue;
			}
			frequency += integer;
		}
		return frequency + "";
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
		return "inputs/day_1_1.txt";
	}

}
