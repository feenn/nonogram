package personal.benjamin.solution;

import personal.benjamin.grid.CellType;

public final class SolutionElement {
	private final int start;
	private final int length;
	private final CellType type;

	public SolutionElement(int start, int length, CellType type) {
		this.start = start;
		this.length = length;
		this.type = type;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public CellType getType() {
		return type;
	}

}
