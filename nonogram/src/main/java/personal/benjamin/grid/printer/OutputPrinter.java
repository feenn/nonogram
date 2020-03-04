package personal.benjamin.grid.printer;

import java.util.Collection;

import personal.benjamin.grid.GridCell;

public interface OutputPrinter {

	public default void load() {
	};

	public default void unLoad() {
	};

	public void draw(GridCell[][] cells, Collection<Integer>[] columnTargets,
			Collection<Integer>[] rowTargets, int step, String file,
			boolean isSolved);
}
