package ro.edy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Arnautu Adrian Mihai
 *
 */
public class Day7First {

	private String input = null;
	List<Step> stepsList = new ArrayList<>();
	List<Step> availableStepsToComplete = new ArrayList<>();

	public Day7First() {
		initializeInput();
	}

	public static void main(String[] args) {
		System.out.println("Result: " + new Day7First().processInput());
	}

	private String processInput() {
		for (Step step : stepsList) {
			if (step.getDependsOn().size() == 0) {
				availableStepsToComplete.add(step);
			}
		}

		finishStep(availableStepsToComplete.get(0));

		System.out.println("");
		return "";
	}

	private void finishStep(Step stepToFinish) {
		stepToFinish.setCompleted(true);
		System.out.print(stepToFinish.getId());
		availableStepsToComplete.remove(stepToFinish);

		for (Step step : stepsList) {
			if (!step.isCompleted() && step.getDependsOn().size() > 0 && !availableStepsToComplete.contains(step)) {
				boolean allStepsFinished = true;
				for (Step dependentStep : step.getDependsOn()) {
					if (!dependentStep.isCompleted()) {
						allStepsFinished = false;
						break;
					}
				}
				if (allStepsFinished) {
					availableStepsToComplete.add(step);
				}
			}
		}

		Collections.sort(availableStepsToComplete, new StepComparator());
		if (availableStepsToComplete.size() > 0) {
			finishStep(availableStepsToComplete.get(0));
		}
	}

	private void initializeInput() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(getInputLocation()).toURI());

			Stream<String> lines = Files.lines(path);
			input = lines.collect(Collectors.joining("\n"));
			lines.close();

			List<String> inputRawList = Arrays.asList(input.split("\n"));
			for (String row : inputRawList) {
				String stepDependsId = row.substring(row.indexOf("Step ") + "Step ".length(), row.indexOf(" must be")).trim();
				Step dependsOnStep = new Step(stepDependsId);
				int indexOfStep = stepsList.indexOf(dependsOnStep);
				if (indexOfStep != -1) {
					dependsOnStep = stepsList.get(indexOfStep);
				} else {
					stepsList.add(dependsOnStep);
					Collections.sort(stepsList, new StepComparator());
				}

				String stepId = row.substring(row.indexOf("before step ") + "before step ".length(), row.indexOf(" can begin")).trim();
				Step newStep = new Step(stepId);
				indexOfStep = stepsList.indexOf(newStep);
				if (indexOfStep != -1) {
					newStep = stepsList.get(indexOfStep);
				} else {
					stepsList.add(newStep);
					Collections.sort(stepsList, new StepComparator());
				}

				if (newStep.getDependsOn().indexOf(dependsOnStep) == -1) {
					newStep.getDependsOn().add(dependsOnStep);
					Collections.sort(newStep.getDependsOn(), new StepComparator());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getInputLocation() {
		return "inputs/day_7.txt";
	}

	class StepComparator implements Comparator<Step> {

		@Override
		public int compare(Step step1, Step step2) {
			return step1.getId().compareTo(step2.getId());
		}
	}

	class Step {
		private String id;
		private List<Step> dependsOn = new ArrayList<>();
		private boolean completed = false;

		public Step(String id) {
			super();
			this.id = id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
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
			Step other = (Step) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<Step> getDependsOn() {
			return dependsOn;
		}

		public void setDependsOn(List<Step> dependsOn) {
			this.dependsOn = dependsOn;
		}

		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}

	}

}
