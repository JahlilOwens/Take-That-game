  //package takethatgui;
  
  import javax.swing.*;
  import java.awt.event.*;
  import java.util.Random;
  /*
  * This class represents one square on the Take That! board.
  */
  public class Cell
  extends JButton {
    private int value; // number in the cell
    private boolean selected; // have we picked it already?
    private int row; // coordinates of the cell on the board
    private int col;
    private Board brd; // the Board object it is part of
    public Cell(int r, int c, Board b, int min, int max, Random rand) {
      // r & c - coordinates of the cell
      // b - Board it is a part of
      // min & max - range for this cell's randomly chosen value
      // rand - the random number generator we'll use to pick the value
      super(); // do Button stuff
      selected = false; // not picked yet
      brd = b;
      row = r;
      col = c;
      value=rand.nextInt(max-min+1)+min;
      this.setText(""+value); // display number in cell
      this.addActionListener(new CellListener()); // set up to be clicked
      this.setVisible(true);
    }
    public boolean isSelected() { return selected; } // accessors
    public void unselected() { selected = false;}
    public int getValue() { return value; }
    public void select() // mark as selected; change the text too
    {
      selected = true;
      setText("***");
    }

public void setValue(int val) {
  value=val;
}

    private class CellListener implements ActionListener
    {
      public void actionPerformed(ActionEvent event)
      {
        if (selected == false) {
          boolean result = brd.makeMove(row, col, value);
          if (result) {
            selected = true;
            setText("***");
          }
          brd.nextTurn();
        }
      }
      
    }
  }
  