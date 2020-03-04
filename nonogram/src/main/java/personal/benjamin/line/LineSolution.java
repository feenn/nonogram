package personal.benjamin.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import personal.benjamin.grid.CellType;
import personal.benjamin.grid.GridCell;
import personal.benjamin.solution.Solution;
import personal.benjamin.solution.SolutionElement;

public final class LineSolution {

	private final List<Integer> targetLens = new ArrayList<Integer>();
	private final int lineLen;
	private final int lineNum;
	private final LineType lineType;

	private List<Solution> solutions = new ArrayList<Solution>();

	public enum SolutionType {
		PROCESSING, SOLVED, NOSOLUTION
	};

	private SolutionType lineState = SolutionType.PROCESSING;

	public LineSolution(final List<Integer> targets, int lineLen, int lineNum,
			LineType lineType) {
		this.lineLen = lineLen;
		this.lineNum = lineNum;
		this.lineType = lineType;
		for (Integer integer : targets) {
			if (integer.intValue() > 0)
				this.targetLens.add(integer);
		}

		if (!targetLens.isEmpty()) {
			findInitialSolutions(0, lineLen, targets, solutions, new Stack<>());
			if (solutions.isEmpty())
				lineState = SolutionType.NOSOLUTION;
			else if (solutions.size() == 1)
				lineState = SolutionType.SOLVED;
			else
				lineState = SolutionType.PROCESSING;
		} else {
			SolutionElement element = new SolutionElement(0, lineLen,
					CellType.EMPTY);
			List<SolutionElement> elements = new ArrayList<SolutionElement>();
			elements.add(element);
			solutions.add(new Solution(elements));
			lineState = SolutionType.SOLVED;
		}
	}

	public int getLineLen() {
		return lineLen;
	}

	public int getLineNum() {
		return lineNum;
	}

	public LineType getLineType() {
		return lineType;
	}

	public void setLineFixedCells(Collection<GridCell> cells) {
		List<GridCell> filtererCells = cells.stream()
				.filter(cell -> lineType == LineType.COLUMN
						? cell.getColumn() == lineNum
						: cell.getRow() == lineNum
								&& cell.getType() != CellType.UNKNOWN)
				.collect(Collectors.toList());

		if (filtererCells.isEmpty())
			return;

		switch (lineState) {
		case SOLVED:
		case NOSOLUTION:
			return;
		default:
			List<Solution> collect = solutions.stream().filter(solution -> {
				for (GridCell cell : filtererCells)
					if (!canSatisfied(solution, cell))
						return false;

				return true;
			}).collect(Collectors.toList());

			solutions = collect;
			if (collect.isEmpty())
				lineState = SolutionType.NOSOLUTION;
			else if (collect.size() == 1)
				lineState = SolutionType.SOLVED;
		}

	}

	private boolean canSatisfied(Solution solution, GridCell cell) {
		if (cell.getType() == CellType.UNKNOWN)
			return true;

		List<SolutionElement> elements = solution.getElements();
		int lineIndex = lineType == LineType.ROW ? cell.getColumn()
				: cell.getRow();
		if (cell.getType() == CellType.SOLID) {
			for (SolutionElement element : elements) {
				if (element.getStart() <= lineIndex && element.getStart()
						+ element.getLength() - 1 >= lineIndex)
					return true;
			}
			return false;
		} else { // EMPTY
			if (lineIndex < elements.get(0).getStart() || lineIndex >= (elements
					.get(elements.size() - 1).getStart()
					+ elements.get(elements.size() - 1).getLength()))
				return true;
			for (int i = 1; i < elements.size(); i++) {
				if (lineIndex >= (elements.get(i - 1).getStart()
						+ elements.get(i - 1).getLength())
						&& lineIndex < elements.get(i).getStart())
					return true;
			}
			return false;
		}
	}

