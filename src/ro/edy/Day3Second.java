package ro.edy;

import java.awt.Rectangle;
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
public class Day3Second {

	private String input = null;
	private List<ElfClaim> inputElfClaimsList = null;

	public Day3Second() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day3Second().processInput());
	}

	private String processInput() {
		for (int i = 0; i < inputElfClaimsList.size(); i++) {
			ElfClaim elfClaim = inputElfClaimsList.get(i);
			boolean overlaps = false;
			for (int j = 0; j < inputElfClaimsList.size(); j++) {
				if (i == j) {
					continue;
				}
				ElfClaim elfClaimToCompareTo = inputElfClaimsList.get(j);

				if (elfClaim.getRectangle().intersects(elfClaimToCompareTo.getRectangle())) {
					// the two claims intersect
					overlaps = true;
					break;
				}
			}
			if (!overlaps) {
				// Found the claim that doesn't overlap with other claim. Yey!
				return elfClaim.getId() + "";
			}
		}

		return "Could not find a claim that does not overlap";
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			inputElfClaimsList = new ArrayList<>();
			List<String> inputRawRowsList = Arrays.asList(input.split("\n"));
			for (String rawRow : inputRawRowsList) {
				ElfClaim parsedElfClaim = parseElfClaim(rawRow);
				if (parsedElfClaim != null) {
					inputElfClaimsList.add(parsedElfClaim);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private ElfClaim parseElfClaim(String rawRow) {
		if (rawRow == null || rawRow.indexOf("#") == -1 || rawRow.indexOf("@") == -1 || rawRow.indexOf(":") == -1) {
			System.out.println("Invalid row: " + rawRow);
			return null;
		}

		try {
			int id = Integer.parseInt(rawRow.substring(rawRow.indexOf("#") + 1, rawRow.indexOf(" ", rawRow.indexOf("#"))));
			String startPositionRaw = rawRow.substring(rawRow.indexOf("@") + 1, rawRow.indexOf(":", rawRow.indexOf("@"))).trim();
			int startPositionX = Integer.parseInt(startPositionRaw.substring(0, startPositionRaw.indexOf(",")));
			int startPositionY = Integer.parseInt(startPositionRaw.substring(startPositionRaw.indexOf(",") + 1));
			int width = Integer.parseInt(rawRow.substring(rawRow.indexOf(":") + 1, rawRow.indexOf("x", rawRow.indexOf(":"))).trim());
			int height = Integer.parseInt(rawRow.substring(rawRow.indexOf("x") + 1).trim());
			ElfClaim claim = new ElfClaim(id, new Rectangle(startPositionX, startPositionY, width, height));
			return claim;
		} catch (Exception ex) {
			System.out.println("Error parsing row <" + rawRow + "> because: " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

	private String getInputLocation() {
		return "inputs/day_3.txt";
	}

	class ElfClaim {
		private int id;
		private Rectangle rectangle;

		public ElfClaim(int id, Rectangle rectangle) {
			super();
			this.id = id;
			this.rectangle = rectangle;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Rectangle getRectangle() {
			return rectangle;
		}

		public void setRectangle(Rectangle rectangle) {
			this.rectangle = rectangle;
		}

	}

}
