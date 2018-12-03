package ro.edy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day3First {

	private String input = null;
	private List<ElfClaim> inputElfClaimsList = null;

	public Day3First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day3First().processInput());
	}

	private String processInput() {
		// Not the best memory solution but for the challenge works fine. May be optimized to use much less memory (store coordinate intervals not the actual
		// points :) ) but let's just enjoy for now.
		Map<Point, String> intersectionPoints = new HashMap<>();
		for (int i = 0; i < inputElfClaimsList.size(); i++) {
			ElfClaim elfClaim = inputElfClaimsList.get(i);

			for (int j = i + 1; j < inputElfClaimsList.size(); j++) {
				ElfClaim elfClaimToCompareTo = inputElfClaimsList.get(j);

				Rectangle2D intersectionRectangle = elfClaim.getRectangle().createIntersection(elfClaimToCompareTo.getRectangle());

				if (intersectionRectangle.getWidth() > 0 && intersectionRectangle.getHeight() > 0) {
					// the two claims intersect
					addAllPointsFromRectangle(intersectionRectangle, intersectionPoints);
				}
			}
		}

		return intersectionPoints.size() + "";
	}

	private void addAllPointsFromRectangle(Rectangle2D rectangle, Map<Point, String> intersectionPoints) {
		if (rectangle.getWidth() <= 0 || rectangle.getHeight() <= 0) {
			return;
		}

		for (int i = 0; i < rectangle.getWidth(); i++) {
			for (int j = 0; j < rectangle.getHeight(); j++) {
				intersectionPoints.put(new Point((int) rectangle.getX() + i, (int) rectangle.getY() + j), "1");
			}
		}
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