	public List<GridCell> getLineFillHint() {
		switch (lineState) {
		case NOSOLUTION:
			return null;
		case SOLVED:
//			List<SolutionElement> solutionElements = solutions.get(0)
//					.getElements();
//			List<GridCell> res = new ArrayList<GridCell>();
//
//			SolutionElement firstElement = solutionElements.get(0);
//			if (solutionElements.get(0).getStart() > 0)
//				for (int i = 0; i < firstElement.getStart() - 1; i++)
//					res.add(new GridCell(
//							lineType == LineType.COLUMN ? lineNum : i,
//							lineType == LineType.ROW ? lineNum : i,
//							CellType.EMPTY));
////				res.add(new SolutionElement(0,
////						solutionElements.get(0).getStart(), CellType.EMPTY));
////			res.add(solutionElements.get(0));
//
//			for (int i = firstElement.getStart(); i < firstElement.getStart()
//					+ firstElement.getLength(); i++)
//				res.add(new GridCell(lineType == LineType.COLUMN ? lineNum : i,
//						lineType == LineType.ROW ? lineNum : i,
//						CellType.SOLID));
//
//			if (solutionElements.size() > 1) {
//				for (int i = 1; i < solutionElements.size(); i++) {
//					for (int j = solutionElements.get(i - 1).getStart()
//							+ solutionElements.get(i - 1)
//									.getLength(); j < solutionElements.get(i)
//											.getStart(); j++)
//						res.add(new GridCell(
//								lineType == LineType.COLUMN ? lineNum : j,
//								lineType == LineType.ROW ? lineNum : j,
//								CellType.EMPTY));
////					res.add(new SolutionElement(
////							solutionElements.get(i - 1).getStart()
////									+ solutionElements.get(i - 1).getLength(),
////							solutionElements.get(i).getStart()
////									- solutionElements.get(i - 1).getStart()
////									- solutionElements.get(i - 1).getLength(),
////							CellType.EMPTY));
//					for (int j = solutionElements.get(i)
//							.getStart(); j < solutionElements.get(i).getStart()
//									+ solutionElements.get(i).getLength(); j++)
//						res.add(new GridCell(
//								lineType == LineType.COLUMN ? lineNum : j,
//								lineType == LineType.ROW ? lineNum : j,
//								CellType.SOLID));
//
////					res.add(solutionElements.get(i));
//				}
//			}
//			SolutionElement lastElement = solutionElements
//					.get(solutionElements.size() - 1);
//			int endIndex = lastElement.getStart() + lastElement.getLength();
//			if (endIndex < lineLen)
//				for (int i = endIndex; i < lineLen; i++)
//					res.add(new GridCell(
//							lineType == LineType.COLUMN ? lineNum : i,
//							lineType == LineType.ROW ? lineNum : i,
//							CellType.EMPTY));
////				res.add(new SolutionElement(endIndex, lineLen - endIndex,
////						CellType.EMPTY));
//			return res;
		default:
			List<GridCell> res = new ArrayList<GridCell>();
			int[] counts = new int[lineLen];
			
			Arrays.fill(counts, 0);
			for (Solution solution : solutions) {
				for (SolutionElement element : solution.getElements()) {
					for (int i = element.getStart(); i < element.getStart()
							+ element.getLength(); i++) {
						counts[i]++;
					}
				}
			}

			for (int i = 0; i < counts.length; i++) {
				if (counts[i] == 0)
					res.add(new GridCell(
							lineType == LineType.COLUMN ? lineNum : i,
							lineType == LineType.ROW ? lineNum : i,
							CellType.EMPTY));
				else if(counts[i] == solutions.size())
					res.add(new GridCell(
							lineType == LineType.COLUMN ? lineNum : i,
							lineType == LineType.ROW ? lineNum : i,
							CellType.SOLID));
			}
			return res;
//			int[] min = new int[targetLens.size()];
//			int[] max = new int[targetLens.size()];
//			Arrays.fill(min, lineLen);
//			Arrays.fill(max, 0);
//			for (Solution solution : solutions) {
//				List<SolutionElement> elements = solution.getElements();
//				for (int i = 0; i < targetLens.size(); i++) {
//					if (min[i] > elements.get(i).getStart())
//						min[i] = elements.get(i).getStart();
//					if (max[i] < elements.get(i).getStart())
//						max[i] = elements.get(i).getStart();
//				}
//			}
//
////			res = new ArrayList<SolutionElement>();
//			res = new ArrayList<GridCell>();
//			if (min[0] > 0)
//				for (int i = 0; i < min[0]; i++)
//					res.add(new GridCell(
//							lineType == LineType.COLUMN ? lineNum : i,
//							lineType == LineType.ROW ? lineNum : i,
//							CellType.EMPTY));
////				res.add(new SolutionElement(0, min[0], CellType.EMPTY));
//			endIndex = max[max.length - 1] + targetLens.get(min.length - 1);
//			if (endIndex < lineLen)
//				for (int i = endIndex; i < lineLen; i++)
//					res.add(new GridCell(
//							lineType == LineType.COLUMN ? lineNum : i,
//							lineType == LineType.ROW ? lineNum : i,
//							CellType.EMPTY));
////				res.add(new SolutionElement(endIndex, lineLen - endIndex,
////						CellType.EMPTY));
//			for (int i = 0; i < min.length; i++)
//				if (min[i] + targetLens.get(i) > max[i])
//					for (int j = max[i]; j < min[i] + targetLens.get(i); j++)
//						res.add(new GridCell(
//								lineType == LineType.COLUMN ? lineNum : j,
//								lineType == LineType.ROW ? lineNum : j,
//								CellType.SOLID));
////					res.add(new SolutionElement(max[i],
////							min[i] + targetLens.get(i) - max[i],
////							CellType.SOLID));
//
//			for (int i = 1; i < max.length; i++)
//				if (max[i - 1] + targetLens.get(i - 1) < min[i])
//					for (int j = max[i - 1]
//							+ targetLens.get(i - 1); j < min[i]; j++)
//						res.add(new GridCell(
//								lineType == LineType.COLUMN ? lineNum : j,
//								lineType == LineType.ROW ? lineNum : j,
//								CellType.EMPTY));
////					res.add(new SolutionElement(
////							max[i - 1] + targetLens.get(i - 1),
////							min[i] - max[i - 1] - targetLens.get(i - 1),
////							CellType.EMPTY));
//			return res;
		}
	}

