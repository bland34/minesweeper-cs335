import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CreateMenuBar extends JMenuBar implements ActionListener{

    private JMenu menu;
    private Minesweeper newGame;

    public CreateMenuBar() {
        // initialize menu variables
        JMenuItem menuItem;

        // arrays for menu titles/items
        String[] menuArray = {"Start", "Setup"};
        String[][] menuItemArray = {{"New Game", "Help", "Quit"}, {"Beginner 4x4 (4 mines)", "Intermediate 8x8 (15 mines)", "Expert 12x12 (40 mines)", "Custom Game"}};

        // build menuBar
        int stopper = 3;
        for (int i = 0; i < 2; i++) {
            menu = new JMenu(menuArray[i]);
            this.add(menu);
            if (i == 1) {
                stopper++;
            }
            for (int j = 0; j < stopper; j++) {
                menuItem = new JMenuItem(menuItemArray[i][j]);
                menuItem.addActionListener(this);
                menu.add(menuItem);
                if (j < 2 || (j < 3 && i == 1)) {
                    menu.addSeparator();
                }
            }
        }
    }

    public JLabel createLink() {
        // create hyperlink to link to the minesweeper help page
        String text = "Click this to go to the Minesweeper rule page!";
        JLabel link = new JLabel(text);
        link.setForeground(Color.blue.darker());
        link.setFont(new Font("Serif", Font.PLAIN, 20));
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // add mouse listener that makes the Jlabel behave like a hyperlink when mouse is moved over it and when it is clicked
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.instructables.com/id/How-to-play-minesweeper/"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setText("<html><a href=''>" + text + "</a></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                link.setText(text);
            }
        });
        return link;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        String s = source.getText();
        System.out.println(s);

        if (s == "New Game") {
            Minesweeper.startOver = true;
        }
        else if (s == "Help") {
            JLabel link = createLink();
            JOptionPane.showMessageDialog(null, link, "Help", JOptionPane.PLAIN_MESSAGE);
        }
        else if (s == "Quit") {
            System.exit(0);
        }
        else if (s == "Beginner 4x4 (4 mines)") {
            Minesweeper.totalMines = 4;
            Minesweeper.gridSize = 4;
            Minesweeper.startOver = true;
        }
        else if (s == "Intermediate 8x8 (15 mines)") {
            Minesweeper.totalMines = 15;
            Minesweeper.gridSize = 8;
            Minesweeper.startOver = true;
        }
        else if (s == "Expert 12x12 (40 mines)") {
            Minesweeper.totalMines = 40;
            Minesweeper.gridSize = 12;
            Minesweeper.startOver = true;
        }
        else if (s == "Custom Game") {
            JTextField dimensions = new JTextField(5);
            JTextField numMines = new JTextField(5);

            JPanel questions = new JPanel();
            questions.add(new JLabel("Height and Width:"));
            questions.add(dimensions);
            questions.add(Box.createHorizontalStrut(15));
            questions.add(new JLabel("Number of Mines:"));
            questions.add(numMines);


            int result = JOptionPane.showConfirmDialog(null, questions, "Custom Game", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int mines = Integer.parseInt(numMines.getText());
                    int dim = Integer.parseInt(dimensions.getText());
                    if (mines < 2 || mines > (Minesweeper.gridSize * Minesweeper.gridSize)) {
                        JOptionPane.showMessageDialog(null, "Mines must be in between 2 and half of the total grid size!");
                        return;
                    } else if (dim < 3 || dim > 12) {
                        JOptionPane.showMessageDialog(null, "Dimensions must be in between 3 and 12!");
                    }

                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Please enter a number in the box.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

            System.out.println("Height and Width: " + dimensions.getText());
            System.out.println("Number of Mines: " + numMines.getText());
            int mines = Integer.parseInt(numMines.getText());
            int dim = Integer.parseInt(dimensions.getText());
            Minesweeper.totalMines = mines;
            Minesweeper.gridSize = dim;

            Minesweeper.startOver = true;
        }
    }
}
