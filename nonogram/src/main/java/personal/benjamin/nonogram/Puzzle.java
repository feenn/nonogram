package personal.benjamin.nonogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import personal.benjamin.grid.Grid;
import personal.benjamin.grid.GridCell;
import personal.benjamin.grid.printer.ColorfullConsolePrinter;
import personal.benjamin.grid.printer.ConsolePrinter;
import personal.benjamin.grid.printer.GraphicPrinter;
import personal.benjamin.grid.printer.OutputPrinter;
import processing.core.PApplet;

public class Puzzle {

	public static List<Integer>[] columnTargets;
	public static List<Integer>[] rowTargets;
	public static ArrayList<GridCell[][]> cellsList = new ArrayList<GridCell[][]>();
	private static Grid grid;
	public static File file;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("[Usage] nonogram <number_file_name> [output]\n"
					+ "    output: 0 --- in plain text (default)\n"
					+ "            1 --- in plain text with ANSI color escape sequence\n"
					+ "            2 --- draw to screen");
			return;
		}

		file = new File(args[0]);
		OutputPrinter printer = null;
		if (args.length > 1) {
			int num = 0;
			try {
				num = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("[Output] Params Error: not a number");
				System.exit(-1);
			}

			switch (num) {
			case 0:
				printer = new ConsolePrinter();
				break;
			case 1:
				printer = new ColorfullConsolePrinter();
				break;
			case 2:
				printer = null;
				break;
			default:
				System.err.println("[Output=" + num
						+ "] error: not implemented, use default");
				printer = new ConsolePrinter();
				break;
			}
		} else
			printer = new ConsolePrinter();

		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(file))) {
			/**
			 * load printer
			 */
			if (printer != null)
				printer.load();

			/**
			 * read columns numbers
			 */
			String line = bufferedReader.readLine();
			int columnSize = Integer.parseInt(line.trim());

			columnTargets = new List[columnSize];
			for (int i = 0; i < columnSize; i++) {
				line = bufferedReader.readLine().trim();
				columnTargets[i] = new ArrayList<Integer>();
				String[] tmp = line.split(" ");
				for (int j = 0; j < tmp.length; j++) {
					columnTargets[i].add(Integer.parseInt(tmp[j]));
				}
			}

			/**
			 * get rows numbers
			 */
			line = bufferedReader.readLine();
			int rowSize = Integer.parseInt(line.trim());

			rowTargets = new List[rowSize];
			for (int i = 0; i < rowSize; i++) {
				line = bufferedReader.readLine().trim();
				rowTargets[i] = new ArrayList<Integer>();
				String[] tmp = line.split(" ");
				for (int j = 0; j < tmp.length; j++) {
					rowTargets[i].add(Integer.parseInt(tmp[j]));
				}
			}

			grid = new Grid(columnSize, rowSize, columnTargets, rowTargets);

			int page = 1;
			while (!grid.isSolved()) {
				if (grid.isFailed()) {
					System.out.println("no solution!!");
					break;
				}

				grid.nextFill();
				if (printer != null)
					printer.draw(grid.getCells(), columnTargets, rowTargets,
							page++, file.getName(), grid.isSolved());
				else {
					GridCell[][] orginal = grid.getCells();
					GridCell[][] cells = new GridCell[orginal.length][orginal[0].length];
					for(int i = 0; i < cells.length; i++)
						for(int j = 0; j < cells[i].length; j++)
							cells[i][j] = new GridCell(orginal[i][j]);
					cellsList.add(cells);
				}
			}
			
			if(printer == null)
				PApplet.main(GraphicPrinter.class);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/**
			 * unload printer
			 */
			if (printer != null)
				printer.unLoad();
		}
	}

}
