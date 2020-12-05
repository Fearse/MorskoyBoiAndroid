package ru.samsung.itschool.book.cells;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import org.w3c.dom.Text;

import task.Stub;
import task.Task;

public class CellsActivity extends AppCompatActivity {

    private final Context context = this;
    private int Popadanie=0;
    private int xkor=0;
    private int ykor=0;
    private CellsActivity userProgram;
    private boolean ProstavleniKorabli = false;
    private boolean ProstavleniKorabli2 = false;
    private int WIDTH = 12;
    private int HEIGHT = 12;
    private TextView textOrientation;
    private TextView Pole1;
    private TextView Pole2;
    private TextView Start;
    private Button Orientation;
    private int OrientationI = 1;
    private TextView textnapole;
    private int[] ShipsPlayer = new int[5];
    private int[] ShipsComputer = new int[5];
    private int[][] Razmetka = new int[12][12];
    private int[][] RazmetkaComp = new int[12][12];
    private int kolichestvoigrokov=0;
    int RegForEnemyAttack=0;
    private Button[][] cells;
    private Button[][] cells1;
    private Button restartButton;
    private  Button Multiplayer;
    private Button Singleplayer;
    private Button Quit;
    private int otschet=0;
    private int DeadK1=0;
    private int DeadK2=0;
    private GridLayout cellslayout;
    private GridLayout cellslayout1;
    private TextView textend;
    private TextView text2;
    private TextView text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cells);
        Orientation = (Button) findViewById(R.id.OrientationB);
        Orientation.setOnClickListener(onMyButtonClick);
        restartButton = (Button) findViewById(R.id.closeButton);
        Singleplayer=(Button) findViewById(R.id.Singleplayer);
        Multiplayer=(Button) findViewById(R.id.Multiplayer);
        Quit=(Button) findViewById(R.id.Quit);
        cellslayout1=(GridLayout) findViewById(R.id.CellsLayout1);
        cellslayout=(GridLayout) findViewById(R.id.CellsLayout);
        Pole1=(TextView) findViewById(R.id.Pole1);
        Pole2=(TextView) findViewById(R.id.Pole2);
        Start=(TextView) findViewById(R.id.Start);
        textend=(TextView) findViewById(R.id.textend);
        text1=(TextView) findViewById(R.id.text1);
        text2=(TextView) findViewById(R.id.text2);
        makeCells();
        makeCells1();

        generate();

    }

    OnClickListener onMyButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (OrientationI == 1) {
                OrientationI = 0;
                textOrientation.setText("Текущая ориентация:горизонтальная");
            } else {
                textOrientation.setText("Текущая ориентация:вертикальная");
                OrientationI = 1;
            }
        }
    };

    void generate() {
        int x = 1;
        for (int i = 0; i < 5; i++) {
            ShipsPlayer[i] = 0;
            ShipsComputer[i] = 0;
        }
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 12; j++)
                if (i == 0 || j == 0 || i == 11 || j == 11) {
                    Razmetka[i][j] = -1;
                    RazmetkaComp[i][j] = -1;
                } else {
                    RazmetkaComp[i][j] = 0;
                    Razmetka[i][j] = 0;
                }
        for (int i = 1; i < HEIGHT - 1; i++) {
            cells[i][0].setText(x + " ");
            cells1[i][0].setText(x + " ");
            x++;
            cells[i][0].setBackgroundColor(Color.rgb(245, 245, 220));
            cells1[i][0].setBackgroundColor(Color.rgb(245, 245, 220));
        }

        cells[0][1].setText("A");
        cells[0][2].setText("B");
        cells[0][3].setText("C");
        cells[0][4].setText("D");
        cells[0][5].setText("E");
        cells[0][6].setText("F");
        cells[0][7].setText("G");
        cells[0][8].setText("H");
        cells[0][9].setText("I");
        cells[0][10].setText("J");
        cells1[0][1].setText("A");
        cells1[0][2].setText("B");
        cells1[0][3].setText("C");
        cells1[0][4].setText("D");
        cells1[0][5].setText("E");
        cells1[0][6].setText("F");
        cells1[0][7].setText("G");
        cells1[0][8].setText("H");
        cells1[0][9].setText("I");
        cells1[0][10].setText("J");
        for (int i = 0; i < WIDTH; i++) {
            cells[0][i].setBackgroundColor(Color.rgb(245, 245, 220));
            cells[HEIGHT - 1][i].setBackgroundColor(Color.rgb(245, 245, 220));
            cells[i][WIDTH - 1].setBackgroundColor(Color.rgb(245, 245, 220));
            cells1[0][i].setBackgroundColor(Color.rgb(245, 245, 220));
            cells1[HEIGHT - 1][i].setBackgroundColor(Color.rgb(245, 245, 220));
            cells1[i][WIDTH - 1].setBackgroundColor(Color.rgb(245, 245, 220));
        }
        if (kolichestvoigrokov==1) {
            for (int i = 4; i > 0; i--)
                while (ShipsComputer[i] != 5 - i) {
                    int xk = (int) (Math.random() * 10 + 1);
                    int yk = (int) (Math.random() * 10 + 1);
                    int OrientationComp = (int) (Math.random() * 2);
                    CreateShipComp(i, yk, xk, OrientationComp);
                }
            ProstavleniKorabli2=true;
        }
    }

    /*
     * NOT FOR THE BEGINNERS
     * ==================================================
     */
    //-1-системная кнопка
    //0-промах
    //1-корабль
    //2-поле вокруг корабля
    //3-Обработанное поле(некативная клетка)
    //4-отстрелянный корабль

    void EnemyAttack (){
        if( RegForEnemyAttack==1){
            while(RegForEnemyAttack==1) {
                if (Popadanie==0) {
                    int xk = (int) (Math.random() * 10 + 1);
                    int yk = (int) (Math.random() * 10 + 1);
                    while (!((xk<11&&yk<11&&xk>0&&yk>0)&&Razmetka[xk][yk]!=-1&&Razmetka[xk][yk]!=3&&Razmetka[xk][yk]!=4))
                    {
                        xk = (int) (Math.random() * 10 + 1);
                        yk = (int) (Math.random() * 10 + 1);
                    }
                    RegistraciaPopadania(xk, yk, cells, Razmetka);
                }
                else {
                    if (Razmetka[xkor-1][ykor]!=-1 && Razmetka[xkor-1][ykor]!=3&&Razmetka[xkor-1][ykor]!=4){
                        RegistraciaPopadania(xkor-1,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor-1][ykor]!=-1&&Razmetka[xkor-1][ykor]==4&&Razmetka[xkor-2][ykor]!=-1 &&Razmetka[xkor-2][ykor]!=3&&Razmetka[xkor-2][ykor]!=4){
                        RegistraciaPopadania(xkor-2,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor-1][ykor]!=-1&&Razmetka[xkor-2][ykor]!=-1&&Razmetka[xkor-3][ykor]!=-1&&Razmetka[xkor-1][ykor]==4&&Razmetka[xkor-2][ykor]== 4&& Razmetka[xkor-3][ykor]!=-1 &&Razmetka[xkor-3][ykor]!=3&&Razmetka[xkor-3][ykor]!=4){
                        RegistraciaPopadania(xkor-3,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor+1][ykor]!=-1 &&Razmetka[xkor+1][ykor]!=3&&Razmetka[xkor+1][ykor]!=4){
                        RegistraciaPopadania(xkor+1,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor+1][ykor]!=-1&&Razmetka[xkor+2][ykor]!=-1&&Razmetka[xkor+1][ykor]==4&&Razmetka[xkor+2][ykor]!=-1 &&Razmetka[xkor+2][ykor]!=-3&&Razmetka[xkor+2][ykor]!=4){
                        RegistraciaPopadania(xkor+2,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor+1][ykor]!=-1&&Razmetka[xkor+2][ykor]!=-1&&Razmetka[xkor+3][ykor]!=-1&&Razmetka[xkor+1][ykor]==4&&Razmetka[xkor+2][ykor]== 4&& Razmetka[xkor+3][ykor]!=-1 &&Razmetka[xkor+3][ykor]!=3&&Razmetka[xkor+3][ykor]!=4){
                        RegistraciaPopadania(xkor+3,ykor,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor+1]!=-1 &&Razmetka[xkor][ykor+1]!=3&&Razmetka[xkor][ykor+1]!=4){
                        RegistraciaPopadania(xkor,ykor+1,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor+1]!=-1&&Razmetka[xkor][ykor+2]!=-1&&Razmetka[xkor][ykor+1]==4&&Razmetka[xkor][ykor+2]!=-1 &&Razmetka[xkor][ykor+2]!=3&&Razmetka[xkor][ykor+2]!=4){
                        RegistraciaPopadania(xkor,ykor+2,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor+1]!=-1&&Razmetka[xkor][ykor+2]!=-1&&Razmetka[xkor][ykor+3]!=-1&&Razmetka[xkor][ykor+1]==4&&Razmetka[xkor][ykor+2]== 4&& Razmetka[xkor][ykor+3]!=-1 &&Razmetka[xkor][ykor+3]!=3&&Razmetka[xkor][ykor+3]!=4){
                        RegistraciaPopadania(xkor,ykor+3,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor-1]!=-1 &&Razmetka[xkor][ykor-1]!=3&&Razmetka[xkor][ykor-1]!=4){
                        RegistraciaPopadania(xkor,ykor-1,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor-1]!=-1&&Razmetka[xkor][ykor-2]!=-1&&Razmetka[xkor][ykor-1]==4&&Razmetka[xkor][ykor-2]!=-1 &&Razmetka[xkor][ykor-2]!=3&&Razmetka[xkor][ykor-2]!=4){
                        RegistraciaPopadania(xkor,ykor-2,cells,Razmetka);}
                    else if(Razmetka[xkor][ykor-1]!=-1&&Razmetka[xkor][ykor-2]!=-1&&Razmetka[xkor][ykor-3]!=-1&&Razmetka[xkor][ykor-1]==4&&Razmetka[xkor][ykor-2]== 4&& Razmetka[xkor][ykor-3]!=-1 &&Razmetka[xkor][ykor-3]!=3&&Razmetka[xkor][ykor-3]!=4){
                        RegistraciaPopadania(xkor,ykor-3,cells,Razmetka);}


                }
            }
            //  Stub.show(context, "Саня, ложись, по нам ебашат");

        }
    }
    public void restartUserProgram(View v) {
        RegForEnemyAttack=0;
        Singleplayer.setVisibility(View.VISIBLE);
        Multiplayer.setVisibility(View.VISIBLE);
        Quit.setVisibility(View.VISIBLE);
        Start.setVisibility(View.VISIBLE);
        Pole1.setVisibility(View.GONE);
        textnapole.setVisibility(View.GONE);
        Orientation.setVisibility(View.GONE);
        textOrientation.setVisibility(View.GONE);
        cellslayout.setVisibility(View.GONE);
        cellslayout1.setVisibility(View.GONE);
        Pole2.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);
        textend.setVisibility(View.GONE);
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);
        Popadanie=0;
        ykor=0;
        xkor=0;
        otschet=0;
        DeadK1=0;
        DeadK2=0;
        ProstavleniKorabli = false;
        ProstavleniKorabli2 = false;
        int x = 1;
        for (int i = 0; i < 5; i++) {
            ShipsPlayer[i] = 0;
            ShipsComputer[i] = 0;
        }
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 12; j++)
                if (i == 0 || j == 0 || i == 11 || j == 11) {
                    Razmetka[i][j] = -1;
                    RazmetkaComp[i][j] = -1;
                } else {
                    RazmetkaComp[i][j] = 0;
                    Razmetka[i][j] = 0;
                    cells[i][j].setBackgroundColor(Color.WHITE);
                    cells1[i][j].setBackgroundColor(Color.WHITE);
                }
        OrientationI=1;
        textOrientation.setText("Текущая ориентация - вертикальная");
    }

    public void Singleplayer(View v){
        Singleplayer.setVisibility(View.GONE);
        Multiplayer.setVisibility(View.GONE);
        Quit.setVisibility(View.GONE);
        Start.setVisibility(View.GONE);
        Pole1.setVisibility(View.VISIBLE);
        textnapole.setVisibility(View.VISIBLE);
        Orientation.setVisibility(View.VISIBLE);
        textOrientation.setVisibility(View.VISIBLE);
        cellslayout.setVisibility(View.VISIBLE);
        cellslayout1.setVisibility(View.VISIBLE);
        Pole2.setVisibility(View.VISIBLE);
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        kolichestvoigrokov=1;
        for (int i = 4; i > 0; i--)
            while (ShipsComputer[i] != 5 - i) {
                int xk = (int) (Math.random() * 10 + 1);
                int yk = (int) (Math.random() * 10 + 1);
                int OrientationComp = (int) (Math.random() * 2);
                CreateShipComp(i, yk, xk, OrientationComp);
            }
        ProstavleniKorabli2=true;
    }
    public void Multiplayer(View v) {
        Singleplayer.setVisibility(View.GONE);
        Multiplayer.setVisibility(View.GONE);
        Quit.setVisibility(View.GONE);
        Start.setVisibility(View.GONE);
        Pole1.setVisibility(View.VISIBLE);
        textnapole.setVisibility(View.VISIBLE);
        Orientation.setVisibility(View.VISIBLE);
        textOrientation.setVisibility(View.VISIBLE);
        cellslayout.setVisibility(View.VISIBLE);
        cellslayout1.setVisibility(View.VISIBLE);
        Pole2.setVisibility(View.VISIBLE);
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        kolichestvoigrokov = 2;
    }

    public void Quit(View v){
        finish();
    }
    void RegistraciaPopadania(int x, int y, Button[][] pole, int[][] Razmetka) {
        if (Razmetka[x][y] != -1 && Razmetka[x][y] != 3 && Razmetka[x][y] != 4)
            if (Razmetka[x][y] == 1) {
                if(kolichestvoigrokov==2)
                    Stub.show(context, "Попал");
                Razmetka[x][y] = 4;
                if (RegForEnemyAttack==1&&Popadanie==0){
                    xkor=x;
                    ykor=y;
                    Popadanie=1;
                }
                pole[x][y].setBackgroundColor(Color.RED);
             /*   if(Razmetka[x+1][y]==0)
                    Razmetka[x+1][y]=2;
                if(Razmetka[x+1][y+1]==0)
                    Razmetka[x+1][y+1]=2;
                if(Razmetka[x+1][y-1]==0)
                    Razmetka[x+1][y-1]=2;
                if(Razmetka[x][y-1]==0)
                    Razmetka[x][y-1]=2;
                if (Razmetka[x][y+1]==0)
                    Razmetka[x][y+1]=2;
                if (Razmetka[x-1][y+1]==0)
                    Razmetka[x-1][y+1]=2;
                if(Razmetka[x-1][y]==0)
                    Razmetka[x-1][y]=2;
                if(Razmetka[x-1][y-1]==0)
                    Razmetka[x-1][y-1]=2;*/
                if (Razmetka[x + 1][y] == 1 || Razmetka[x][y + 1] == 1 || Razmetka[x - 1][y] == 1 || Razmetka[x][y - 1] == 1 || Razmetka[x + 1][y + 1] == 1 || Razmetka[x - 1][y - 1] == 1 ||
                        Razmetka[x + 1][y] == 4 && x + 2 < 11 && Razmetka[x + 2][y] == 1 || Razmetka[x - 1][y] == 4 && x - 2 > 0 && Razmetka[x - 2][y] == 1 ||
                        Razmetka[x][y + 1] == 4 && y + 2 < 11 && Razmetka[x][y + 2] == 1 || Razmetka[x][y - 1] == 4 && y - 2 > 0 && Razmetka[x][y - 2] == 1 ||
                        Razmetka[x + 1][y] == 4 && x + 3 < 11 && Razmetka[x + 2][y] == 4 && Razmetka[x + 3][y] == 1 || Razmetka[x - 1][y] == 4 && x - 3 > 0 && Razmetka[x - 2][y] == 4 && Razmetka[x - 3][y] == 1 ||
                        Razmetka[x][y + 1] == 4 && y + 3 < 11 && Razmetka[x][y + 2] == 4 && Razmetka[x][y + 3] == 1 || Razmetka[x][y - 1] == 4 && y - 3 > 0 && Razmetka[x][y - 2] == 4 && Razmetka[x][y - 3] == 1)
                {

                }
                else
                { //Случаи для 4 горизонт
                    if(y-3>0 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4&&Razmetka[x][y-2]==4&&Razmetka[x][y-3]==4){
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if (Razmetka[x-1][y-4]==0)  //7
                            Razmetka[x-1][y-4]=2;
                        if(Razmetka[x][y-4]==0) //8
                            Razmetka[x][y-4]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-4]==0)   //9
                            Razmetka[x+1][y-4]=2;
                        if(Razmetka[x+1][y-3]==0)   //10
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //11
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //12
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //13
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //14
                            Razmetka[x+1][y+1]=2;
                    }
                    else if (y-2 >0 && y+1<11 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4&&Razmetka[x][y-2]==4&&Razmetka[x][y+1]==4){
                        y=y+1;
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if (Razmetka[x-1][y-4]==0)  //7
                            Razmetka[x-1][y-4]=2;
                        if(Razmetka[x][y-4]==0) //8
                            Razmetka[x][y-4]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-4]==0)   //9
                            Razmetka[x+1][y-4]=2;
                        if(Razmetka[x+1][y-3]==0)   //10
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //11
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //12
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //13
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //14
                            Razmetka[x+1][y+1]=2;
                    }
                    else if(y-1>0 && y+2<11 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4&&Razmetka[x][y+2]==4&&Razmetka[x][y+1]==4){
                        y=y+2;
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if (Razmetka[x-1][y-4]==0)  //7
                            Razmetka[x-1][y-4]=2;
                        if(Razmetka[x][y-4]==0) //8
                            Razmetka[x][y-4]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-4]==0)   //9
                            Razmetka[x+1][y-4]=2;
                        if(Razmetka[x+1][y-3]==0)   //10
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //11
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //12
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //13
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //14
                            Razmetka[x+1][y+1]=2;
                    }   else if(y+3<11 && Razmetka[x][y]==4&& Razmetka[x][y+3]==4&&Razmetka[x][y+2]==4&&Razmetka[x][y+1]==4) {
                        y = y + 3;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        //Верхний ряд
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 1][y] == 0) //3
                            Razmetka[x - 1][y] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //4
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x - 1][y - 2] == 0)   //5
                            Razmetka[x - 1][y - 2] = 2;
                        if (Razmetka[x - 1][y - 3] == 0)    //6
                            Razmetka[x - 1][y - 3] = 2;
                        if (Razmetka[x - 1][y - 4] == 0)  //7
                            Razmetka[x - 1][y - 4] = 2;
                        if (Razmetka[x][y - 4] == 0) //8
                            Razmetka[x][y - 4] = 2;
                        //Нижний ряд
                        if (Razmetka[x + 1][y - 4] == 0)   //9
                            Razmetka[x + 1][y - 4] = 2;
                        if (Razmetka[x + 1][y - 3] == 0)   //10
                            Razmetka[x + 1][y - 3] = 2;
                        if (Razmetka[x + 1][y - 2] == 0)   //11
                            Razmetka[x + 1][y - 2] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //12
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //13
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //14
                            Razmetka[x + 1][y + 1] = 2;
                    }//случаи для 4 вертик
                    else if(x-3>0&& Razmetka[x][y]==4&& Razmetka[x-1][y]==4&&Razmetka[x-2][y]==4&&Razmetka[x-3][y]==4) {
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 4][y + 1] == 0)   //5
                            Razmetka[x - 4][y + 1] = 2;
                        if (Razmetka[x - 4][y] == 0)    //6
                            Razmetka[x - 4][y] = 2;
                        if (Razmetka[x - 4][y - 1] == 0)  //7
                            Razmetka[x - 4][y - 1] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //8
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //9
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //10
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //11
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //12
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //13
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //14
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(x-2>0 && x+1<11 && Razmetka[x][y]==4&& Razmetka[x-1][y]==4&&Razmetka[x-2][y]==4&&Razmetka[x+1][y]==4) {
                        x=x+1;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 4][y + 1] == 0)   //5
                            Razmetka[x - 4][y + 1] = 2;
                        if (Razmetka[x - 4][y] == 0)    //6
                            Razmetka[x - 4][y] = 2;
                        if (Razmetka[x - 4][y - 1] == 0)  //7
                            Razmetka[x - 4][y - 1] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //8
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //9
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //10
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //11
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //12
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //13
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //14
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(x-1>0 && x+2<11 && Razmetka[x][y]==4&& Razmetka[x-1][y]==4&&Razmetka[x+2][y]==4&&Razmetka[x+1][y]==4) {
                        x=x+2;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 4][y + 1] == 0)   //5
                            Razmetka[x - 4][y + 1] = 2;
                        if (Razmetka[x - 4][y] == 0)    //6
                            Razmetka[x - 4][y] = 2;
                        if (Razmetka[x - 4][y - 1] == 0)  //7
                            Razmetka[x - 4][y - 1] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //8
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //9
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //10
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //11
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //12
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //13
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //14
                            Razmetka[x + 1][y + 1] = 2;
                    } else if( x+3<11 && Razmetka[x][y]==4&& Razmetka[x+3][y]==4&&Razmetka[x+2][y]==4&&Razmetka[x+1][y]==4) {
                        x=x+3;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 4][y + 1] == 0)   //5
                            Razmetka[x - 4][y + 1] = 2;
                        if (Razmetka[x - 4][y] == 0)    //6
                            Razmetka[x - 4][y] = 2;
                        if (Razmetka[x - 4][y - 1] == 0)  //7
                            Razmetka[x - 4][y - 1] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //8
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //9
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //10
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //11
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //12
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //13
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //14
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    //для 3 горизонт
                    else if(y-2>0 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4&&Razmetka[x][y-2]==4){
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if(Razmetka[x][y-3]==0) //7
                            Razmetka[x][y-3]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-3]==0)   //8
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //9
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //10
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //11
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //12
                            Razmetka[x+1][y+1]=2;
                    }  else if(y-1>0 && y+1<11 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4&&Razmetka[x][y+1]==4){
                        y=y+1;
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if(Razmetka[x][y-3]==0) //7
                            Razmetka[x][y-3]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-3]==0)   //8
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //9
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //10
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //11
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //12
                            Razmetka[x+1][y+1]=2;
                    }
                    else if(y+2<11 && Razmetka[x][y]==4&& Razmetka[x][y+2]==4&&Razmetka[x][y+1]==4){
                        y=y+2;
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if (Razmetka[x-1][y-3]==0)    //6
                            Razmetka[x-1][y-3]=2;
                        if(Razmetka[x][y-3]==0) //7
                            Razmetka[x][y-3]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-3]==0)   //8
                            Razmetka[x+1][y-3]=2;
                        if(Razmetka[x+1][y-2]==0)   //9
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //10
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //11
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //12
                            Razmetka[x+1][y+1]=2;
                    }
                    //случаи 3 верт
                    else if(x-2>0 && Razmetka[x][y]==4&& Razmetka[x-1][y]==4&&Razmetka[x-2][y]==4) {
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 3][y] == 0)    //5
                            Razmetka[x - 3][y] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //6
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //7
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //8
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //9
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //10
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //11
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //12
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(x+1<11 && x-1>0 && Razmetka[x][y]==4&& Razmetka[x+1][y]==4&&Razmetka[x-1][y]==4) {
                        x=x+1;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 3][y] == 0)    //5
                            Razmetka[x - 3][y] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //6
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //7
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //8
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //9
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //10
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //11
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //12
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(x+2<11 && Razmetka[x][y]==4&& Razmetka[x+2][y]==4&&Razmetka[x+1][y]==4) {
                        x=x+2;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 3][y + 1] == 0)   //4
                            Razmetka[x - 3][y + 1] = 2;
                        if (Razmetka[x - 3][y] == 0)    //5
                            Razmetka[x - 3][y] = 2;
                        if (Razmetka[x - 3][y - 1] == 0) //6
                            Razmetka[x - 3][y - 1] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //7
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //8
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //9
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //10
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //11
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //12
                            Razmetka[x + 1][y + 1] = 2;
                    }//случаи для 2 гор
                    else if(y-1>0 && Razmetka[x][y]==4&& Razmetka[x][y-1]==4){
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if(Razmetka[x][y-2]==0) //6
                            Razmetka[x][y-2]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-2]==0)   //7
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //8
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //9
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //10
                            Razmetka[x+1][y+1]=2;
                    }else if(y+1<11 && Razmetka[x][y]==4&& Razmetka[x][y+1]==4){
                        y=y+1;
                        if(Razmetka[x][y+1]==0) //1
                            Razmetka[x][y+1]=2;
                        //Верхний ряд
                        if(Razmetka[x-1][y+1]==0)   //2
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0) //3
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)   //4
                            Razmetka[x-1][y-1]=2;
                        if(Razmetka[x-1][y-2]==0)   //5
                            Razmetka[x-1][y-2]=2;
                        if(Razmetka[x][y-2]==0) //6
                            Razmetka[x][y-2]=2;
                        //Нижний ряд
                        if(Razmetka[x+1][y-2]==0)   //7
                            Razmetka[x+1][y-2]=2;
                        if(Razmetka[x+1][y-1]==0)   //8
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x+1][y]==0)   //9
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)   //10
                            Razmetka[x+1][y+1]=2;
                    }//случаи 2 верт
                    else if(x-1>0 && Razmetka[x][y]==4&& Razmetka[x-1][y]==4) {
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 2][y] == 0)    //4
                            Razmetka[x - 2][y] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //5
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //6
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //7
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //8
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //9
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //10
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(x+1<11 && Razmetka[x][y]==4&& Razmetka[x+1][y]==4) {
                        x=x+1;
                        if (Razmetka[x][y + 1] == 0) //1
                            Razmetka[x][y + 1] = 2;
                        if (Razmetka[x - 1][y + 1] == 0)   //2
                            Razmetka[x - 1][y + 1] = 2;
                        if (Razmetka[x - 2][y + 1] == 0) //3
                            Razmetka[x - 2][y + 1] = 2;
                        if (Razmetka[x - 2][y] == 0)    //4
                            Razmetka[x - 2][y] = 2;
                        if (Razmetka[x - 2][y - 1] == 0)   //5
                            Razmetka[x - 2][y - 1] = 2;
                        if (Razmetka[x - 1][y - 1] == 0)   //6
                            Razmetka[x - 1][y - 1] = 2;
                        if (Razmetka[x][y - 1] == 0)   //7
                            Razmetka[x][y - 1] = 2;
                        if (Razmetka[x + 1][y - 1] == 0)   //8
                            Razmetka[x + 1][y - 1] = 2;
                        if (Razmetka[x + 1][y] == 0)   //9
                            Razmetka[x + 1][y] = 2;
                        if (Razmetka[x + 1][y + 1] == 0)   //10
                            Razmetka[x + 1][y + 1] = 2;
                    }
                    else if(Razmetka[x][y]==4){
                        if(Razmetka[x+1][y]==0)
                            Razmetka[x+1][y]=2;
                        if(Razmetka[x+1][y+1]==0)
                            Razmetka[x+1][y+1]=2;
                        if(Razmetka[x+1][y-1]==0)
                            Razmetka[x+1][y-1]=2;
                        if(Razmetka[x][y-1]==0)
                            Razmetka[x][y-1]=2;
                        if (Razmetka[x][y+1]==0)
                            Razmetka[x][y+1]=2;
                        if (Razmetka[x-1][y+1]==0)
                            Razmetka[x-1][y+1]=2;
                        if(Razmetka[x-1][y]==0)
                            Razmetka[x-1][y]=2;
                        if(Razmetka[x-1][y-1]==0)
                            Razmetka[x-1][y-1]=2;
                    }

                    for (int i = 1; i < 12; i++)
                        for (int j = 1; j < 12; j++)
                            if (Razmetka[i][j] == 2) {
                                Razmetka[i][j] = 3;
                                pole[i][j].setBackgroundColor(Color.BLUE);
                            }
                    if(kolichestvoigrokov==2)
                        Stub.show(context, "Убил");
                    if (RegForEnemyAttack==0) {
                        DeadK2++;
                        if (DeadK2==10){
                            DeadK1=-999;
                            restartButton.setVisibility(View.VISIBLE);
                            textend.setVisibility(View.VISIBLE);
                            textend.setText("Игрок 1 победил");
                            RegForEnemyAttack=-1;
                        }
                    }
                    else {
                        DeadK1++;
                        if (DeadK1==10) {
                            RegForEnemyAttack=0;
                            DeadK2 = -999;
                            restartButton.setVisibility(View.VISIBLE);
                            textend.setVisibility(View.VISIBLE);
                            textend.setText("Игрок 2 победил");
                            RegForEnemyAttack=-1;
                        }
                    }
                    if (RegForEnemyAttack==1&&Popadanie==1)
                        Popadanie=0;
                }
            } else if (Razmetka[x][y] == 0 || Razmetka[x][y] == 2) {
                Razmetka[x][y] = 3;
                pole[x][y].setBackgroundColor(Color.BLUE);
                if(kolichestvoigrokov==2)
                    Stub.show(context, "Промах");
                RegForEnemyAttack=1-RegForEnemyAttack;
            }

    }


    void CreateShipComp(int size,int x,int y,int orientation){
        int Oshibka=0;
        if (orientation==1) {
            for (int i = -1; i < size + 1; i++)

                if (i + x < 12 && y + 1 < 12 && y != 0 && x != 0 && RazmetkaComp[x + i][y] != 1 && RazmetkaComp[x + i][y - 1] != 1 && RazmetkaComp[x + i][y + 1] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i + x > 11 || y == 0 || y + 1 > 12 || x == 0 || RazmetkaComp[x + i][y] != 0)
                    Oshibka = 1;

            if (Oshibka == 1) {
            } else {
                for (int i = -1; i < size + 1; i++)
                {
                    if (i == -1 || i == size) {
                        if (RazmetkaComp[x + i][y] != -1) {
                            RazmetkaComp[x + i][y] = 0;
                        }

                    } else {
                        RazmetkaComp[x + i][y] = 1;
                        //         cells1[x + i][y].setBackgroundColor(Color.BLACK);
                    }
                    if (RazmetkaComp[x + i][y - 1] != -1) {
                        RazmetkaComp[x + i][y - 1] = 0;
                    }
                    if (RazmetkaComp[x + i][y + 1] != -1) {
                        RazmetkaComp[x + i][y + 1] = 0;
                    }
                }
                ShipsComputer[size]++;
            }
        }
        else {
            for (int i = -1; i < size + 1; i++)

                if (i+y<12&&x+1<12&&x!=0&&y!=0&&RazmetkaComp[x][y+i] != 1 && RazmetkaComp[x-1][y+i] != 1 && RazmetkaComp[x + 1][y + i] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i+y>11||y==0||x+1>12||x==0||RazmetkaComp[x][y+i] != 0)
                    Oshibka = 1;

            if (Oshibka == 1)
            {
            }
            else {
                for (int i = -1; i < size + 1; i++) {
                    if (i == -1 || i == size) {
                        if (RazmetkaComp[x][y+i] != -1) {
                            RazmetkaComp[x][y+i] = 0;
                        }

                    } else {
                        RazmetkaComp[x][y+i] = 1;
                        //        cells1[x][y+i].setBackgroundColor(Color.BLACK);
                    }
                    if (RazmetkaComp[x - 1][y + i] != -1) {
                        RazmetkaComp[x - 1][y + i] = 0;
                    }
                    if (RazmetkaComp[x + 1][y + i] != -1) {
                        RazmetkaComp[x + 1][y + i] = 0;
                    }
                }
                ShipsComputer[size]++;
            }
        }
    }

    void CreateShip2P(int size,int x,int y,int orientation){
        int Oshibka=0;
        if (orientation==1) {
            for (int i = -1; i < size + 1; i++)

                if (i+x<12&&y+1<12&&y!=0&&x!=0&&RazmetkaComp[x + i][y] != 1 && RazmetkaComp[x + i][y - 1] != 1 && RazmetkaComp[x + i][y + 1] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i+x>11||y==0||y+1>12||x==0||RazmetkaComp[x + i][y] != 0)
                    Oshibka = 1;

            if (Oshibka == 1)
                textnapole.setText("Ошибка,сюда нельзя поставить корабль");
            else {
                for (int i = -1; i < size + 1; i++) {
                    if (i == -1 || i == size) {
                        if (RazmetkaComp[x + i][y] != -1) {
                            RazmetkaComp[x + i][y] = 0;
                        }

                    } else {
                        RazmetkaComp[x + i][y] = 1;
                        cells1[x + i][y].setBackgroundColor(Color.BLACK);
                    }
                    if (RazmetkaComp[x + i][y - 1] != -1) {
                        RazmetkaComp[x + i][y - 1] = 0;
                    }
                    if (RazmetkaComp[x + i][y + 1] != -1) {
                        RazmetkaComp[x + i][y + 1] = 0;
                    }
                }
                ShipsComputer[size]++;
            }
        }
        else {
            for (int i = -1; i < size + 1; i++)

                if (i+y<12&&x+1<12&&x!=0&&y!=0&&RazmetkaComp[x][y+i] != 1 && RazmetkaComp[x-1][y+i] != 1 && RazmetkaComp[x + 1][y + i] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i+y>11||y==0||x+1>12||x==0||RazmetkaComp[x][y+i] != 0)
                    Oshibka = 1;

            if (Oshibka == 1)
                textnapole.setText("Ошибка,сюда нельзя поставить корабль");
            else {
                for (int i = -1; i < size + 1; i++) {
                    if (i == -1 || i == size) {
                        if (RazmetkaComp[x][y+i] != -1) {
                            RazmetkaComp[x][y+i] = 0;
                        }

                    } else {
                        RazmetkaComp[x][y+i] = 1;
                        cells1[x][y+i].setBackgroundColor(Color.BLACK);
                    }
                    if (RazmetkaComp[x - 1][y + i] != -1) {
                        RazmetkaComp[x - 1][y + i] = 0;
                    }
                    if (RazmetkaComp[x + 1][y + i] != -1) {
                        RazmetkaComp[x + 1][y + i] = 0;
                    }
                }
                ShipsComputer[size]++;
            }
        }
    }

    void CreateShip(int size,int x,int y,int orientation){
        int Oshibka=0;
        if (orientation==1) {
            for (int i = -1; i < size + 1; i++)

                if (i+x<12&&y+1<12&&y!=0&&x!=0&&Razmetka[x + i][y] != 1 && Razmetka[x + i][y - 1] != 1 && Razmetka[x + i][y + 1] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i+x>11||y==0||y+1>12||x==0||Razmetka[x + i][y] != 0)
                    Oshibka = 1;

            if (Oshibka == 1)
                textnapole.setText("Ошибка,сюда нельзя поставить корабль");
            else {
                for (int i = -1; i < size + 1; i++) {
                    if (i == -1 || i == size) {
                        if (Razmetka[x + i][y] != -1) {
                            Razmetka[x + i][y] = 0;
                        }

                    } else {
                        Razmetka[x + i][y] = 1;
                        cells[x + i][y].setBackgroundColor(Color.BLACK);
                    }
                    if (Razmetka[x + i][y - 1] != -1) {
                        Razmetka[x + i][y - 1] = 0;
                    }
                    if (Razmetka[x + i][y + 1] != -1) {
                        Razmetka[x + i][y + 1] = 0;
                    }
                }
                ShipsPlayer[size]++;
            }
        }
        else {
            for (int i = -1; i < size + 1; i++)

                if (i+y<12&&x+1<12&&x!=0&&y!=0&&Razmetka[x][y+i] != 1 && Razmetka[x-1][y+i] != 1 && Razmetka[x + 1][y + i] != 1) {

                } else Oshibka = 1;
            for (int i = 0; i < size; i++)
                if (i+y>11||y==0||x+1>12||x==0||Razmetka[x][y+i] != 0)
                    Oshibka = 1;

            if (Oshibka == 1)
                textnapole.setText("Ошибка,сюда нельзя поставить корабль");
            else {
                for (int i = -1; i < size + 1; i++) {
                    if (i == -1 || i == size) {
                        if (Razmetka[x][y+i] != -1) {
                            Razmetka[x][y+i] = 0;
                        }

                    } else {
                        Razmetka[x][y+i] = 1;
                        cells[x][y+i].setBackgroundColor(Color.BLACK);
                    }
                    if (Razmetka[x - 1][y + i] != -1) {
                        Razmetka[x - 1][y + i] = 0;
                    }
                    if (Razmetka[x + 1][y + i] != -1) {
                        Razmetka[x + 1][y + i] = 0;
                    }
                }
                ShipsPlayer[size]++;
            }
        }
    }

    int getX(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[1]);
    }

    int getY(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[0]);
    }


    void makeCells() {
        textnapole=(TextView) findViewById(R.id.textnapole);
        textOrientation=(TextView) findViewById(R.id.textOrientation);
        textOrientation.setText("Текущая ориентация:вертикальная");
        textnapole.setText("Поставьте 4-ёх палубный корабль");

        cells = new Button[HEIGHT][WIDTH];
        final GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(WIDTH);
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                cells[i][j].setOnClickListener(new OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        Button tappedCell = (Button) v;

                        //Получаем координтаты нажатой клетки
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        //ADD YOUR CODE HERE
                        //....
                        for (int i=4;i>0;i--)
                            if (ShipsPlayer[i]!=5-i) {
                                CreateShip(i, tappedY, tappedX, OrientationI);
                                break;
                            }
                        ProstavleniKorabli=true;
                        for (int i=4;i>0;i--)
                            if (ShipsPlayer[i]!=5-i) {
                                switch (i) {
                                    case 4:
                                        textnapole.setText("Поставьте " + i + "-ёх палубный корабль");
                                        break;
                                    case 3:
                                        textnapole.setText("Поставьте " + i + "-ёх палубный корабль");
                                        break;
                                    case 2:
                                        textnapole.setText("Поставьте " + i + "-ух палубный корабль");
                                        break;
                                    case 1:
                                        textnapole.setText("Поставьте " + i + " палубный корабль");
                                        break;

                                }
                                ProstavleniKorabli=false;
                                break;
                            }
                        if (ProstavleniKorabli==true)
                            for (int i=1;i<11;i++)
                                for (int j=1;j<11;j++)
                                    if (Razmetka[i][j]==1&&kolichestvoigrokov==2)
                                        cells[i][j].setBackgroundColor(Color.WHITE);
                        if (ProstavleniKorabli==true&&kolichestvoigrokov==1) {
                            textOrientation.setVisibility(View.GONE );
                            Orientation.setVisibility(View.GONE);
                            textnapole.setVisibility(View.GONE);
                            textnapole.setText("Ваш ход");
                        }
                        if (ProstavleniKorabli2==true && RegForEnemyAttack==1&&kolichestvoigrokov==2)
                            RegistraciaPopadania(tappedY, tappedX, cells, Razmetka);
                    }
                });
                cells[i][j].setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Эту строку нужно удалить
                        Stub.show(context, "Добавьте код в функцию активности onLongClick() - реакцию на долгое нажатие на клетку");
                        return false;
                    }
                });
                cells[i][j].setTag(i + "," + j);
                cellsLayout.addView(cells[i][j]);
            }
    }
    void makeCells1() {
        cells1 = new Button[HEIGHT][WIDTH];
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout1);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(WIDTH);
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cells1[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                cells1[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button tappedCell = (Button) v;

                        //Получаем координтаты нажатой клетки
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        //ADD YOUR CODE HERE
                        //....
                        textnapole=(TextView) findViewById(R.id.textnapole);
                        if (ProstavleniKorabli==true)
                        {   if (kolichestvoigrokov==2){
                            for (int i=4;i>0;i--)
                                if (ShipsComputer[i]!=5-i) {
                                    CreateShip2P(i, tappedY, tappedX, OrientationI);
                                    break;
                                }
                            ProstavleniKorabli2=true;
                            for (int i=4;i>0;i--)
                                if (ShipsComputer[i]!=5-i) {
                                    switch (i) {
                                        case 4:
                                            textnapole.setText("Поставьте " + i + "-ёх палубный корабль");
                                            break;
                                        case 3:
                                            textnapole.setText("Поставьте " + i + "-ёх палубный корабль");
                                            break;
                                        case 2:
                                            textnapole.setText("Поставьте " + i + "-ух палубный корабль");
                                            break;
                                        case 1:
                                            textnapole.setText("Поставьте " + i + " палубный корабль");
                                            break;

                                    }
                                    ProstavleniKorabli2=false;
                                    break;
                                }
                            if (ProstavleniKorabli2==true) {
                                if (ProstavleniKorabli2==true)
                                    for (int i=1;i<11;i++)
                                        for (int j=1;j<11;j++)
                                            if (RazmetkaComp[i][j]==1)
                                                cells1[i][j].setBackgroundColor(Color.WHITE);
                                otschet++;
                                textOrientation.setVisibility(View.GONE );
                                Orientation.setVisibility(View.GONE);
                                textnapole.setVisibility(View.GONE);
                                textnapole.setText("Ход первого игрока");
                            }
                        }
                        else otschet=10;
                            if (RegForEnemyAttack==0&&ProstavleniKorabli2==true&&otschet>1)
                            {
                                RegistraciaPopadania(tappedY, tappedX, cells1, RazmetkaComp);
                            }
                            if (kolichestvoigrokov!=2)  {
                                EnemyAttack();}

                        }

                    }
                });
                cells1[i][j].setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Эту строку нужно удалить
                        Stub.show(context, "Добавьте код в функцию активности onLongClick() - реакцию на долгое нажатие на клетку");
                        return false;
                    }
                });
                cells1[i][j].setTag(i + "," + j);
                cellsLayout.addView(cells1[i][j]);
            }
    }

}