package ro.edy;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day14Second {

	private int inputNumberOfRecipes = 170641;

	public Day14Second() {
		// initializeInput();
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day14Second().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		List<Integer> recipesScoresList = new ArrayList<>();
		recipesScoresList.add(3);
		recipesScoresList.add(7);
		int firstElfIndex = 0;
		int secondElfIndex = 1;
		StringBuilder recipeStringBuilder = new StringBuilder("37");

		while (true) {
			int sum = recipesScoresList.get(firstElfIndex) + recipesScoresList.get(secondElfIndex);
			if (sum < 10) {
				recipesScoresList.add(sum);
				recipeStringBuilder.append(sum);
			} else {
				recipesScoresList.add(sum / 10);
				recipeStringBuilder.append(sum / 10);
				recipesScoresList.add(sum % 10);
				recipeStringBuilder.append(sum % 10);
			}

			firstElfIndex = (firstElfIndex + (recipesScoresList.get(firstElfIndex) + 1)) % recipesScoresList.size();
			secondElfIndex = (secondElfIndex + (recipesScoresList.get(secondElfIndex) + 1)) % recipesScoresList.size();

			int numberToTheLeft = getNumberToTheLeft(recipeStringBuilder);
			if (numberToTheLeft >= 0) {
				return numberToTheLeft + "";
			}
		}
	}

	private int getNumberToTheLeft(StringBuilder recipeStringBuilder) {
		int indexOf = -1;
		if (recipeStringBuilder.length() > 34) {
			indexOf = recipeStringBuilder.indexOf(inputNumberOfRecipes + "", recipeStringBuilder.length() - 20);
		} else {
			indexOf = recipeStringBuilder.indexOf(inputNumberOfRecipes + "");
		}
		if (indexOf != -1) {
			return indexOf;
		}
		return -1;
	}

}
