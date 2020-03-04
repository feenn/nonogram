package personal.benjamin.solution;

import java.util.ArrayList;
import java.util.List;

public final class Solution {

	private final List<SolutionElement> elements = new ArrayList<SolutionElement>();

	public Solution(final List<SolutionElement> elements) {
		for (SolutionElement solutionElement : elements) {
			this.getElements().add(solutionElement);
		}

		elements.sort((SolutionElement o1, SolutionElement o2) -> {
			return o1.getStart() - o2.getStart();
		});
	}

	public List<SolutionElement> getElements() {
		return elements;
	}

	public boolean isValid() {
		if (this.elements.isEmpty())
			return false;

		for (int i = 1; i < this.elements.size(); i++) {
			SolutionElement element = this.elements.get(i - 1);
			SolutionElement currElement = this.elements.get(i);

			if (element.getStart() + element.getLength() > currElement
					.getStart())
				return false;
		}
		return true;
	}

}
