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
public class Day8Second {

	private String input = null;
	private List<Integer> inputNumbers = new ArrayList<>();
	private List<Node> allNodesList = new ArrayList<>();
	private Node rootNode = null;

	public Day8Second() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day8Second().processInput());
	}

	private String processInput() {
		int nodeValue = calculateNodeValue(rootNode);

		return nodeValue + "";
	}

	private int calculateNodeValue(Node node) {
		int nodeValue = 0;
		if (node.getChildrenSize() == 0) {
			nodeValue += node.getMetadataList().stream().mapToInt(Integer::intValue).sum();
		} else {
			for (Integer metadataValue : node.getMetadataList()) {
				if (node.getChildrenSize() < metadataValue) {
					continue;
				}
				nodeValue += calculateNodeValue(node.getChildnodesList().get(metadataValue - 1));
			}
		}
		return nodeValue;
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> inputRawList = Arrays.asList(input.split(" "));
			for (String numberString : inputRawList) {
				inputNumbers.add(Integer.parseInt(numberString));
			}

			parseNode(new MutableInteger(0));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Node parseNode(MutableInteger mutableInt) {
		Node node = new Node();
		if (mutableInt.getValue() == 0) {
			rootNode = node;
		}
		allNodesList.add(node);
		node.setChildrenSize(inputNumbers.get(mutableInt.getValueAndIncrement()));
		node.setMetadataSize(inputNumbers.get(mutableInt.getValueAndIncrement()));
		if (node.getChildrenSize() == 0) {
			for (int j = 0; j < node.getMetadataSize(); j++) {
				node.getMetadataList().add(inputNumbers.get(mutableInt.getValueAndIncrement()));
			}
		} else {
			for (int j = 0; j < node.getChildrenSize(); j++) {
				node.getChildnodesList().add(parseNode(mutableInt));
			}
			for (int j = 0; j < node.getMetadataSize(); j++) {
				node.getMetadataList().add(inputNumbers.get(mutableInt.getValueAndIncrement()));
			}
		}
		return node;
	}

	private String getInputLocation() {
		return "inputs/day_8.txt";
	}

	public class MutableInteger {
		private int value;

		public MutableInteger(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public void increment() {
			this.value++;
		}

		public int getValueAndIncrement() {
			return value++;
		}
	}

	class Node {
		private int childrenSize;
		private int metadataSize;
		private List<Node> childnodesList = new ArrayList<>();
		private List<Integer> metadataList = new ArrayList<>();

		public int getChildrenSize() {
			return childrenSize;
		}

		public void setChildrenSize(int childrenSize) {
			this.childrenSize = childrenSize;
		}

		public int getMetadataSize() {
			return metadataSize;
		}

		public void setMetadataSize(int metadataSize) {
			this.metadataSize = metadataSize;
		}

		public List<Node> getChildnodesList() {
			return childnodesList;
		}

		public void setChildnodesList(List<Node> childnodesList) {
			this.childnodesList = childnodesList;
		}

		public List<Integer> getMetadataList() {
			return metadataList;
		}

		public void setMetadataList(List<Integer> metadataList) {
			this.metadataList = metadataList;
		}

	}

}
