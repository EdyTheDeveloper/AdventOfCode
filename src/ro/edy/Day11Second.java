package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day11Second {

	private String input = null;
	private int gridSerialNumber = 8444;

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day11Second().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		int[][] fuelMatrix = new int[300][300];
		for (int i = 0; i < 300; i++) {
			for (int j = 0; j < 300; j++) {
				fuelMatrix[i][j] = calculateEnergyForCell(i, j);
			}
		}

		int maxFuel = 0;
		int maxFuelX = -1;
		int maxFuelY = -1;
		int maxGridSize = 0;
		int[][] topFuelMatrix = new int[300][300];

		for (int gridSize = 1; gridSize <= 300; gridSize++) {
			for (int i = 0; i < 300 - (gridSize - 1); i++) {
				for (int j = 0; j < 300 - (gridSize - 1); j++) {
					topFuelMatrix[i][j] = calculateTopFuel(i, j, fuelMatrix, gridSize);
					if (topFuelMatrix[i][j] > maxFuel) {
						maxFuel = topFuelMatrix[i][j];
						maxFuelX = i;
						maxFuelY = j;
						maxGridSize = gridSize;
					}
				}
			}
			if (gridSize % 30 == 0) {
				System.out.println("- " + gridSize);
			}
		}

		return "Top fuel cell is at position: " + maxFuelX + ", " + maxFuelY + ", " + maxGridSize;
	}

	private int calculateTopFuel(int x, int y, int[][] fuelMatrix, int gridSize) {
		int sum = 0;
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				sum += fuelMatrix[x + i][y + j];
			}
		}
		return sum;
	}

	private int calculateEnergyForCell(int x, int y) {
		int rackId = x + 10;
		int powerLevel = rackId * y;
		powerLevel = powerLevel + gridSerialNumber;
		powerLevel = powerLevel * rackId;
		powerLevel = ((powerLevel / 100) % 10);
		powerLevel = powerLevel - 5;
		return powerLevel;
	}

}
