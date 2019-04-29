package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.Node;
import game.Sewers;
import game.Tile;

/** An instance is responsible for drawing the underlying maze on the screen.<br>
 * The MazePanel should contain only static images that don't need to change <br>
 * unless the screen is redrawn. */
public class MazePanel extends JPanel {
	private static final long serialVersionUID= 1L;
	private GUI gui;       // The gui on which this panel resides.

	private static final String RING_PATH= "res/ring2.png";        // Path to ring image
	private static final String PATH_PATH= "res/path.png";         // Path to image representing
														           // path
	private static final String WALL_PATH= "res/wall.png";         // Path to wall image
	private static final String COIN_PATH= "res/coins.png";        // Path to coin image
	private static final String ENTRANCE_PATH= "res/manhole.png";  // Path to entrance image
	private static final String Final_EXIT_PATH= "res/coinpersonSmall.png";
	private static final String TASTY_PATH= "res/notes.txt";
	private static final String BACKGROUND_PATH= "res/info_texture.png";

	public static int TILE_WIDTH;       // The width (in pixels) of a tile on the grid
	public static int TILE_HEIGHT;      // The height (in pixels) of a tile on the grid

	private final BufferedImage path;   // Image representing an area the diver can walk on
	private final BufferedImage wall;   // Image representing a blocked area
	private final BufferedImage ring;    // Image representing the ring
	private final BufferedImage entrance;// Image representing entrance at beginning
	protected final BufferedImage exitEnd; // Image of man emerging from hole
	private final BufferedImage tasty;
	private final Sprite coinSheet;        // Image representing a coin spritesheet
	private BufferedImage background;
	private boolean diverSpriteExitPainted= false;
	private Sewers sewers;              // Representation of the graph/level

	private boolean[][] visited;        // Contains the nodes already visited

	private static final float DARK_FACTOR= 0.3f; // How dark should dark path be? Lower values
												  // means darker
	private Color darkness;                           // Color to place over unvisited paths
	private static final int COIN_SPRITES_PER_ROW= 7;
	private static final int COIN_SPRITES_PER_COL= 2;

