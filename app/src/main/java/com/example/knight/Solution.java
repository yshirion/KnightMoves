package com.example.knight;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.PriorityQueue;

//Solution by BFS method.
public class Solution extends AppCompatActivity{

    final short MIN = 0;
    final short MAX; // For different size of board.
    boolean[][] map; // Mapping the board to mark the visited nodes, to avoid try them again.
    Move tmp = new Move();

    //This class hold the data for each 'node'.
    public class Move implements Comparable<Move>{

        int i;
        int j;
        int num;
        Move parent;
        private Move(){}
        //For the first node.
        private Move(int i, int j){
            this.i = i;
            this.j = j;
            num = 0;
        }
        //Define the node as 'son' of the its previous node.
        private Move(int i, int j, Move parent){
            this.i = i;
            this.j = j;
            this.parent = parent;
            num = parent.num+1; //Its number on the path from the start node.
        }

        //Compare method for priorityQueue.
        @Override
        public int compareTo(Move o) {

            if ((o.i - end.i) +(o.j - end.j) > (this.i - end.i) +(this.j - end.j)) return -1;
            return 1;
        }
    }

    Move start;
    Move end;
    ArrayList<Move> explore;

    //Define start and end node, and the data structure for the calc.
    public Solution(short max, int[] start, int[] end){

        MAX = (short) (max-1);
        this.start = new Move(start[0],start[1]);
        this.end = new Move(end[0],end[1]);
        explore =  new ArrayList<>();
        explore.add(this.start);
        map = new boolean[MAX+1][MAX+1];
    }

    //The method that been invoked by the mainActivity.
    public Move play(){

        while (!explore.isEmpty()){
            tmp = explore.get(0);
            explore.remove(0);
            if(search()) break;
        }

        return tmp;
    }

    //BFS search, to find the fastest way, that for sure find the fastest, because this the attribute of BFS search.
    private boolean search(){
        if (map[tmp.i][tmp.j]) return false; //This node is already watched.
        map[tmp.i][tmp.j] = true; //mark this position in map that watched.

        //Try to improve the performance by order the nodes according to proximity to end node, by use PriorityQueue.
        PriorityQueue<Move> tmpList = new PriorityQueue<>();
        // All sons of this node.
        tmpList.add(new Move(tmp.i-2,tmp.j+1,tmp));
        tmpList.add(new Move(tmp.i-2,tmp.j-1,tmp));
        tmpList.add(new Move(tmp.i+2,tmp.j+1,tmp));
        tmpList.add(new Move(tmp.i+2,tmp.j-1,tmp));
        tmpList.add(new Move(tmp.i+1,tmp.j+2,tmp));
        tmpList.add(new Move(tmp.i+1,tmp.j-2,tmp));
        tmpList.add(new Move(tmp.i-1,tmp.j+2,tmp));
        tmpList.add(new Move(tmp.i-1,tmp.j-2,tmp));

        while (!tmpList.isEmpty()){
            Move a = tmpList.poll();
            if (a.i > MAX || a.i < MIN || a.j > MAX || a.j < MIN) continue; //out of the range - not entering to explore list.
            if (a.i == end.i && a.j == end.j) return true; //This node is the target node - check it before entering to 'explore' - BFS attribute.
            explore.add(a);
         }

        return false;
    }
}
