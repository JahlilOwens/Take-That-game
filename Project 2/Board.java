//package takethatgui;

/*
 * Take That!
 * by Scott Weiss
 * 
 * This class does most of the work. It displays the labels and grid buttons.
 * It changes the grid state after each move.
 * 
 * It also manages the computer players. You will be rewriting the method
 * makeComputerChoice so it intelligently picks a move based on the current game state.
 * 
 */
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board
    extends JPanel {
  private Player rowP; // the two players
  private Player colP;
  private boolean isRowsTurn; // whose turn is it
  private int currentRow, currentCol; // row and column of last move
  private JLabel[] colLabels; // the labels that indicate which column to play in
  private JLabel[] rowLabels; // the labels to indicate the row to play in
  private Cell[][] cells; // the grid of numbers
  private JLabel messageLabel; // label for turn information, winner
  private int sizeB;//size of board
  private boolean ABbool;//our equivalent to isRowsTurn for our copyBoard
  private int p1,p2,p1LS, p2LS;//to keep track of score in simulated 
  Board copyBoard;//our copied board
  public Board(int size, Player rp, Player cp, int min, int max, Random rand) {
    // Constructor
    // size is grid size (assume square)
    // rp & cp are players
    // min and max is range of possible numbers for a square
    // rand is random number generator
    rowP = rp; // remember player information
    colP = cp;
    sizeB = size;
    isRowsTurn = rand.nextInt()%2 == 0; // determine who moves first
    rowLabels = new JLabel[size]; // make arrays of labels for row/column headings
    colLabels = new JLabel[size];
    cells = new Cell[size][size]; // make 2D array of Cells
    this.setLayout(new GridLayout(size+2, size+1)); // set layout for Board
    // one extra row to accommodate winner message
    // extra row and column for labels
    this.add(new JLabel("",JLabel.CENTER)); // blank label to line things up nicely
    for (int i=0; i < size; i++) // add column headers across the top
    {
      colLabels[i] = new JLabel(""+i,JLabel.CENTER); // label with column number
      colLabels[i].setForeground(Color.RED); // make it large and red
      colLabels[i].setFont(new Font(null, Font.BOLD, 20));
      colLabels[i].setVisible(false); // no numbers shown yet
      this.add(colLabels[i]);
    }
    for (int i=0; i < size; i++)
    {
      // make label for row first
      rowLabels[i] = new JLabel(""+i, JLabel.CENTER);
      rowLabels[i].setForeground(Color.RED);
      rowLabels[i].setFont(new Font(null, Font.BOLD, 20));
      rowLabels[i].setVisible(false);
      this.add(rowLabels[i]);
      // then make cells in that row
      for (int j=0; j < size; j++)
      {
        // create a new Cell object
        Cell tempCell = new Cell(i,j,this, min, max, rand);
        cells[i][j] = tempCell;
        this.add(tempCell); // add to panel
      }
    }
    if (isRowsTurn) // is it row player's turn to start?
    {
      // pick a random row
      currentRow = rand.nextInt(size);
      // make that row's label visible
      rowLabels[currentRow].setVisible(true);
    }
    else // do similar for columns
    {
      currentCol = rand.nextInt(size);
      colLabels[currentCol].setVisible(true);
    }
    // put text in the message label
    messageLabel = new JLabel("Time to play Take That");
    this.add(messageLabel);
  }
  
  public void nextTurn()
  // This method sets up the next player's turn
  {
    if (!gameOver()) // if there are still turns to play
    {
      if (isRowsTurn) // tell appropriate player to take its turn
        rowP.takeTurn();
      else
        colP.takeTurn();
    }
  }

  public void makeComputerMove()
  // This performs the move for the computer player
  // After determining what the move should be, it executes that move on the board
  {
    if (isRowsTurn) // is it row player's turn?
    {
      // tell user the computer player is thinking
       messageLabel.setText(rowP.getName()+" thinking");
       // determine the player's move
       int colChoice = makeComputerChoice(currentRow);
       // attempt to make the move
       if (makeMove(currentRow, colChoice, 
           cells[currentRow][colChoice].getValue()))
         cells[currentRow][colChoice].select(); // if legal move, change the grid
    }
    else // similar for column player
    {
       messageLabel.setText(colP.getName()+" thinking");
       int rowChoice = makeComputerChoice(currentCol);
       if (makeMove(rowChoice, currentCol, 
           cells[rowChoice][currentCol].getValue()))
         cells[rowChoice][currentCol].select();
      
    }
    nextTurn(); // have the next player go
  }
  public Board copyBoard(){
    Random rand = new Random();//just a random rand to fulfill the board requirement
    Board board2Board = new Board(sizeB, rowP, colP, -25, 75, rand);//making a board to copy original board
    for(int i=0; i<sizeB;i++){//for loop to go through rows
      for(int j=0;j<sizeB;j++){//for loop to go through columns
        board2Board.cells[i][j].setValue(cells[i][j].getValue());//sets the value of each cell in the copyboard as the same a the original board
        if(cells[i][j].isSelected()){//if a cell is selected on original board
          board2Board.cells[i][j].select();//select the cell on the copy board
        }
      }
    }
    return board2Board;
  }

  public Board board2(Board board){
    Random rand = new Random();//random rand to fulfill board requirements
    Board board2Board = new Board(sizeB, rowP, colP, -25, 75, rand);//building a board to return
    for(int i=0; i<sizeB;i++){//forloop to through rows
      for(int j=0;j<sizeB;j++){//forloop to go through columns
        board2Board.cells[i][j].setValue(board.cells[i][j].getValue());//set the values of the board as the same as the other board
        if(board.cells[i][j].isSelected()){//if cell is selected on board
          board2Board.cells[i][j].select();//select it on new board
        }
      }
    }
    return board2Board;
  }


  public int makeComputerChoice(int RowOrCol)
  // This method determines the square the computer player will move to
  // It returns the row or column of the move as appropriate
  // You will replace this code - right now, it makes a random legal move in the 
  // appropriate row or column
  {
    p1=0;
    p2=0;
    int depth = 0;//depth counter for cutoff
    ABbool = isRowsTurn;//Are tracker for our coppy board
    copyBoard = copyBoard();//our coppy board 
    ArrayList<int[]>nodes = BoardNode.makeNode(RowOrCol, isRowsTurn, sizeB);//generates the nodes for what row or column it starts in
    ArrayList<int[]> aNodes = new ArrayList<>();//Arraylist to hold all cells that have not been selected yet on original board
    aNodes.clear();//Clears previous cells held in array
    for(int[] pair:nodes){//for loop to add unselected cells into arraylist
      boolean tf = cells[pair[0]][pair[1]].isSelected();//check to see if cell is selected on original board
      boolean tf2 = copyBoard.cells[pair[0]][pair[1]].isSelected();//check to see if cell is selected on copy board 
      if(tf==true || tf2==true){//if statement to see if cells have been selected
        continue;//if cell was selected, skip cell
      }else{
        aNodes.add(pair);//if cell not selected yet. add to arraylist
      }
    }
    float[] RVM = new float[2];//our pair to collect our return move
    RVM = maxValue(aNodes, depth-1, Float.MAX_VALUE*-1 , Float.MAX_VALUE);//pass nodes to maxValue
    int move = Math.round(RVM[1]);//get out move

      
    return move;
  }

  public float[] maxValue(ArrayList<int[]> nodes, int depth, float alpha, float beta){
    boolean terminal = isTerminal(nodes);//check to see if we are in a terminal state
    boolean cutoff = cutOff(depth);//check to see if we are at cutoff depth 
    float value = (Float.MAX_VALUE*(-1));//our value for maxValue
    float[] moveR = new float[2];//our pair to collect return value from minValue
    if(terminal || cutoff){//if statement to see if we are in a terminal state or cutoff depth
      depth=+1;//increase depth since we are going up the tree
      return eval();//return our eval values
    }
    for(int [] pairs:nodes){//for loop to check nodes
      p1LS = cells[pairs[0]][pairs[1]].getValue();//collects the p1's score in the simulation
      p1+=p1LS;
      float[] minR = minValue(results(pairs), depth-1, alpha, beta);//make results off what cell we chose and pass to minValue
      if(cells[pairs[0]][pairs[1]].isSelected()){//if cell has been selected in original board, skip over cell
        continue;
      }
      float value2 = minR[0];//get returned value from minValue
      
      int move;
      if(value2>value){//if statement to see if value2 from minValue is greater than maxValue's value
        value = value2;//if value2 is greater, change value to value2
        //move = pairs[1];
        if(isRowsTurn){//if it is rows turn
          move = pairs[1];//move has to select a column
        }
        else{
          move = pairs[0];//move has to select a row
        }
        moveR[0] = value;//put value into our return pair
        moveR[1] = move;//put move into our return pair
        alpha = Math.max(alpha,value);

      }
      if(value>=beta){
        depth+=1;//moving up the tree. so, increase depth
        return moveR;//return our value and move
      }
      //copyBoard.cells[pairs[0]][pairs[1]].unSelect();
    }
    depth+=1;//increase depth since moving up tree
    return moveR;//return our value and move 
  }

  public float[] minValue(ArrayList<int[]>nodes,int depth, float alpha, float beta){
    boolean terminal = isTerminal(nodes);//our terminal function
    boolean cutoff = cutOff(depth);//our cutoff funtion
    float value = Float.MAX_VALUE;//minValue's value
    float[] moveR = new float[2];//our return pair
    if(terminal || cutoff){//if statement to check if we are at a terminal state or at cutoff depth
      depth+=1;//moving back up tree, so depth increase 1
      return eval();//returns out eval value
    }
    for(int[] pairs:nodes){//forloop for minValue 
      p2LS = cells[pairs[0]][pairs[1]].getValue();//collects player's 2 score in simulation
      p2+=p2LS;
      float [] maxR = maxValue(results(pairs), depth-1, alpha, beta);//generates next generation and passes it to maxValue
      if(cells[pairs[0]][pairs[1]].isSelected()){//checks to see if the pair has already been selected on the original board
        continue;//if it has been selected, skip cell
      }
      float value2 = maxR[0];//collecting value2 from maxValue
      int move;
      if(value2<value){//if statement to see if value2 is less than value
        value = value2;//if value2 is less than value, value=value2
        //move = pairs[1];
        if(isRowsTurn){//check to see if rows or columns turn
          move = pairs[1];//if rows turn, move will be which column it chose
        }
        else{
          move = pairs[0];//if columns turn, move will be which row it chose
        }
        moveR[0]=value;//puting value into our return pair
        moveR[1]=move;//putting move into out return pair
        beta = Math.min(beta,value);
      }
      if(value<=alpha){
        depth+=1;//moveing up tree so increase depth by 1
        return moveR;//returns out return pair 
      }
      //copyBoard.cells[pairs[0]][pairs[1]].unSelect();
    }
    depth+=1;//moving up tree so increase depth by 1
    return moveR;
  }

  public float[] eval(){
    float[] pair = new float[2];//create a float pair
    ArrayList<int[]> allNodes = BoardNode.makeAllNodes(sizeB);//generates all nodes in the board
    ArrayList<int[]>rNodes = new ArrayList<>();//arraylist for unselected nodes
    for(int pairs[]:allNodes){//forloop to fun through all cells on board
      if(copyBoard.cells[pairs[0]][pairs[1]].isSelected()==true || cells[pairs[0]][pairs[1]].isSelected()==true){//if statment to see if cell has been seleceted on copy board or original board
        continue;//skip node if it has been selected
      }
      else{
        rNodes.add(pairs);//cells that have not been selected are added to this arraylist
      }
    }
    int pointsL=0;
    for(int[] rPair: rNodes){//forloop to run through all cells that have not been selected
      pointsL += cells[rPair[0]][rPair[1]].getValue();//adds up all possible points left on board
    }
    int possiblePoints = pointsL/2;//cuts possible points into 2 since there are two players that take turns going
    if(isRowsTurn){//if p1 is rows or colums
      p1+=rowP.getScore();//adds points to p1 if p1 is rows 
      p2+=colP.getScore();//add points to p2 if columns
    }
    else{
      p1+=colP.getScore();//add points to p1 if columns
      p2+=rowP.getScore();//adds point to p2 if rows
    }
    float odds=0; 
    odds= p1 + p2; //adds the scores to see how many points have been selected
    float p1odds = 0;
    p1odds = (float)p1/odds;//what is the ratio of points p1 holds, of all selected points so far
    int nodesLeft = rNodes.size();//how many nodes are left in game
    if(nodesLeft==0){//if 0 cells left
      if(p1<p2){//if p2 had more points than p1, return a negative chance of winning
        p1odds=p1odds*(-1);//make p1 odds negative
        pair[0]=p1odds;//put p1odds into return pair
      }else{
        pair[0]=p1odds;//if p1 has more points that p2, return positive chances
      }
    }
    else{//else there are cells left to play
      if((p1+possiblePoints)<(p2+possiblePoints)){//if statement to see if p1 has less point than p2 if they split all possible points left.
        p1odds=p1odds*(-1);//if p1 has less points than p2, return negative chance of winning
        pair[0]=p1odds;//put value into return pair
      }else{
        pair[0]=p1odds;//else return a positive chance of winning
      }
    }
    pair[1]=0;//add 0 to useless spot in return pair
    p1-=p1LS;//subtrack last score to p1
    p2-=p2LS;//subtrack last score to p2
    if(isRowsTurn){
      p1-=rowP.getScore();
      p2-=colP.getScore();
    }
    else{
      p1-=colP.getScore();
      p2-=rowP.getScore();
    
    }
    /* 
    int k = sNodes.size();
    k-=1;
    int[] rpair = sNodes.get(k);
    evalBoard.cells[rpair[0]][rpair[1]].unSelect();
    */
    return pair;//return pair
  }

  public boolean isTerminal(ArrayList<int[]> nodes){//our terminal function
    if(nodes.size()==0){//if there are no more cells to pick from in that row or column
      return true;//return true if in terminal state
    }else{
      return false;//return false if not in terminal state
    }
  }

  public boolean cutOff(int depth){//cutoff function
    if(depth<=-6){//cutoff depth is -6
      return true;//if at cutoff depth return true 
    }
    else{
      return false;//if not at cutoff depth return false
    }
  }

  public ArrayList<int[]> results (int[] state){//our results function
    ABbool=!ABbool;//switches from rows turn or columns turn
    Board childBoard = board2(copyBoard);//creates copyboard of our copyboard
    ArrayList<int[]> results=new ArrayList<>();//arraylist to hold next generating of cells
    results.clear();
    childBoard.cells[state[0]][state[1]].select();//selected the state we are att since we are choosing to expand this cell
    if(ABbool){//see if ABbool is true
      for(int q = 0; q<sizeB;q++){//forloop to create next generation
        int [] pair = new int[2];//creates pair to be put into arraylist
        pair[0]=state[0];//if rows turn, rows is gonna be constant
        pair[1] = q;//all columns possible in row
        boolean idk = childBoard.cells[pair[0]][pair[1]].isSelected();//checks to see if that cell has been selected on the copyboard
        boolean idk2 = cells[pair[0]][pair[1]].isSelected();//see if that cell has been selected on original board
        if(idk==true|| pair==state){//check to see if it's true or not 
          continue;
        }else if(idk2==true){//check to see if true
          continue;
        }
        else{//if not true, add pair to arraylist
          results.add(pair);
        }
      }
    }else{//if columns turn
      for(int q = 0; q<sizeB;q++){//forloop to generate next   generation
        int [] pair = new int[2];//pair to add to arrayList
        pair[0]=q;//if columns turn, rows change
        pair[1] = state[1];//column is constant
        boolean idk = childBoard.cells[pair[0]][pair[1]].isSelected();//checks to see if that cell has been selected on the copyboard
        boolean idk2 = cells[pair[0]][pair[1]].isSelected();//see if that cell has been selected on original board
        if(idk==true|| pair==state){//see if it is true
          continue;//if true skip cell
        }
        else if(idk2==true){//see if true
          continue;//if true skip cell
        }
        else{//if false for both
          results.add(pair);//add pair to arraylist
        }
    }
  }
  copyBoard = board2(childBoard);//update copyboard
  return results;//return arraylist 
  }




  public boolean makeMove(int row, int col, int val)
  // Execute move made by player
  // row, col - coordinates of selected square
  // val - number in the square
  // returns true if move is legal, false otherwise
  {
    if (isRowsTurn && row==currentRow) // if row's turn and square is in current row
    {
      rowP.addToScore(val); // update player's score
      rowLabels[currentRow].setVisible(false); // hide previous visible label
      currentCol = col; // update column to that of selected square
      colLabels[currentCol].setForeground(Color.RED); // display header for selected column
      colLabels[currentCol].setVisible(true);
      isRowsTurn = false; // switch to column player's turn
      cells[row][col].select(); // select given square
      if (gameOver()) // if the game is over
      {
        add(new JLabel(rowP.getWinner(colP))); // announce the winner
      }
      return true;
    }
    else if (!isRowsTurn && col==currentCol) // do analogous things for columns
    {
      colP.addToScore(val);
      colLabels[currentCol].setVisible(false);
      currentRow = row;
      rowLabels[currentRow].setForeground(Color.RED);
      rowLabels[currentRow].setVisible(true);
      isRowsTurn = true;
      cells[row][col].select();
      if (gameOver())
        add(new JLabel(rowP.getWinner(colP)));
      return true;
    }
    else
      return false;
  }
  public boolean gameOver()
  // determine if game is over (current player does not have an available square)
  // returns true if game is over, false otherwise
  {
    if (isRowsTurn) // row turn
    {
      for (int i=0; i < cells.length; i++) // search current row
        if (cells[currentRow][i].isSelected() == false) // if some square can be picked
          return false; // game is not over
      return true; // all squares picked - game is over
    }
    else // similar for columns
    {
      for (int j=0; j < cells.length; j++)
        if (cells[j][currentCol].isSelected() == false)
          return false;
      return true;
    }
  }
  
  public void setMessage(String mesg)
  // change the message label
  // mesg - String to display
  {
    messageLabel.setText(mesg);
  }
}