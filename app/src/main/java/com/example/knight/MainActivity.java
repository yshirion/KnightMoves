package com.example.knight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final short SIZE = 8; //For change the size of board (for not 'real' chess board) in easy.
    double relative = SIZE/8;
    double x = 1/relative;
    GridLayout chess;
    Button[][] arr;
    Button refresh;
    boolean isDest = false; // Recognize which press now - first for start either the end.
    boolean isEven = true; // For paint the board - like chess board.
    TextView tv;
    String text;
    int[] start;
    int[] end;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initializeAgain();
        buildGrid();
    }
    //Initialize all screen components.
    private void initialize() {
        chess = findViewById(R.id.chessBoard);
        chess.setColumnCount(SIZE);
        chess.setRowCount(SIZE);
        tv = findViewById(R.id.instruction);
        text = "";
        tv.setText("Please press on your desire start");
        tv.setTextColor(Color.GRAY);
        arr = new Button[SIZE][SIZE];
    }
    //Define the 'again' button to reset the board for other game.
    private void initializeAgain() {
        refresh = findViewById(R.id.button);
        refresh.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });
    }

    //Create buttons and push them like a part of the chess board.
    private void buildGrid() {
        for (int i=0;i< arr.length;i++) {
            for (int j=arr[i].length-1;j>=0;j--) {
                arr[i][j] = new Button(this);
                chess.addView(arr[i][j]);
                setButton(arr[i][j]);
                arr[i][j].setText(""+(i)+","+(j));
                arr[i][j].setTextSize((int) (10*x));
                arr[i][j].setTextColor(Color.WHITE);
                arr[i][j].setTag(new int[]{i,j});// Add data in button for connection of button and its position in array.
            }
            isEven = !isEven;
        }
    }

    //For each square in chess board.
    private void setButton(Button b) {

        setParam(b);
        paintButton(b);
        b.setOnClickListener(this);
    }

    private void paintButton(Button b) {
        if (isDest)
            b.setBackgroundColor(Color.BLUE);
        else {
            //For board - use the background for change the default background of every button.
            if (isEven) b.setBackgroundResource(R.drawable.white);
            else b.setBackgroundResource(R.drawable.black);
            isEven = !isEven;
        }
    }

    //UI of chess board.
    private void setParam(Button b) {
        GridLayout.LayoutParams param = (GridLayout.LayoutParams) b.getLayoutParams();
        param.height = (int) (120*x);
        param.width = (int) (120*x);
        param.rightMargin = 3;
        param.leftMargin = 3;
        param.bottomMargin = 3;
        param.topMargin = 3;
        b.setLayoutParams(param);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (!isDest) {
            startClick(b);
        }
        else{
            endClick(b);
        }
    }

    //First click - define the start position.
    private void startClick(Button b) {
        int[] position = (int[]) b.getTag();
        isDest = true;
        start = new int[]{position[0], position[1]};
        b.setBackgroundColor(Color.BLUE);
        b.setText("start");
        b.setEnabled(false);
        setHead(true);
    }

    //Second click - define the destination, lock the board for more clicks, and start the calculation.
    private void endClick(Button b) {
        int[] position = (int[]) b.getTag();
        b.setBackgroundColor(Color.RED);
        end = new int[]{position[0], position[1]};
        b.setText("end");
        for (int i=0;i< arr.length;i++) for (int j=arr[i].length-1;j>=0;j--)
            arr[i][j].setEnabled(false);
        setHead(false);

        //Send the problem to solve by Solution object.
        Solution solution = new Solution( SIZE, start,end);
        Solution.Move move = solution.play();
        paintPath(move);
    }

    //Text box for instructions, and describe the situation.
    private void setHead(boolean isStart){
        if (isStart){
            tv.setText("Now choose your destination.");
            text += "your start is :(" + (start[0])+","+(start[1])+"),\n";
        }
        else {
            text += "and your destination is :(" + (end[0])+","+(end[1])+").";
            tv.setText(text);
        }
    }

    //Paint the path after the algorithm finished its calculate.
    private void paintPath(Solution.Move path) {
        if (path == null) return;
        while (path.parent != null){
            arr[path.i][path.j].setBackgroundColor(Color.YELLOW);
            arr[path.i][path.j].setText(""+path.num);
            arr[path.i][path.j].setTextColor(Color.BLACK);
            path = path.parent;
        }
    }
}