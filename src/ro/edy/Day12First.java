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
public class Day12First {

	private String input = null;
	private List<Note> notesProducePlantList = new ArrayList<>();

	public Day12First() {
		initializeInput();
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		System.out.println("Result: " + new Day12First().processInput());
		long t1 = System.currentTimeMillis();
		System.out.println("Processing took: " + (t1 - t0) + " ms");
	}

	private String processInput() throws Exception {
		int generation = 0;
		int firstIndex = 0;
		while (generation < 20) {
			generation++;
			input = "...." + input + "....";
			firstIndex = firstIndex - 4;
			char[] charArray = input.toCharArray();
			char[] newCharArray = input.toCharArray();
			for (int i = 0; i < charArray.length - 4; i++) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 5; j++) {
					builder.append(charArray[i + j]);
				}
				if (notesProducePlantList.contains(new Note(builder.toString(), false))) {
					newCharArray[i+2] = '#';
				} else {
					newCharArray[i+2] = '.';
				}
				input = new String(newCharArray);
			}
			System.out.println(input);
		}

		int currentIndex = firstIndex;
		char[] charArray = input.toCharArray();
		int sum = 0;
		for (int i = 0; i < charArray.length; i++) {
			if ( (charArray[i] + "").equals("#") ) {
				sum += currentIndex;
			}
			currentIndex++;
		}
		return "Result: " + sum;
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> linesRaw = Arrays.asList(input.split("\n"));
			boolean firstLine = true;
			for (String line : linesRaw) {
				if (line.trim().length() == 0) {
					continue;
				}

				if (firstLine) {
					input = line.substring("initial state: ".length());
					firstLine = false;
					continue;
				}

				String patternNote = line.substring(0, line.indexOf("=>")).trim();
				boolean producesPlant = line.substring(line.indexOf("=>") + 2).trim().equals("#");
//				notesList.add(new Note(patternNote, producesPlant));
				if (producesPlant) {
					notesProducePlantList.add(new Note(patternNote, producesPlant));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class Note {
		private String note;
		private boolean producesPlant = false;

		public Note(String note, boolean producesPlant) {
			super();
			this.note = note;
			this.producesPlant = producesPlant;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((note == null) ? 0 : note.hashCode());
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
			Note other = (Note) obj;
			if (note == null) {
				if (other.note != null)
					return false;
			} else if (!note.equals(other.note))
				return false;
			return true;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public boolean isProducesPlant() {
			return producesPlant;
		}

		public void setProducesPlant(boolean producesPlant) {
			this.producesPlant = producesPlant;
		}

	}

	private String getInputLocation() {
		return "inputs/day_12.txt";
	}

}
