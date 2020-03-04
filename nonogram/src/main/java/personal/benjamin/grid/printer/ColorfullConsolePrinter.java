package personal.benjamin.grid.printer;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.RED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.fusesource.jansi.AnsiConsole;

import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import personal.benjamin.grid.GridCell;

public class ColorfullConsolePrinter implements OutputPrinter {
	@Override
	public void load() {
		System.setProperty("jansi.passthrough", "true");
		AnsiConsole.systemInstall();
	}

	@Override
	public void draw(GridCell[][] cells, Collection<Integer>[] columnTargets,
			Collection<Integer>[] rowTargets, int step, String file,
			boolean isSolved) {

		/**
		 * generate the grid
		 */
		AsciiTable out = new AsciiTable();
		out.setTextAlignment(TextAlignment.CENTER);

		out.addRule();
		Collection<String> headers = new ArrayList<>();
		headers.add(" ");
		for (Collection<Integer> nums : columnTargets) {
			StringBuilder builder = new StringBuilder();
			Iterator<Integer> it = nums.iterator();
			builder.append(it.next());
			while (it.hasNext())
				builder.append("<br>").append(it.next());
			headers.add(builder.toString());
		}
		AT_Row headerRow = out.addRow(headers);
		headerRow.setTextAlignment(TextAlignment.CENTER);
		out.addRule();

		for (int i = 0; i < rowTargets.length; i++) {
			Collection<String> row = new ArrayList<String>();

			StringBuilder builder = new StringBuilder();
			Iterator<Integer> iterator = rowTargets[i].iterator();
			builder.append(iterator.next());
			while (iterator.hasNext())
				builder.append(" ").append(iterator.next());
			row.add(builder.toString());

			for (int j = 0; j < columnTargets.length; j++) {
				switch (cells[i][j].getType()) {
				case SOLID:
					row.add("XXX");
					break;
				case EMPTY:
					row.add("OOO");
					break;
				default:
					row.add(" ? ");
					break;
				}
			}

			AT_Row at_Row = out.addRow(row);
			at_Row.getCells().get(0).getContext()
					.setTextAlignment(TextAlignment.RIGHT);
			for (int j = 1; j <= columnTargets.length; j++) {
				at_Row.getCells().get(j).getContext()
						.setTextAlignment(TextAlignment.CENTER);
			}
			out.addRule();
		}

		out.getRenderer().setCWC(new CWC_LongestLine());
		String gridChars = out.render();
		gridChars = gridChars
				.replaceAll("XXX", ansi().bg(RED).a("   ").reset().toString())
				.replaceAll("OOO", " X ");

		/**
		 * generate tails
		 */
		AsciiTable tail = new AsciiTable();
		tail.addRule();
		tail.addRow("Step:" + step, "Solved:" + isSolved, "Input:" + file);
		tail.addHeavyRule();
		tail.getContext().setWidth(out.getContext().getWidth());
		tail.getContext().setGrid(A8_Grids.lineDobuleTripple());
		tail.getContext().setGridTheme(TA_GridThemes.BOTTOM);
		tail.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
		String tailChar = tail.render();

		/**
		 * print
		 */
		System.out.println(gridChars + "\n" + tailChar + "\n");
//		return gridChars + "\n" + tailChar + "\n";
	}

	@Override
	public void unLoad() {
		AnsiConsole.systemUninstall();
	}
}
