package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day6First {

	private String input = null;
	private Point maxPoint = null;
	private List<Point> pointsList = new ArrayList<>();

	public Day6First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day6First().processInput());
	}

	private String processInput() {
		Set<DistancePoint> distancePoints = new TreeSet<>();
		// Calculate shortest distance for each point in matrix
		for (int i = 0; i < maxPoint.getX(); i++) {
			for (int j = 0; j < maxPoint.getY(); j++) {
				Point testPoint = new Point(i, j);
				DistancePoint distancePoint = new DistancePoint(testPoint);
				distancePoints.add(distancePoint);

				for (Point point : pointsList) {
					int calculatedManhattanDistance = calculateManhattanDistance(point, testPoint);
					if (calculatedManhattanDistance < distancePoint.getClosestDistance()) {
						distancePoint.setClosestDistance(calculatedManhattanDistance);
						distancePoint.setClosestPoint(point);
						distancePoint.setDuplicateSameDistance(false);
					} else if (calculatedManhattanDistance == distancePoint.getClosestDistance()) {
						distancePoint.setDuplicateSameDistance(true);
					}
				}

				if (i == 0 || j == 0 || i == maxPoint.getX() - 1 || j == maxPoint.getY() - 1) {
					// The input points on the margins (shortest distance for margin point) are infinite.
					distancePoint.getClosestPoint().setInfinite(true);
				}
			}
		}

		// Calculate area size for each input point (identified as getClosestPoint)
		Map<Point, Integer> numberOfShortestPointsMap = new HashMap<>();
		for (DistancePoint distancePoint : distancePoints) {
			if (distancePoint.isDuplicateSameDistance()) {
				continue;
			}
			if (distancePoint.getClosestPoint().isInfinite()) {
				continue;
			}

			if (numberOfShortestPointsMap.get(distancePoint.getClosestPoint()) == null) {
				numberOfShortestPointsMap.put(distancePoint.getClosestPoint(), 1);
			} else {
				numberOfShortestPointsMap.put(distancePoint.getClosestPoint(), numberOfShortestPointsMap.get(distancePoint.getClosestPoint()) + 1);
			}
		}

		Point maxPoint = null;
		int maxNumberOfPoints = 0;
		for (Entry<Point, Integer> entry : numberOfShortestPointsMap.entrySet()) {
			if (entry.getValue() > maxNumberOfPoints) {
				maxNumberOfPoints = entry.getValue();
				maxPoint = entry.getKey();
			}
		}
		System.out.println("Max point " + maxPoint.getX() + ", " + maxPoint.getY() + " has an area of " + maxNumberOfPoints);
		return "Max area: " + maxNumberOfPoints;
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
		private Point closestPoint;
		private int closestDistance = Integer.MAX_VALUE;
		private boolean duplicateSameDistance = false;

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

		public Point getClosestPoint() {
			return closestPoint;
		}

		public void setClosestPoint(Point closestPoint) {
			this.closestPoint = closestPoint;
		}

		public int getClosestDistance() {
			return closestDistance;
		}

		public void setClosestDistance(int closestDistance) {
			this.closestDistance = closestDistance;
		}

		public boolean isDuplicateSameDistance() {
			return duplicateSameDistance;
		}

		public void setDuplicateSameDistance(boolean duplicateSameDistance) {
			this.duplicateSameDistance = duplicateSameDistance;
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
