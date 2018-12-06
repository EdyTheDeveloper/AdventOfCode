package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
public class Day6Second {

	private String input = null;
	private Point maxPoint = null;
	private List<Point> pointsList = new ArrayList<>();

	private static final int MAXIMUM_DISTANCE_ALLOWED = 10_000;

	public Day6Second() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day6Second().processInput());
	}

	private String processInput() {
		Set<DistancePoint> distancePoints = new TreeSet<>();
		// Calculate shortest distance for each point in matrix
		int areaSize = 0;
		for (int i = 0; i < maxPoint.getX(); i++) {
			for (int j = 0; j < maxPoint.getY(); j++) {
				Point testPoint = new Point(i, j);
				DistancePoint distancePoint = new DistancePoint(testPoint);
				distancePoints.add(distancePoint);

				for (Point point : pointsList) {
					int calculatedManhattanDistance = calculateManhattanDistance(point, testPoint);
					distancePoint.setDistanceToAllPoints(distancePoint.getDistanceToAllPoints() + calculatedManhattanDistance);
				}

				if (distancePoint.getDistanceToAllPoints() < MAXIMUM_DISTANCE_ALLOWED) {
					areaSize++;
				}
			}
		}

		return "Area size: " + areaSize;
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> inputRawList = Arrays.asList(input.split("\n"));
			maxPoint = loadPoints(inputRawList, pointsList);

			// determinInfinitePoints(pointsList);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private int calculateManhattanDistance(Point first, Point second) {
		int distance = Math.abs(first.getX() - second.getX()) + Math.abs(first.getY() - second.getY());
		return distance;
	}

	/**
	 * Load the point objects and return the max point of the matrix obtained by the given points
	 * 
	 * @param inputRawList
	 * @param pointsList
	 * @return
	 */
	private Point loadPoints(List<String> inputRawList, List<Point> pointsList) {
		int maxPointX = 0;
		int maxPointY = 0;
		for (String pointText : inputRawList) {
			int x = Integer.parseInt(pointText.substring(0, pointText.indexOf(",")));
			int y = Integer.parseInt(pointText.substring(pointText.indexOf(",") + 1).trim());
			pointsList.add(new Point(x, y));
			if (x > maxPointX) {
				maxPointX = x;
			}
			if (y > maxPointY) {
				maxPointY = y;
			}
		}
		return new Point(maxPointX, maxPointY);
	}

	private String getInputLocation() {
		return "inputs/day_6.txt";
	}

	class Point {
		private int x;
		private int y;
		private boolean infinite = false;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return x + ", " + y + ", " + infinite;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public boolean isInfinite() {
			return infinite;
		}

		public void setInfinite(boolean infinite) {
			this.infinite = infinite;
		}

	}

	/**
	 * This class stores distance information regarding a point in the matrix, distance information is: the closes input point, the distance to that input
	 * point, the shortest distance is duplicated meaning it is the same for two input points.
	 * 
	 * @author adrian.arnautu
	 *
	 */
	class DistancePoint implements Comparable<DistancePoint> {
		private Point point;
		private int distanceToAllPoints = 0;

		public DistancePoint(Point point) {
			super();
			this.point = point;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((point == null) ? 0 : point.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DistancePoint other = (DistancePoint) obj;
			if (point == null) {
				if (other.point != null)
					return false;
			} else if (!point.equals(other.point))
				return false;
			return true;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public int getDistanceToAllPoints() {
			return distanceToAllPoints;
		}

		public void setDistanceToAllPoints(int distanceToAllPoints) {
			this.distanceToAllPoints = distanceToAllPoints;
		}

		@Override
		public int compareTo(DistancePoint compareTo) {
			int compareToX = Integer.valueOf(this.getPoint().getX()).compareTo(Integer.valueOf(compareTo.getPoint().getX()));
			if (compareToX != 0) {
				return compareToX;
			}
			return Integer.valueOf(this.getPoint().getY()).compareTo(Integer.valueOf(compareTo.getPoint().getY()));
		}

	}

}
