package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 * This implementation is a mess, do it better.... 
 *
 */
public class Day13First {

	private String input = null;
	private List<Track> tracksList = new ArrayList<>();
	private List<Cart> cartsList = new ArrayList<>();

	public Day13First() {
		initializeInput();
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day13First().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		Point collisionPoint = null;
		while (collisionPoint == null) {
			for (Cart cart : cartsList) {
				Track trackForCart = tracksList.get(tracksList.indexOf(new Track(cart.getPoint())));

				Track newTrackForCart = null;
				if (trackForCart.getType().equals("-")) {
					if (cart.getDirection().equals(">")) {
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX() + 1, cart.getPoint().getY()))));
					} else if (cart.getDirection().equals("<")) {
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX() - 1, cart.getPoint().getY()))));
					} else {
						System.out.println("Invalid direction " + cart.getDirection());
					}
				} else if (trackForCart.getType().equals("|")) {
					if (cart.getDirection().equals("^")) {
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
					} else if (cart.getDirection().equals("v")) {
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
					} else {
						System.out.println("Invalid direction " + cart.getDirection());
					}
				} else if (trackForCart.getType().equals("/")) {
					if (cart.getDirection().equals("^")) {
						cart.setDirection(">");
						Point pointToGoTo = null;
						if (cart.getPreviousPoint().getX() + 1 == cart.getPoint().getX()) {
							pointToGoTo = new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1);
						} else {
							pointToGoTo = new Point(cart.getPoint().getX() + 1, cart.getPoint().getY());
						}
						newTrackForCart = tracksList.get(tracksList.indexOf(new Track(pointToGoTo)));
					} else if (cart.getDirection().equals("<")) {
						cart.setDirection("v");
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
					} else if (cart.getDirection().equals(">")) {
						cart.setDirection("^");
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
					} else if (cart.getDirection().equals("v")) {
						cart.setDirection("<");

						Point pointToGoTo = null;
						if (cart.getPreviousPoint().getX() - 1 == cart.getPoint().getX()) {
							pointToGoTo = new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1);
						} else {
							pointToGoTo = new Point(cart.getPoint().getX() - 1, cart.getPoint().getY());
						}
						newTrackForCart = tracksList.get(tracksList.indexOf(new Track(pointToGoTo)));
					} else {
						System.out.println("Invalid direction " + cart.getDirection());
					}
				} else if (trackForCart.getType().equals("\\")) {
					if (cart.getDirection().equals("^")) {
						cart.setDirection("<");

						Point pointToGoTo = null;
						if (cart.getPreviousPoint().getX() - 1 == cart.getPoint().getX()) {
							pointToGoTo = new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1);
						} else {
							pointToGoTo = new Point(cart.getPoint().getX() - 1, cart.getPoint().getY());
						}
						newTrackForCart = tracksList.get(tracksList.indexOf(new Track(pointToGoTo)));
					} else if (cart.getDirection().equals("v")) {
						cart.setDirection(">");
						Point pointToGoTo = null;
						if (cart.getPreviousPoint().getX() + 1 == cart.getPoint().getX()) {
							pointToGoTo = new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1);
						} else {
							pointToGoTo = new Point(cart.getPoint().getX() + 1, cart.getPoint().getY());
						}
						newTrackForCart = tracksList.get(tracksList.indexOf(new Track(pointToGoTo)));
					} else if (cart.getDirection().equals("<")) {
						cart.setDirection("^");
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
					} else if (cart.getDirection().equals(">")) {
						cart.setDirection("v");
						newTrackForCart = tracksList.get(tracksList
								.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
					} else {
						System.out.println("Invalid direction " + cart.getDirection());
					}
				} else if (trackForCart.getType().equals("+")) {
					if (cart.getInterSectionNumber() % 3 == 0) {
						// first time turn left
						if (cart.getDirection().equals("v")) {
							cart.setDirection(">");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() + 1, cart.getPoint().getY()))));
						} else if (cart.getDirection().equals("^")) {
							cart.setDirection("<");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() - 1, cart.getPoint().getY()))));
						} else if (cart.getDirection().equals(">")) {
							cart.setDirection("^");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
						} else if (cart.getDirection().equals("<")) {
							cart.setDirection("v");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
						}
					} else if (cart.getInterSectionNumber() % 3 == 1) {
						// second time go straight
						if (cart.getDirection().equals("v")) {
							cart.setDirection("v");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
						} else if (cart.getDirection().equals("^")) {
							cart.setDirection("^");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
						} else if (cart.getDirection().equals(">")) {
							cart.setDirection(">");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() + 1, cart.getPoint().getY()))));
						} else if (cart.getDirection().equals("<")) {
							cart.setDirection("<");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() - 1, cart.getPoint().getY()))));
						}
					} else if (cart.getInterSectionNumber() % 3 == 2) {
						// third time go right
						if (cart.getDirection().equals("v")) {
							cart.setDirection("<");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() - 1, cart.getPoint().getY()))));
						} else if (cart.getDirection().equals("^")) {
							cart.setDirection(">");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX() + 1, cart.getPoint().getY()))));
						} else if (cart.getDirection().equals(">")) {
							cart.setDirection("v");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() + 1))));
						} else if (cart.getDirection().equals("<")) {
							cart.setDirection("^");
							newTrackForCart = tracksList.get(tracksList
									.indexOf(new Track(new Point(cart.getPoint().getX(), cart.getPoint().getY() - 1))));
						}
					}

					cart.incrementInterSectionNumber();
				}

				cart.setPoint(newTrackForCart.getPoint());
				collisionPoint = isCollision();
				if (collisionPoint != null) {
					break;
				}
			}

			Collections.sort(cartsList);
			collisionPoint = isCollision();
		}

		return "Result: " + collisionPoint.getX() + "," + collisionPoint.getY();

	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> linesRaw = Arrays.asList(input.split("\n"));
			int y = -1;
			for (String line : linesRaw) {
				y++;
				if (line.trim().length() == 0) {
					continue;
				}
				char[] charArray = line.toCharArray();
				for (int i = 0; i < charArray.length; i++) {
					if (charArray[i] == ' ') {
						continue;
					}

					if (charArray[i] == '/' || charArray[i] == '-' || charArray[i] == '\\' || charArray[i] == '+'
							|| charArray[i] == '|') {
						Track track = new Track(new Point(i, y), charArray[i] + "");
						tracksList.add(track);
					}

					if (charArray[i] == '^' || charArray[i] == 'v') {
						Track track = new Track(new Point(i, y), "|");
						tracksList.add(track);

						Cart cart = new Cart(new Point(i, y), charArray[i] + "");
						cartsList.add(cart);
					}
					if (charArray[i] == '<' || charArray[i] == '>') {
						Track track = new Track(new Point(i, y), "-");
						tracksList.add(track);

						Cart cart = new Cart(new Point(i, y), charArray[i] + "");
						cartsList.add(cart);
					}
				}
			}

			Collections.sort(cartsList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Point isCollision() {
		for (Cart cart : cartsList) {
			for (Cart cart2 : cartsList) {
				if (cart == cart2) {
					continue;
				}
				if (cart.getPoint().equals(cart2.getPoint())) {
					return cart.getPoint();
				}
			}
		}
		return null;
	}

	class Cart implements Comparable<Cart> {
		private Point point;
		private Point previousPoint;
		private String direction;
		private int interSectionNumber = 0;

		public Cart(Point point, String direction) {
			super();
			this.point = point;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return "Cart [point=" + point + ", direction=" + direction + ", interSectionNumber=" + interSectionNumber
					+ "]";
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
			Cart other = (Cart) obj;
			if (point == null) {
				if (other.point != null)
					return false;
			} else if (!point.equals(other.point))
				return false;
			return true;
		}

		public int getInterSectionNumber() {
			return interSectionNumber;
		}

		public void incrementInterSectionNumber() {
			interSectionNumber++;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.previousPoint = this.point;
			this.point = point;
		}

		public Point getPreviousPoint() {
			return previousPoint;
		}

		public void setPreviousPoint(Point previousPoint) {
			this.previousPoint = previousPoint;
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}

		@Override
		public int compareTo(Cart cart2) {
			int compareToY = Integer.valueOf(this.getPoint().getY())
					.compareTo(Integer.valueOf(cart2.getPoint().getY()));
			if (compareToY == 0) {
				return Integer.valueOf(this.getPoint().getX()).compareTo(Integer.valueOf(cart2.getPoint().getX()));
			}
			return compareToY;
		}

	}

	class Track {
		private Point point;
		private String type;

		public Track(Point point) {
			super();
			this.point = point;
		}

		public Track(Point point, String type) {
			super();
			this.point = point;
			this.type = type;
		}

		@Override
		public String toString() {
			return "Track [point=" + point + ", type=" + type + "]";
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
			if (obj instanceof Point) {
				if (point == null) {
					return false;
				} else if (!point.equals((Point) obj))
					return false;
			}

			if (getClass() != obj.getClass())
				return false;
			Track other = (Track) obj;
			if (point == null) {
				if (other.point != null)
					return false;
			} else if (!point.equals(other.point))
				return false;
			return true;
		}

		public List<Point> getPossibleConnectionPoints() {
			List<Point> points = new ArrayList<>();
			if (type.equals("|")) {
				if (this.point.getY() > 0) {
					Point newPoint = new Point(this.point.getX(), this.point.getY() - 1);
					points.add(newPoint);
				}
				Point newPoint = new Point(this.point.getX(), this.point.getY() + 1);
				points.add(newPoint);
			}
			if (type.equals("-")) {
				if (this.point.getX() > 0) {
					Point newPoint = new Point(this.point.getX() - 1, this.point.getY());
					points.add(newPoint);
				}
				Point newPoint = new Point(this.point.getX() + 1, this.point.getY());
				points.add(newPoint);
			}
			if (type.equals("/")) {
				Point newPoint = new Point(this.point.getX(), this.point.getY() + 1);
				points.add(newPoint);
				newPoint = new Point(this.point.getX() + 1, this.point.getY());
				points.add(newPoint);
			}
			if (type.equals("\\")) {
				if (this.point.getX() > 0) {
					Point newPoint = new Point(this.point.getX() - 1, this.point.getY());
					points.add(newPoint);
				}
				Point newPoint = new Point(this.point.getX(), this.point.getY() + 1);
				points.add(newPoint);
			}
			if (type.equals("+")) {
				if (this.point.getX() > 0) {
					Point newPoint = new Point(this.point.getX() - 1, this.point.getY());
					points.add(newPoint);
				}
				if (this.point.getY() > 0) {
					Point newPoint = new Point(this.point.getX(), this.point.getY() - 1);
					points.add(newPoint);
				}
				Point newPoint = new Point(this.point.getX() + 1, this.point.getY());
				points.add(newPoint);
				newPoint = new Point(this.point.getX(), this.point.getY() + 1);
				points.add(newPoint);
			}
			return points;
		}

		public Point getPoint() {
			return point;
		}

		public String getType() {
			return type;
		}

	}

	class Point {
		private int x;
		private int y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
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

		public int getY() {
			return y;
		}

	}

	private String getInputLocation() {
		return "inputs/day_13_2.txt";
	}

}