	private void findInitialSolutions(int startLineGridIndex /* 0 based */,
			int endLineGridIndex /* max: linelen - 1 */,
			final List<Integer> targetLens, List<Solution> solutions,
			Stack<SolutionElement> stack) {

		int totalTargetLen = targetLens.stream()
				.reduce((sum, elem) -> sum + elem).get();
		totalTargetLen += targetLens.size() - 1;
		int totalCurrLen = endLineGridIndex - startLineGridIndex;

		if (totalTargetLen > lineLen)
			return;
		else if (totalTargetLen == totalCurrLen) {
			List<SolutionElement> res = new ArrayList<SolutionElement>(stack);
			int start = startLineGridIndex;
			for (Integer len : targetLens) {
				res.add(new SolutionElement(start, len, CellType.SOLID));
				start += len + 1;
			}
			solutions.add(new Solution(res));
		} else {
			int maxStartLineGridIndex = totalCurrLen - totalTargetLen;
			int eLen = targetLens.get(0);
			if (targetLens.size() == 1) {
				for (int i = 0; i <= maxStartLineGridIndex; i++) {
					List<SolutionElement> res = new ArrayList<SolutionElement>(
							stack);
					res.add(new SolutionElement(startLineGridIndex + i, eLen,
							CellType.SOLID));
					solutions.add(new Solution(res));
				}
			} else {
				List<Integer> remainTargetLens = targetLens.subList(1,
						targetLens.size());
				for (int i = 0; i <= maxStartLineGridIndex; i++) {
					int newStart = startLineGridIndex + i;
					stack.add(new SolutionElement(newStart, eLen,
							CellType.SOLID));
					findInitialSolutions(newStart + eLen + 1, endLineGridIndex,
							remainTargetLens, solutions, stack);
					stack.pop();
				}
			}
		}
	}

	public void printSolution(List<SolutionElement> elements) {
		char[] pchar = new char[lineLen];
		Arrays.fill(pchar, '-');
		for (SolutionElement element : elements)
			for (int i = 0; i < element.getLength(); i++) {
				switch (element.getType()) {
				case SOLID:
					pchar[element.getStart() + i] = '#';
					break;
				case EMPTY:
					pchar[element.getStart() + i] = '0';
					break;
				case UNKNOWN:
					pchar[element.getStart() + i] = '-';
					break;
				}

//			System.out.printf("s=%d,l=%d\t", element.getStart(), element.getLength());

			}

		for (char c : pchar) {
			System.out.printf("%-2s", c);
		}
		System.out.println();
	}

	public void printSolutions() {
		System.out.println(solutions.size());
		for (Solution solution : solutions) {
			printSolution(solution.getElements());
			System.out.println("-----------");
		}

	}

	public SolutionType getLineState() {
		return lineState;
	}

}
