public class Table{
    private String [][] tab;
    private int rows;
    private int cols;

    /*
     * READ THIS: READ THE PREVIOUS MESSAGE ON THE DUCK CLASS MENTIONING 
       HOW THE TABLE IS BEING CREATED BASED OF THE NUMBER OF POSITIONS AND DUCKS.
     */

    public Table(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.tab = new String [rows][cols]; // (x,y)
        for(int x=0; x<rows; x++)
            for(int y=0; y<cols; y++)
                this.tab[x][y] =  "E";
    }

    public void createBoard(int row, int col, String symbol){
       for(int x=0; x<this.rows; x++){
        for(int y=0; y<this.cols; y++){
            tab[x][y] = " ";
        }
       }
       if (row >= 0 && row < rows && col >= 0 && col < cols) {
        tab[row][col] = symbol;
       }

    }

    public void displayBoard(){
        for(int x=0; x<this.rows; x++){
            for(int y=0; y<this.rows; y++){
                System.out.print(this.tab[x][y] + " ");
            }
            System.out.println(); // this print moves to the next line after each row to give it the board like feel. 
        }

    }
}