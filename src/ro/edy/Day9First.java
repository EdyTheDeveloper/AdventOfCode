package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
public class Day9First {

	private String input = null;
	private int numberOfPlayers = -1;
	private int maxMarble = -1;

	public Day9First() {
		initializeInput();
	}

	public static void main(String[] args) {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day9First().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() {
		Map<Integer, Integer> playersScoreMap = new HashMap<>();
		for (int i = 0; i < numberOfPlayers; i++) {
			playersScoreMap.put(Integer.valueOf(i), 0);
		}

		List<Integer> marblesCircle = new ArrayList<Integer>();
		marblesCircle.add(0);
		marblesCircle.add(1);

		int position = 1;
		for (int marbleValue = 2; marbleValue <= maxMarble; marbleValue++) {
			int currentPlayer = (marbleValue + 2) % numberOfPlayers;

			if (marbleValue % 23 == 0) {
				playersScoreMap.put(currentPlayer, playersScoreMap.get(currentPlayer) + marbleValue);
				position = position - 7;
				if (position < 0) {
					position = marblesCircle.size() + position;
				}
				playersScoreMap.put(currentPlayer, playersScoreMap.get(currentPlayer) + marblesCircle.remove(position));
				continue;
			}

			position = position + 2;
			if (position > marblesCircle.size()) {
				position = position % (marblesCircle.size());
			}
			marblesCircle.add(position, marbleValue);
		}

		int winner = -1;
		int maxScore = 0;
		for (Entry<Integer, Integer> entry : playersScoreMap.entrySet()) {
			if (entry.getValue() > maxScore) {
				maxScore = entry.getValue();
				winner = entry.getKey();
			}
		}

		return "Player " + winner + " has " + maxScore + "";
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			numberOfPlayers = Integer.parseInt(input.substring(0, input.indexOf(" players;")));
			maxMarble = Integer.parseInt(
					input.substring(input.indexOf("is worth ") + "is worth ".length(), input.indexOf(" points")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getInputLocation() {
		return "inputs/day_9.txt";
	}

}
