import java.util.ArrayList;

public class BoardNode {
    private int num;
    private boolean turn;
    private int size;
    private static ArrayList <int[]> children1 = new ArrayList<>();
    private static ArrayList <int[]> allChildren = new ArrayList<>();
    Board originalBoard = new Board(size, null, null, size, num, null);


    public BoardNode(int state, boolean RorC, int Size){
        num = state;
        turn = RorC;
        size = Size;
    }

    public static ArrayList<int[]> makeNode(int state,boolean RorC, int Size){
        int s=state;
        if(RorC){
            for(int q=0;q<Size;q++){
                int[] pair = new int[2];
                pair[0]=s;
                pair[1]=q;
                children1.add(pair);
                
            }
            return children1;
        }
        else{
            for(int q=0;q<Size;q++){
                int[] pair = new int[2];
                pair[0]=q;
                pair[1]=s;
                children1.add(pair);
            }
            return children1;
        }
    }
    public static ArrayList<int[]> makeAllNodes(int size){
        allChildren.clear();
        for(int q = 0; q<size;q++){
            for(int k = 0; k<size; k++){
                int[] pair = new int[2];
                pair[0]=q;
                pair[1]=k;
                allChildren.add(pair);
            }
        }
        return allChildren;
    }
    public static ArrayList<int[]> getArray(){
        return allChildren;
    }



}

