import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper extends JFrame implements ActionListener {

    private Board gameBoard;
    private JPanel boardView, labelView;
    private static JLabel gameTimer, separator, minesLeft;
    public static boolean clicked = false, startOver = false;
    private Timer timer;

    public static int counter = 0;

    public static int gridSize = 5, totalMines = 3, numMines = totalMines, runningTimer = 0;

    // centers window when it compiles
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public void createTimer(JLabel label) {
        runningTimer++;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Time: " + runningTimer);
                runningTimer++;
            }
        });
        timer.start();
    }

    public void createContentPane() {
        // initialize board and label
        boardView = new JPanel();
        labelView = new JPanel();

        Container c = getContentPane();
        c.setBackground(Color.CYAN);

        // build gameBoard
        gameBoard = new Board(gridSize * gridSize, this);
        boardView.setLayout(new GridLayout(gridSize, gridSize, 0, 0));
        pack();
        gameBoard.fillBoardView(boardView);

        // build labels
        gameTimer = new JLabel("Time: " + 0);
        separator = new JLabel("|");
        minesLeft = new JLabel("Mines left: " + numMines);
        labelView.add(gameTimer);
        labelView.add(separator);
        labelView.add(minesLeft);

        c.add(boardView, BorderLayout.SOUTH);
        c.add(labelView, BorderLayout.NORTH);

        CreateMenuBar menuBar = new CreateMenuBar();
        this.setJMenuBar(menuBar);
        this.setSize(new Dimension(40 * (gridSize), (40 * gridSize) + 85 ));
        centerWindow(this);
        createTimer(gameTimer);
        Timer checkStartOver = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startOver) {
                    timer.stop();
                    restartGame();
                }
            }
        });
        checkStartOver.start();
        show();
    }

    public void restartGame() {
        clicked = false;
        boardView.removeAll();
        System.out.print("game restarted");
        runningTimer = 0;
        createTimer(gameTimer);
        gameBoard = new Board(gridSize * gridSize, this);
        boardView.setLayout(new GridLayout(gridSize, gridSize, 0, 0));
        gameBoard.fillBoardView(boardView);
        this.setSize(new Dimension(40 * (gridSize), (40 * gridSize) + 85 ));
        startOver = false;
    }

    public Minesweeper() {
        super("Minesweeper by CB");
        createContentPane();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // get currently clicked square
        Square currSquare = (Square)(e.getSource());
        currSquare.setMargin(new Insets(0,0,0,0));

        // when square is clicked check if it is the first click
        if (!clicked) {     // first click
            currSquare.showBlank();
            gameBoard.randomizeBoard(currSquare);
            gameBoard.lookAround(currSquare, this);
            currSquare.setFont(new Font("Arial", Font.PLAIN, 15));
            currSquare.setVerticalTextPosition(SwingConstants.CENTER);
            currSquare.setHorizontalTextPosition(SwingConstants.CENTER);
            currSquare.removeActionListener(this);
        } else {    // after first click
            if (currSquare.customName() == "mine") {        // show mine, display losing message
                currSquare.showMine();
                JOptionPane.showMessageDialog(null, "You lose. Better luck next time :(", "Loser...", JOptionPane.INFORMATION_MESSAGE);
                // change to reset game
                gameBoard.showMines(this);
                currSquare.removeActionListener(this);
                timer.stop();
            } else { // otherwise display blank square with number of mines around it
                currSquare.showBlank();
                gameBoard.lookAround(currSquare, this);
                currSquare.setFont(new Font("Arial", Font.PLAIN, 15));
                currSquare.setVerticalTextPosition(SwingConstants.CENTER);
                currSquare.setHorizontalTextPosition(SwingConstants.CENTER);
                currSquare.removeActionListener(this);
            }
        }
        clicked = true;

        int count = gameBoard.checkFlipped();
        if (count == (gridSize * gridSize) - totalMines) {
            gameBoard.showMines(this);
            JOptionPane.showMessageDialog(null, "You have won! Select New Game to play again.", "Winner!", JOptionPane.INFORMATION_MESSAGE);
            timer.stop();
        }
    }

    public static void main(String[] args) {
        Minesweeper app = new Minesweeper();
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
    }
}
