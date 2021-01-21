import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Board {
    // Array to hold board squares
    private Square[] squares;

    // array to hold indices
    private int[] indices;

    // dimension variables for checking adjacent squares
    private int last;
    private int gridFactor = Minesweeper.gridSize - 1;

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of squares
        squares = new Square[size];

        // allocate array of indices
        indices = new int[size];

        // Fill the squares array
        for (int i = 0; i < size; i++) {

            // Load the front image from the resources folder
            String imgPath = "res/back.png";
            ImageIcon img = new ImageIcon(Objects.requireNonNull(loader.getResource(imgPath)));

            // Setup squares
            Square s = new Square(img);
            s.setPreferredSize(new Dimension(40, 40));
            s.setID(i);
            indices[i] = i;

            // Add them to the array
            squares[i] = s;
        }

        // add actionListener to all squares
        for (Square s: squares) {
            s.addActionListener(AL);
        }

    }

    public int checkFlipped() {
        int c = 0;
        for (Square s : squares) {
            if (s.flipped()) {
                c++;
            }
        }
        return c;
    }

    public void fillBoardView(JPanel view)
    {
        for (Square s : squares) {
            s.setMargin(new Insets(0,0,0,0));
            s.hideFront();
            s.setFlipped(false);
            view.add(s);
        }
    }

    // show all mines
    public void showMines(ActionListener AL) {
        for (Square square : squares) {
            square.setFlipped(false);
            if (square.customName() == "mine") {
                square.showMine();
                square.removeActionListener(AL);
            } else {
                square.showBlank();
                lookAround(square, AL);
            }
        }
    }

    // get random int between min and max
    public static int getRandomInteger(int min, int max) {
        int x = (int)(Math.random() * ( (max - min) + 1) ) + min;
        return x;
    }

    // randomize the board after first square is clicked, where first square is never a mine
    public void randomizeBoard(Square firstSquare) {
        int curNum;
        int length = Minesweeper.totalMines;
        int[] prevMines = new int[length];
        boolean found = false;

        // populate mine index array with -1s so first random number can be compared
        for (int k = 0; k < length; k++) {
            prevMines[k] = -1;
        }

        // populate mine index array with random numbers
        for (int i = 0; i < length; i++) {
            curNum = getRandomInteger(0, squares.length - 1);
            // if randomized number is the same as a previous mine try again
            for (int prevMine : prevMines) {
                if (prevMine == curNum || curNum == firstSquare.id()) {
                    i--;
                    found = true;
                    break;
                }
            }
            // if number is not found in prevmines, add to the array
            if (!found) {
                prevMines[i] = curNum;
            }
            found = false;
        }

        // set name of corresponding square to "mine" if it should be a mine
        for (int z = 0; z < prevMines.length; z++) {
            squares[prevMines[z]].setCustomName("mine");
        }
    }

    // examine square below currSquare
    public int south(int id) {
        if (squares[id + (gridFactor + 1)].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square above currSquare
    public int north(int id) {
        if (squares[id - (gridFactor + 1)].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to the right of currSquare
    public int east(int id) {
        if (squares[id + 1].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to left of currSquare
    public int west(int id) {
        if (squares[id - 1].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to the bottom right of currSquare
    public int southEast(int id) {
        if (squares[id + 1 + (gridFactor + 1)].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to the bottom left of currSquare
    public int southWest(int id) {
        if (squares[id + (gridFactor + 1) - 1].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to the top right of currSquare
    public int northEast(int id) {
        if (squares[id + 1 - (gridFactor + 1)].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // examine square to the top left of currSquare
    public int northWest(int id) {
        if (squares[id - 1 - (gridFactor + 1)].customName() == "mine") {
            return 1;
        }
        return 0;
    }

    // return number of mines around corner
    public int cornerSquare(int id, ActionListener AL) {
        int minesFound = 0;
        if (id == 0) {
            minesFound += east(id) + south(id) + southEast(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id + 1], AL);
                lookAround(squares[id + (gridFactor + 1)], AL);
                lookAround(squares[id + 1 + (gridFactor + 1)], AL);
            }
        } else if (id == last) {
            minesFound += west(id) + north(id) + northWest(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - 1], AL);
                lookAround(squares[id - (gridFactor + 1)], AL);
                lookAround(squares[id - 1 - (gridFactor + 1)], AL);
            }
        } else if (id == (last - gridFactor)) {
            minesFound += north(id) + east(id) + northEast(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - (gridFactor + 1)], AL);
                lookAround(squares[id + 1], AL);
                lookAround(squares[id + 1 - (gridFactor + 1)], AL);
            }
        } else if (id == gridFactor) {
            minesFound += west(id) + south(id) + southWest(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - 1], AL);
                lookAround(squares[id + (gridFactor + 1)], AL);
                lookAround(squares[id + (gridFactor + 1) - 1], AL);
            }
        }
        return minesFound;
    }

    // return numMines found around edges of board
    public int edgeSquare(int id, String type, ActionListener AL) {
        int minesFound = 0;
        if (type == "leftCol") {
            minesFound += north(id) + south(id) + east(id) + northEast(id) + southEast(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - (gridFactor + 1)], AL);
                lookAround(squares[id + (gridFactor + 1)], AL);
                lookAround(squares[id + 1], AL);
                lookAround(squares[id + 1 - (gridFactor + 1)], AL);
                lookAround(squares[id + 1 + (gridFactor + 1)], AL);
            }
        } else if (type == "rightCol") {
            minesFound += north(id) + south(id) + west(id) + northWest(id) + southWest(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - (gridFactor + 1)], AL);
                lookAround(squares[id + (gridFactor + 1)], AL);
                lookAround(squares[id - 1], AL);
                lookAround(squares[id - 1 - (gridFactor + 1)], AL);
                lookAround(squares[id + (gridFactor + 1) - 1], AL);
            }
        } else if (type == "topRow") {
            minesFound += west(id) + east(id) + south(id) + southWest(id) + southEast(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - 1], AL);
                lookAround(squares[id + 1], AL);
                lookAround(squares[id + (gridFactor + 1)], AL);
                lookAround(squares[id + (gridFactor + 1) - 1], AL);
                lookAround(squares[id + 1 + (gridFactor + 1)], AL);
            }
        } else if (type == "bottomRow") {
            minesFound += west(id) + east(id) + north(id) + northWest(id) + northEast(id);
            if (minesFound == 0) {
                squares[id].setCustomName("looked");
                lookAround(squares[id - 1], AL);
                lookAround(squares[id + 1], AL);
                lookAround(squares[id - (gridFactor + 1)], AL);
                lookAround(squares[id - 1 - (gridFactor + 1)], AL);
                lookAround(squares[id + 1 - (gridFactor + 1)], AL);
            }
        }
        return minesFound;
    }

    // return numMines found around a middle square
    public int middleSquare(int id, ActionListener AL) {
        int minesFound = 0;
        minesFound += north(id) + south(id) + west(id) + east(id) + northEast(id) + northWest(id) + southEast(id) + southWest(id);
        if (minesFound == 0) {
            squares[id].setCustomName("looked");
            lookAround(squares[id - (gridFactor + 1)], AL);
            lookAround(squares[id + (gridFactor + 1)], AL);
            lookAround(squares[id - 1], AL);
            lookAround(squares[id + 1], AL);
            lookAround(squares[id + 1 - (gridFactor + 1)], AL);
            lookAround(squares[id - 1 - (gridFactor + 1)], AL);
            lookAround(squares[id + 1 + (gridFactor + 1)], AL);
            lookAround(squares[id + (gridFactor + 1) - 1], AL);
        }
        return minesFound;
    }

    // look at squares around clicked square, return number of mines surrounding current square
    public void lookAround(Square currSquare, ActionListener AL) {
        last = squares.length - 1;

        int id = currSquare.id();
        int minesFound = 0;
        if (currSquare.customName() == "looked") {
            return;
        }
        currSquare.setCustomName("looked");
        // clicked square is corner
        if (id == 0 || id == last || id == (last - gridFactor) || id == (gridFactor) ) {
            minesFound += cornerSquare(id, AL);
        } else if (id > 0 && id < gridFactor) { // clicked square is in the top row
            minesFound += edgeSquare(id, "topRow", AL);
        } else if (id < last && id > (last - gridFactor)) { // clicked square is in the bottom row
            minesFound += edgeSquare(id, "bottomRow", AL);
        } else if (id % (gridFactor + 1) == 0) { // clicked square is in the left column
            minesFound += edgeSquare(id, "leftCol", AL);
        } else if ((id + 1) % (gridFactor + 1) == 0) { // clicked square is in the right column
            minesFound += edgeSquare(id, "rightCol", AL);
        } else {    // clicked square is in the middle of the grid
            minesFound += middleSquare(id, AL);
        }

        if (minesFound == 0) {
            Minesweeper.counter++;
            squares[id].showBlank();
            squares[id].removeActionListener(AL);
            squares[id].setFlipped(true);
        } else {
            Minesweeper.counter++;
            squares[id].showBlank();
            squares[id].removeActionListener(AL);
            squares[id].setFont(new Font("Arial", Font.PLAIN, 15));
            squares[id].setVerticalTextPosition(SwingConstants.CENTER);
            squares[id].setHorizontalTextPosition(SwingConstants.CENTER);
            squares[id].setText("" + minesFound);
            squares[id].setFlipped(true);
        }
    }
}
