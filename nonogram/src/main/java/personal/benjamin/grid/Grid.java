package personal.benjamin.grid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import personal.benjamin.line.LineSolution;
import personal.benjamin.line.LineSolution.SolutionType;
import personal.benjamin.line.LineType;

public final class Grid {
	private final int columnSize;
	private final int rowSize;

	private final GridCell[][] cells;

	private final LineSolution[] columns;
	private final LineSolution[] rows;

	private Set<GridCell> dirtyCells = null;

	public Grid(int columnSize, int rowSize, List<Integer>[] columnTargets,
			List<Integer>[] rowTargets) {
		this.columnSize = columnSize;
		this.rowSize = rowSize;

		cells = new GridCell[rowSize][columnSize];
		for (int i = 0; i < rowSize; i++)
			for (int j = 0; j < columnSize; j++)
				cells[i][j] = new GridCell(j, i, CellType.UNKNOWN);

		columns = new LineSolution[columnSize];
		for (int i = 0; i < columnSize; i++)
			columns[i] = new LineSolution(columnTargets[i], rowSize, i,
					LineType.COLUMN);
		rows = new LineSolution[rowSize];
		for (int i = 0; i < rowSize; i++)
			rows[i] = new LineSolution(rowTargets[i], columnSize, i,
					LineType.ROW);
	}

	public boolean isSolved() {
//		for (LineSolution solution : columns) {
//			if(solution.getLineState() != SolutionType.SOLVED)
//				return false;
//		}
//		
//		for (LineSolution solution : rows) {
//			if(solution.getLineState() != SolutionType.SOLVED)
//				return false;
//		}
		for (int i = 0; i < rowSize; i++)
			for (int j = 0; j < columnSize; j++)
				if (cells[i][j].getType() == CellType.UNKNOWN)
					return false;

		return true;
	}

	public boolean isFailed() {
		for (LineSolution solution : columns) {
			if (solution.getLineState() == SolutionType.NOSOLUTION) {
				System.out.println(
						solution.getLineType() + " " + solution.getLineNum());
				return true;
			}
		}

		for (LineSolution solution : rows) {
			if (solution.getLineState() == SolutionType.NOSOLUTION) {
				System.out.println(
						solution.getLineType() + " " + solution.getLineNum());
				return true;
			}
		}

		return false;
	}

	public void nextFill() {
		if (isSolved() || isFailed())
			return;

		if (dirtyCells == null) {// first step
			dirtyCells = new HashSet<GridCell>();
		}

		for (LineSolution solution : columns) {
			solution.setLineFixedCells(dirtyCells);
		}

		for (LineSolution solution : rows) {
			solution.setLineFixedCells(dirtyCells);
		}
		dirtyCells.clear();

		for (LineSolution solution : columns)
			dirtyCells.addAll(solution.getLineFillHint());
		for (LineSolution solution : rows)
			dirtyCells.addAll(solution.getLineFillHint());

		for (GridCell cell : dirtyCells) {
			cells[cell.getRow()][cell.getColumn()].setType(cell.getType());
		}

	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getRowSize() {
		return rowSize;
	}

	public GridCell[][] getCells() {
		return cells;
	}
}
