import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel implements MouseListener {
	// Constants for game
	// number of ROWS by COLS cell constants
	public static final int ROWS = 3;
	public static final int COLS = 3;
	public static final String TITLE = "Tic Tac Toe";

	// constants for dimensions used for drawing
	// cell width and height
	public static final int CELL_SIZE = 100;
	// drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	// Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	public static final int SYMBOL_STROKE_WIDTH = 8;

	/* declare game object variables */
	// the game board
	private Board board;

	// The current game state
	private GameState currentState;

	// the current player
	private Player currentPlayer;
	// for displaying game status message
	private JLabel statusBar;

	/** Constructor to setup the UI and game components on the panel */
	public GameMain() {

		addMouseListener(this);

		// Setup the status bar (JLabel) to display status message
		statusBar = new JLabel("         "); // create a new JLable
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));// create a font and set parameters
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5)); // create a border
		statusBar.setOpaque(true);
		statusBar.setBackground(Color.LIGHT_GRAY); // set background colour

		// layout of the panel is in border layout
		setLayout(new BorderLayout());
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

		board = new Board(); // new Board instance

		initGame(); // This method initialise the game board

	}

	public static void main(String[] args) {
		// Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				// create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
				JPanel GameMain = new JPanel(); // create a new panel

				frame.add(new GameMain()); // add the new panel to the frame
				frame.pack();
				frame.setLocationRelativeTo(null); // set the location of the frame
				frame.addMouseListener(null); // add a Mouse listener to track mouse input
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the close operation to exit
				frame.setVisible(true); // set the visibility to true

			}
		});
	}

	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
		// fill background and set colour to white
		super.paintComponent(g);
		setBackground(Color.WHITE);
		// ask the game board to paint itself
		board.paint(g);

		// set status bar message
		if (currentState == GameState.Playing) {
			statusBar.setForeground(Color.BLACK);
			if (currentPlayer == Player.Cross && currentState == GameState.Playing) {
				statusBar.setForeground(Color.BLACK);
				statusBar.setText("It is 'X's Turn"); // show status bar message "X" if "Player.Cross" has turn

			} else {
				if (currentPlayer == Player.Nought && currentState == GameState.Playing) {
					statusBar.setForeground(Color.BLUE);
					statusBar.setText("It is 'O's Turn"); // show status bar message "O" if "Player.Nought" has turn
				}

			}
		} else if (currentState == GameState.Draw) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("It's a Draw! Click to play again."); // show status bar message if is Draw
		} else if (currentState == GameState.Cross_won) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'X' Won! Click to play again."); // show status bar message if "Player.Cross" has won
		} else if (currentState == GameState.Nought_won) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'O' Won! Click to play again."); // show status bar message if "Player.Nought" has won
		}
	}

	/**
	 * Initialise the game-board contents and the current status of GameState and
	 * Player)
	 */
	public void initGame() {
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				// all cells empty
				board.cells[row][col].content = Player.Empty;
			}
		}
		currentState = GameState.Playing;
		currentPlayer = Player.Cross;
	}

	/**
	 * After each turn check to see if the current player hasWon by putting their
	 * symbol in that position, If they have the GameState is set to won for that
	 * player If no winner then isDraw is called to see if deadlock, if not
	 * GameState stays as PLAYING
	 * 
	 */
	public void updateGame(Player thePlayer, int row, int col) {
		// check for win after play
		if (board.hasWon(thePlayer, row, col)) {
			if (thePlayer == Player.Cross) {
				currentState = GameState.Cross_won; // set GameState to Cross_won
			} else if (thePlayer == Player.Nought) {
				currentState = GameState.Nought_won; // set GameState to Nought_won
			}

			// check for Draw
		} else if (board.isDraw()) {
			if (thePlayer.equals(Player.Cross)) {
				currentState = GameState.Draw; // set GameState to Draw
			}

		} else {
			currentState = GameState.Playing; // set GameState to Playing

		}
	}

	/**
	 * Event handler for the mouse click on the JPanel. If selected cell is valid
	 * and Empty then current player is added to cell content. UpdateGame is called
	 * which will call the methods to check for winner or Draw. if none then
	 * GameState remains playing. If win or Draw then call is made to method that
	 * resets the game board. Finally a call is made to refresh the canvas so that
	 * new symbol appears
	 */

	public void mouseClicked(MouseEvent e) {
		// get the coordinates of where the click event happened
		int mouseX = e.getX();
		int mouseY = e.getY();
		// Get the row and column clicked
		int rowSelected = mouseY / CELL_SIZE;
		int colSelected = mouseX / CELL_SIZE;
		if (currentState == GameState.Playing) {
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS
					&& board.cells[rowSelected][colSelected].content == Player.Empty) {
				// move
				board.cells[rowSelected][colSelected].content = currentPlayer;
				// update currentState
				updateGame(currentPlayer, rowSelected, colSelected);
				// Switch player
				if (currentPlayer == Player.Cross) {
					currentPlayer = Player.Nought;
				} else {
					currentPlayer = Player.Cross;
				}
			}
		} else {
			// game over and restart
			initGame();
		}
		// repaint game board
		board.paint(getGraphics());
		repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Auto-generated, event not used

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Auto-generated, event not used

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated,event not used

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated, event not used

	}

}
