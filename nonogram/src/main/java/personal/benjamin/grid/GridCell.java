package personal.benjamin.grid;

public final class GridCell {
	private final int column;
	private final int row;
	private CellType type;

	public GridCell(final GridCell cell) {
		this(cell.getColumn(), cell.getRow(), cell.getType());
	}

	public GridCell(int column, int row, CellType type) {
		this.column = column;
		this.row = row;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		if (obj instanceof GridCell) {
			GridCell cell = (GridCell) obj;
			return this.column == cell.getColumn() && this.row == cell.getRow()
					&& this.type == cell.getType();
		}
		return false;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public CellType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + column;
		result = prime * result + row;
		result = prime * result + type.hashCode();
		return result;
	}

	public void setType(CellType type) {
		this.type = type;
	}

}
