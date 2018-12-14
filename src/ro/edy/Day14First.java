package ro.edy;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day14First {

	private int inputNumberOfRecipes = 170641;

	public Day14First() {
		// initializeInput();
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day14First().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		List<Integer> recipesScoresList = new ArrayList<>();
		recipesScoresList.add(3);
		recipesScoresList.add(7);
		int firstElfIndex = 0;
		int secondElfIndex = 1;

		while (recipesScoresList.size() < inputNumberOfRecipes + 10) {
			int sum = recipesScoresList.get(firstElfIndex) + recipesScoresList.get(secondElfIndex);
			if (sum < 10) {
				recipesScoresList.add(sum);
			} else {
				recipesScoresList.add(sum / 10);
				recipesScoresList.add(sum % 10);
			}

			firstElfIndex = (firstElfIndex + (recipesScoresList.get(firstElfIndex) + 1)) % recipesScoresList.size();
			secondElfIndex = (secondElfIndex + (recipesScoresList.get(secondElfIndex) + 1)) % recipesScoresList.size();
		}

		for (int i = inputNumberOfRecipes; i < inputNumberOfRecipes + 10; i++) {
			System.out.print(recipesScoresList.get(i));
		}
		System.out.println();

		return "Result: ";
	}

}
