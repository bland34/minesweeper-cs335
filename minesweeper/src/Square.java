import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    // Square mine icon
    private Icon mine = new ImageIcon((loader.getResource("res/newMine.png")));

    // blank square
    private Icon blank = new ImageIcon(loader.getResource("res/revealed.png"));

    // Square back image
    private Icon back = new ImageIcon(loader.getResource("res/back.png"));

    // flag
    private Icon flag = new ImageIcon(loader.getResource("res/flag.png"));
    // ID + Name
    private int id;
    private String customName;
    private boolean flipped;

    // Default constructor
    public Square() { super(); }

    // Constructor with square back initialization
    public Square(ImageIcon frontImage)
    {
        super();
        back = frontImage;
        super.setIcon(back);
        this.setSize(40, 40);
    }

    // Set the image used as the front of the square
    public void setImage(ImageIcon frontImage) { back = frontImage; }

    // Square flipping functions
    public void showMine() { super.setIcon(mine); }
    public void showBlank() { super.setIcon(blank);}
    public void hideFront() { super.setIcon(back); }
    public void showFlag() { super.setIcon(flag); }

    // Metadata: ID number
    public int id() { return id; }
    public void setID(int i) { id = i; }

    // Metadata: Custom name
    public String customName() { return customName; }
    public void setCustomName(String s) { customName = s; }

    // metadata: state
    public boolean flipped() { return flipped; }
    public void setFlipped(boolean f) { flipped = f; }
}
