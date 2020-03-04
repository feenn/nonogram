package personal.benjamin.grid.printer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import personal.benjamin.grid.GridCell;
import personal.benjamin.nonogram.Puzzle;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class GraphicPrinter extends PApplet {

	private final int leftSpacing = 10;
	private final int topSpacing = 10;
//	private final String fontStr = "Segoe UI Bold";
	private final String fontStr = "Microsoft Yahei UI Bold";

	private final int bgColor = color(255, 255, 255);

	private final float innerLineWeight = 1.5f;
	private final float outterLineWeight = 3.0f;
	private final float textLeading = 25f;
	private final float gridsSpacing = 3.0f;
	private final float rectRadius = 5.0f;
	private final float btnSpacing = 20f;
	private final float btnSize = 40f;

	private final int headerBgColor = color(236, 236, 248);
	private final int headerFontColor = color(41, 35, 81);
	private final int outterLineColor = color(0, 0, 0);
	private final int innerLineColor = color(217, 222, 228);
	private final int tileFillColor = color(56, 72, 97);
	private final int tileStrokeColor = color(42, 53, 75);
	private final int msgColor = color(255, 0, 0);
	private final int statusColor = color(58, 84, 169);

	private String[] columnHeaders = null;
	private String[] rowHeaders = null;

	private int currIndex = -1;
	private boolean isSolved = false;
	private String file = null;

	private float rowHeaderWidth, columnHeaderHeight, tileSize;
	private PImage btnExport, btnPrev, btnNext;

	private MouseTransformed mouse = new MouseTransformed(this);
	private String msg = null;

	public GraphicPrinter() {
		this.currIndex = Puzzle.cellsList.size() - 1;
		this.isSolved = true;
		this.file = Puzzle.file.getName();

		if (this.columnHeaders == null)
			this.columnHeaders = new String[Puzzle.columnTargets.length];

		for (int i = 0; i < Puzzle.columnTargets.length; i++) {
			StringBuilder builder = new StringBuilder();
			Iterator<Integer> iterator = Puzzle.columnTargets[i].iterator();
			builder.append(iterator.next());
			while (iterator.hasNext())
				builder.append("\n").append(iterator.next());
			this.columnHeaders[i] = builder.toString();
		}

		if (this.rowHeaders == null)
			this.rowHeaders = new String[Puzzle.rowTargets.length];

		for (int i = 0; i < Puzzle.rowTargets.length; i++) {
			StringBuilder builder = new StringBuilder();
			Iterator<Integer> iterator = Puzzle.rowTargets[i].iterator();
			builder.append(iterator.next());
			while (iterator.hasNext())
				builder.append("  ").append(iterator.next());
			this.rowHeaders[i] = builder.toString();
		}
	}

	@Override
	public void draw() {
		background(bgColor);
		drawHeaders();
		drawGrids();
		drawTiles();
		drawBtns();
		drawMsg();
		drawStatus();
	}

	private void drawBtns() {
		pushMatrix();
		translate(leftSpacing, topSpacing);
		image(btnPrev, 0, 0, btnSize, btnSize);
		image(btnNext, btnSize + btnSpacing, 0, btnSize, btnSize);
		image(btnExport, 2 * (btnSize + btnSpacing), 0, btnSize, btnSize);
		popMatrix();
	}

	private void drawGrids() {
		pushMatrix();
		translate(leftSpacing + gridsSpacing + rowHeaderWidth, topSpacing
				+ columnHeaderHeight + gridsSpacing + btnSize + btnSpacing);
		color(bgColor);

		/**
		 * draw Inner line
		 */
		stroke(innerLineColor);
		strokeWeight(innerLineWeight);
		for (int i = 1; i < rowHeaders.length; i++)
			line(0, i * tileSize, tileSize * this.columnHeaders.length,
					i * tileSize);
		for (int i = 1; i < columnHeaders.length; i++)
			line(i * tileSize, 0, i * tileSize,
					tileSize * this.rowHeaders.length);

		/**
		 * draw out line
		 */
		noFill();
		stroke(outterLineColor);
		strokeWeight(outterLineWeight);
		rect(0, 0, tileSize * this.columnHeaders.length,
				tileSize * this.rowHeaders.length);
		for (int i = 5; i < this.columnHeaders.length; i += 5)
			line(i * tileSize, 0, i * tileSize,
					tileSize * this.rowHeaders.length);
		for (int i = 5; i < this.rowHeaders.length; i += 5)
			line(0, i * tileSize, tileSize * this.columnHeaders.length,
					i * tileSize);

		popMatrix();
	}

	private void drawHeaders() {

		/**
		 * draw row headers
		 */
		pushMatrix();
		translate(this.leftSpacing, this.topSpacing + this.gridsSpacing
				+ this.columnHeaderHeight + btnSize + btnSpacing);
		color(headerFontColor);
		textAlign(RIGHT, CENTER);

		for (int i = 0; i < this.rowHeaders.length; i++) {
			fill(headerBgColor);
			noStroke();
			rect(0, i * this.tileSize + this.innerLineWeight,
					this.rowHeaderWidth,
					this.tileSize - 2 * this.innerLineWeight, this.rectRadius);
			fill(headerFontColor);
			stroke(0);
			text(this.rowHeaders[i], 0, i * this.tileSize,
					this.rowHeaderWidth - 5, this.tileSize);
		}
		popMatrix();

		/**
		 * draw column headers
		 */
		pushMatrix();
		translate(rowHeaderWidth + leftSpacing + gridsSpacing,
				topSpacing + btnSize + btnSpacing);
		textLeading(textLeading);
		textAlign(CENTER, BOTTOM);
		for (int i = 0; i < this.columnHeaders.length; i++) {
			fill(headerBgColor);
			noStroke();
			rect(i * tileSize + this.innerLineWeight, 0,
					tileSize - 2 * innerLineWeight, this.columnHeaderHeight,
					rectRadius);
			stroke(0);
			fill(headerFontColor);
			text(this.columnHeaders[i], i * tileSize, 0, tileSize,
					columnHeaderHeight - 5);
		}
		popMatrix();
	}

	private void drawMsg() {
		if (msg == null)
			return;

		pushMatrix();
		translate(leftSpacing + 3 * (btnSize + btnSpacing), topSpacing);
		pushStyle();
		color(msgColor);
		fill(msgColor);
		textAlign(LEFT, CENTER);
		textSize(18);
		text(msg, 0, 0, width - 2 * leftSpacing - 3 * (btnSize + btnSpacing),
				btnSize);
		popStyle();
		popMatrix();
	}

	private void drawStatus() {
		if (currIndex < 0)
			return;

		pushMatrix();
		translate(leftSpacing,
				topSpacing + btnSize + btnSpacing + columnHeaderHeight
						+ gridsSpacing + tileSize * rowHeaders.length
						+ btnSpacing);
		pushStyle();
		color(statusColor);
		fill(statusColor);
		textAlign(CENTER, CENTER);
		textSize(20);
		text(String.format(
				"Step=%d   Solved=%b   file=%s", currIndex + 1, isSolved, file),
				0, 0, width - 2 * leftSpacing,
				height - (topSpacing + btnSize + btnSpacing + columnHeaderHeight
						+ gridsSpacing + tileSize * rowHeaders.length
						+ btnSpacing));
		popStyle();
		popMatrix();
	}

	private void drawTiles() {
		GridCell[][] cells = Puzzle.cellsList.get(currIndex);
		pushMatrix();
		translate(leftSpacing + gridsSpacing + rowHeaderWidth, topSpacing
				+ columnHeaderHeight + gridsSpacing + btnSize + btnSpacing);
		stroke(tileStrokeColor);
		strokeWeight(innerLineWeight);
		fill(tileFillColor);
		for (int i = 0; i < rowHeaders.length; i++) {
			for (int j = 0; j < columnHeaders.length; j++) {
				pushMatrix();
				translate(j * tileSize, i * tileSize);
				switch (cells[i][j].getType()) {
				case EMPTY:
					pushStyle();
					strokeWeight(outterLineWeight);
					pushMatrix();
					translate(tileSize / 2, tileSize / 2);
					rotate(QUARTER_PI);
					line(-tileSize / 3, 0, tileSize / 3, 0);
					rotate(-HALF_PI);
					line(-tileSize / 3, 0, tileSize / 3, 0);
					popMatrix();
					popStyle();
					break;
				case SOLID:
					rect(0, 0, tileSize, tileSize);
					break;
				case UNKNOWN:
				default:
					break;
				}
				popMatrix();
			}
		}
		popMatrix();
	}

	private PImage getImage(String url) throws IOException {
		BufferedImage bi = ImageIO.read(getClass().getResourceAsStream(url));
//		return new PImage(bi);

		/*
		 * just for image with transport background
		 */
		PGraphics g = createGraphics(bi.getWidth(), bi.getHeight());

		g.smooth(4);
		g.beginDraw();
		Graphics2D g2d = (Graphics2D) g.getNative();
		g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
		g.endDraw();

		return g.copy();
	}

	private int mouseBtnIndex() {
		mouse.post();
		mouse.translateMouse(leftSpacing, topSpacing);
		for (int i = 0; i < 3; i++)
			if (mouse.mouseX() >= i * (btnSize + btnSpacing)
					&& mouse.mouseX() <= i * (btnSize + btnSpacing) + btnSize
					&& mouse.mouseY() >= 0 && mouse.mouseY() <= btnSize)
				return i + 1;
		return -1;
	}

	@Override
	public void mouseClicked() {
		if (mouseButton == LEFT) {
			int index = mouseBtnIndex();
			switch (index) {
			case 1:
				if (this.currIndex > 0) {
					this.currIndex--;
					this.isSolved = false;
				}
				break;
			case 2:
				if (this.currIndex < Puzzle.cellsList.size() - 1) {
					this.currIndex++;
					this.isSolved = this.currIndex == Puzzle.cellsList.size()
							- 1 ? true : false;
				}
				break;
			case 3:
				PImage img = get(0, (int) (btnSize + topSpacing), width,
						(int) (height - btnSize - topSpacing));
				int idx = file.indexOf(".");
				String temp = file;
				if (idx > 0)
					temp = file.substring(0, idx);
				String filename = temp + "-" + (currIndex + 1) + "#"
						+ Puzzle.cellsList.size() + ".jpg";
//						+ "-"+ year()
//						+ String.format("%02d%02d", month(), day()) + millis()
//						+ ".jpg";
				img.save(filename);
				this.msg = "Saved: " + filename;
				break;
			default:

			}

			redraw();
		}
	}

	@Override
	public void settings() {
		size(600, 400);
		smooth(4);
	}

	@Override
	public void setup() {
		strokeJoin(ROUND);

		try {
			btnPrev = getImage("/icons/btn-prev.png");
			btnNext = getImage("/icons/btn-next.png");
			btnExport = getImage("/icons/btn-export.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		textFont(createFont(fontStr, 15, true));

		/**
		 * calculate models
		 */
		/**
		 * calculate header's width of rows
		 */
		float rowHeaderWidth = 0;

		for (int i = 0; i < this.rowHeaders.length; i++) {
			float f = textWidth(this.rowHeaders[i]);
			if (f > rowHeaderWidth)
				rowHeaderWidth = f;
		}
		this.rowHeaderWidth = rowHeaderWidth + 20;

		/**
		 * calculate header's height of column
		 */
		int maxCount = 0;
		float maxWidth = 0.0f;

		for (int i = 0; i < this.columnHeaders.length; i++) {
			int c = Puzzle.columnTargets[i].size();
			if (c > maxCount)
				maxCount = c;
			float t = textWidth(this.columnHeaders[i]);
			if (t > maxWidth)
				maxWidth = t;
		}
		maxWidth += 5;

		float fHeight = textAscent() + textDescent();
		this.columnHeaderHeight = maxCount * fHeight * 0.8f
				+ ((maxCount - 1) * this.textLeading);

		this.tileSize = (width - 2 * this.leftSpacing - this.rowHeaderWidth
				- this.gridsSpacing) / this.columnHeaders.length;

		int nWidth = width;
		if (tileSize < maxWidth) {
			tileSize = maxWidth;
			nWidth = (int) (leftSpacing * 4 + rowHeaderWidth + gridsSpacing
					+ tileSize * columnHeaders.length);
		}

		int nHeight = (int) (topSpacing + btnSize + btnSpacing
				+ columnHeaderHeight + gridsSpacing
				+ tileSize * rowHeaders.length + 80);
		surface.setSize(nWidth, nHeight);
		surface.setLocation((displayWidth - nWidth) / 2,
				(displayHeight - nHeight) / 2);

		noLoop();

//		this.testCase();
	}
}
