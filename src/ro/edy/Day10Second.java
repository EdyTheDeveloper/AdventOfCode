package ro.edy;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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
public class Day10Second {

	private String input = null;
	private List<Point> pointsList = new ArrayList<>();
	private int minimumX = 0;
	private int minimumY = 0;
	private int maximumX = 0;
	private int maximumY = 0;

	public Day10Second() {
		initializeInput();
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day10Second().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		int second = 0;
		Writer writer = new FileWriter(new File("g:/mesaj.txt"));
		int maximumConnections = 0;
		while (matrixHasPointsInIt()) {
			second++;

			for (Point point : pointsList) {
				point.setX(point.getX() + point.getVelocityX());
				point.setY(point.getY() + point.getVelocityY());
			}

			int calculatedConnections = calculateConnections();
			if (calculatedConnections == 1776) {
				// maximumConnections = calculatedConnections;
				return second + "";
			}
		}

		writer.flush();
		writer.close();

		return maximumConnections + "";
	}

	private int calculateConnections() {
		int connections = 0;
		for (Point point : pointsList) {
			for (Point point2 : pointsList) {
				if (point.equals(point2)) {
					continue;
				}

				if (Math.abs(point.getX() - point2.getX()) <= 1 && Math.abs(point.getY() - point2.getY()) <= 1) {
					connections++;
				}
			}
		}
		return connections;
	}

	private boolean matrixHasPointsInIt() {
		for (Point point : pointsList) {
			if (point.getX() >= minimumX && point.getX() <= maximumX && point.getY() >= minimumY
					&& point.getY() <= maximumY) {
				return true;
			}
		}
		return false;
	}

	private void printMatrix(Writer writer) throws Exception {
		int tmpMinX = Integer.MAX_VALUE;
		int tmpMinY = Integer.MAX_VALUE;
		int tmpMaxX = Integer.MIN_VALUE;
		int tmpMaxY = Integer.MIN_VALUE;
		for (Point point : pointsList) {
			if (point.getX() < tmpMinX) {
				tmpMinX = point.getX();
			}
			if (point.getY() < tmpMinY) {
				tmpMinY = point.getY();
			}
			if (point.getX() > tmpMaxX) {
				tmpMaxX = point.getX();
			}
			if (point.getY() > tmpMaxY) {
				tmpMaxY = point.getY();
			}
		}

		for (int j = tmpMinY; j <= tmpMaxY; j++) {
			for (int i = tmpMinX; i <= tmpMaxX; i++) {
				if (pointsList.contains(new Point(i, j, 0, 0))) {
					writer.write("#");
				} else {
					writer.write(" ");
				}
			}

			writer.write("\r\n");
		}
		
		writer.write("\r\n");
		writer.write("\r\n");
		writer.write("\r\n");
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> linesRaw = Arrays.asList(input.split("\n"));
			for (String line : linesRaw) {
				int x = Integer.parseInt(
						line.substring(line.indexOf("position=<") + "position=<".length(), line.indexOf(",")).trim());
				int y = Integer.parseInt(line.substring(line.indexOf(",") + ",".length(), line.indexOf(">")).trim());
				int velocityX = Integer.parseInt(line.substring(line.indexOf("velocity=<") + "velocity=<".length(),
						line.indexOf(",", line.indexOf("velocity=<"))).trim());
				int velocityY = Integer.parseInt(line.substring(line.indexOf(",", line.indexOf("velocity=<")) + 1,
						line.indexOf(">", line.indexOf("velocity=<"))).trim());

				Point point = new Point(x, y, velocityX, velocityY);
				pointsList.add(point);

				if (x > maximumX) {
					maximumX = x;
				}
				if (y > maximumY) {
					maximumY = y;
				}
				if (x < minimumX) {
					minimumX = x;
				}
				if (y < minimumY) {
					minimumY = y;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getInputLocation() {
		return "inputs/day_10.txt";
	}

	class Point {
		private int x;
		private int y;
		private int velocityX;
		private int velocityY;

		public Point(int x, int y, int velocityX, int velocityY) {
			super();
			this.x = x;
			this.y = y;
			this.velocityX = velocityX;
			this.velocityY = velocityY;
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

		public int getVelocityX() {
			return velocityX;
		}

		public int getVelocityY() {
			return velocityY;
		}

		private Day10Second getOuterType() {
			return Day10Second.this;
		}

	}
}