	/** Create a new MazePanel of a given size.<br>
	 *
	 * @param sew          The Sewers to display
	 * @param screenWidth  The width of the panel, in pixels
	 * @param screenHeight The height of the panel, in pixels
	 * @param gui          The GUI that owns this MazePanel */
	public MazePanel(Sewers sew, int screenWidth, int screenHeight, GUI gui) {
		this.gui= gui;
		sewers= sew;
		visited= new boolean[sewers.getRowCount()][sewers.getColumnCount()];

		// Compute the dimensions of an individual tile
		TILE_WIDTH= (int) (screenWidth * 1.0 / sewers.getColumnCount());
		TILE_HEIGHT= (int) (screenHeight * 0.95 / sewers.getRowCount());
		// Force tiles to be square
		TILE_WIDTH= Math.min(TILE_WIDTH, TILE_HEIGHT);
		TILE_HEIGHT= Math.min(TILE_WIDTH, TILE_HEIGHT);

		// Load content
		try {
			path= ImageIO.read(new File(PATH_PATH));
			wall= ImageIO.read(new File(WALL_PATH));
			ring= ImageIO.read(new File(RING_PATH));
			coinSheet= new Sprite(COIN_PATH, 32, 32, 1);
			entrance= ImageIO.read(new File(ENTRANCE_PATH));
			exitEnd= ImageIO.read(new File(Final_EXIT_PATH));
			tasty= ImageIO.read(new File(TASTY_PATH));
			background= ImageIO.read(new File(BACKGROUND_PATH));
		} catch (IOException e) {
			throw new IllegalArgumentException("Can't find input file : " + e.toString());
		}

		// Create the dark path
		darkness= new Color(0, 0, 0, (int) (256 - 256 * DARK_FACTOR));

		// Add listener for clicking tiles
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row= e.getY() / TILE_HEIGHT;
				int col= e.getX() / TILE_WIDTH;
				if (row < sewers.getRowCount() && col < sewers.getColumnCount()) {
					gui.selectNode(sewers.getNodeAt(row, col));
				}
			}
		});
	}

	/** Set the sewers to c */
	void setSewer(Sewers c) {
		sewers= c;
		repaint();
	}

	/** The screen size has changed. <br>
	 * Adjust the maze panel to (width, height). */
	void updateScreenSize(int width, int height) {
		TILE_WIDTH= (int) (width * 1.0 / sewers.getColumnCount());
		TILE_HEIGHT= (int) (height * 0.95 / sewers.getRowCount());
		// Force tiles to be square
		TILE_WIDTH= Math.min(TILE_WIDTH, TILE_HEIGHT);
		TILE_HEIGHT= Math.min(TILE_WIDTH, TILE_HEIGHT);
		repaint();
	}

	/** Set the sewers to be all light (light = true) or all dark. */
	public void setLighting(boolean light) {
		for (int i= 0; i < sewers.getRowCount(); i++ ) {
			for (int j= 0; j < sewers.getColumnCount(); j++ ) {
				visited[i][j]= light;
			}
		}
		repaint();
	}

	/** Update the GUI to inform it that tile (row, col) was visited. */
	public void setVisited(int row, int col) {
		visited[row][col]= true;
	}

	/** Return an image representing tile type type. */
	public BufferedImage getIcon(Tile.TileType tileType) {
		switch (tileType) {
		case FLOOR:
			return path;
		case RING:
			return ring;
		case ENTRANCE: {
			if (gui.gameState.fleeSucceeded()) return exitEnd;
			return entrance;
		}
		case WALL:
			return wall;
		default:
			return path;
		}
	}

	/** Return an icon for the coins on tile n, or null if no coins. */
	public BufferedImage getCoinsIcon(Node n) {
		double cns= n.getTile().coins();
		if (cns == Sewers.TASTY_VALUE) return tasty;
		cns*= (double) COIN_SPRITES_PER_ROW * COIN_SPRITES_PER_COL / Sewers.MAX_COIN_VALUE;
		int spriteIndex= (int) cns;
		int rowIndex= spriteIndex / COIN_SPRITES_PER_ROW;
		int colIndex= spriteIndex % COIN_SPRITES_PER_ROW;
		return coinSheet.getSprite(rowIndex, colIndex);
	}

	/** Draw the maze on the screen. */
	@Override
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		for (int i= 0; i < getWidth(); i+= 100) {
			page.drawImage(background, i, 0, 100, getHeight(), null);
		}
		page.setColor(darkness);

		// Draw the maze tiles
		for (int row= 0; row < sewers.getRowCount(); row++ ) {
			for (int col= 0; col < sewers.getColumnCount(); col++ ) {
				if (sewers.getTileAt(row, col).getType() == Tile.TileType.WALL) {
					page.drawImage(wall, TILE_WIDTH * col, TILE_HEIGHT * row,
						TILE_WIDTH, TILE_HEIGHT, null);
					continue;
				}

				// Draw the path image to the background
				page.drawImage(path, TILE_WIDTH * col, TILE_HEIGHT * row,
					TILE_WIDTH, TILE_HEIGHT, null);
				// Darken this tile if the diver has not visited it yet
				if (!visited[row][col]) {
					page.fillRect(TILE_WIDTH * col, TILE_HEIGHT * row,
						TILE_WIDTH, TILE_HEIGHT);
				}
				// If this is the ring-tile, draw the ring
				if (sewers.getTileAt(row, col).getType() == Tile.TileType.RING) {
					page.drawImage(ring, TILE_WIDTH * col, TILE_HEIGHT * row,
						TILE_WIDTH, TILE_HEIGHT, null);
				}
				// If there is a coin here, draw it
				if (sewers.getTileAt(row, col).coins() > 0) {
					page.drawImage(getCoinsIcon(sewers.getNodeAt(row, col)),
						TILE_WIDTH * col, TILE_HEIGHT * row,
						TILE_WIDTH, TILE_HEIGHT, null);
				}
				paintEntranceExit(page, row, col);
			}
		}
	}

	/** If tile (row, col) is the entrance/exit, draw the graphic for it. */
	public void paintEntranceExit(Graphics page, int row, int col) {

		if (sewers.getTileAt(row, col).getType() != Tile.TileType.ENTRANCE) return;
		if (gui.gameState.fleeSucceeded()) {
			page.drawImage(path, TILE_WIDTH * col, TILE_HEIGHT * row,
				TILE_WIDTH, TILE_HEIGHT, null);
			if (!diverSpriteExitPainted) {
				gui.diver.repaint();
				diverSpriteExitPainted= true;
			}
			page.drawImage(exitEnd, TILE_WIDTH * col, TILE_HEIGHT * row,
				TILE_WIDTH, TILE_HEIGHT, null);
			// System.out.println("paintEntranceExit. drew exitEnd");
			return;
		}
		page.drawImage(entrance, TILE_WIDTH * col, TILE_HEIGHT * row,
			TILE_WIDTH, TILE_HEIGHT, null);
	}

}
