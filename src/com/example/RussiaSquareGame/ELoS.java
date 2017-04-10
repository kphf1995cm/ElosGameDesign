package com.example.RussiaSquareGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class ELoS extends Activity
{
    /**
     * Called when the activity is first created.
     */
    public ELoSSquare curElos=new ELoSSquare();
    public ELoSSquare curElosNext=new ELoSSquare();//当前俄罗斯方块经过移动或变化后产生的俄罗斯块
    public ELoSSquare nextElos=new ELoSSquare();
    public Square[][] squareNet;
    public int rowNum;
    public int columnNum;
    public int squareWidth;
    public int squareHeight;
    public int gameScreenWidth;
    public int gameScreenHeight;
    public int gameSquareScreenWidth;
    public int gameSquareScreenHeight;
    public boolean initialGameScreen=false;
    public int nXOffset;
    public int nYOffset;
    public Drawable[] drImgArray;
    public Drawable drImgGameBackground;
    public Drawable drImgGameSquareBkg;
    public boolean gameOverFlag=false;
    public static int gameGrade=0;
    public int gameTime=0;
    public int gameTimeMips=0;
    public static int gameStage=1;
    public Drawable drImgStateBackground;
    public Drawable drImgStateSquareBkg;
    public int squareImgChoice=0;
    public int squareTypeChoice=1;
    public int nextSquareImgChoice=0;
    public int nextSquareTypeChoice=1;
    public GameView gameView;
    public StateView stateView;
    public boolean running=false;//是否在运行
    public boolean pause=false;//是否处于暂停状态
    public int timeInterval=600;
    public GameRuningThread gameRuningThread;
    public static boolean gameOverActivityRunningFlag=false;
    //public List<Activity> endActivityList=new List<Activity>() ;
    public int GenerateRandomSquareType()
    {
     return (int)((Math.random()*19)+1);//产生1和2之间一个数
    }

    public int GenerateNextImg()
    {
        return (squareImgChoice+1)%3;//图片循环抽取
    }

    //测试俄罗斯方块是否越界以及该方块对应的数组是否已经存放数值
    public boolean TestElosSquareRangeAndAble(ELoSSquare elosSquare)
    {
        if(elosSquare==null) return true;
        for(int i=0;i<4;i++)
        {
            if(elosSquare.pointArray[i].getX()<0||elosSquare.pointArray[i].getX()>=rowNum)
                return false;//判断行是否超出数组界限
            if(elosSquare.pointArray[i].getY()<0||elosSquare.pointArray[i].getY()>=columnNum)
                return false;//判断列是否超出数组界限
            if(squareNet[elosSquare.pointArray[i].getX()][elosSquare.pointArray[i].getY()].fillFlag)
                return false;//判断对应的数组是否存放了数值
        }
        return true;
    }


    //左移、右移、下移
    public ELoSSquare MoveElosSquareLeft(ELoSSquare eLoSSquare)
    {
        ELoSSquare temp=new ELoSSquare();
        temp.setCentrePoint(eLoSSquare.centrePoint.X,eLoSSquare.centrePoint.Y-1);
        temp.type=eLoSSquare.type;
        temp.imgType=eLoSSquare.imgType;
        temp.GenernateElosSquare();
        return temp;
    }
    public ELoSSquare MoveElosSquareRight(ELoSSquare eLoSSquare)
    {
        ELoSSquare temp=new ELoSSquare();
        temp.setCentrePoint(eLoSSquare.centrePoint.X,eLoSSquare.centrePoint.Y+1);
        temp.type=eLoSSquare.type;
        temp.imgType=eLoSSquare.imgType;
        temp.GenernateElosSquare();
        return temp;
    }
    public ELoSSquare MoveElosSquareDown(ELoSSquare eLoSSquare)
    {
        ELoSSquare temp=new ELoSSquare();
        temp.setCentrePoint(eLoSSquare.centrePoint.X+1,eLoSSquare.centrePoint.Y);
        temp.type=eLoSSquare.type;
        temp.imgType=eLoSSquare.imgType;
        temp.GenernateElosSquare();
        return temp;
    }

    //移动当前活动块
    public void MoveElosSquareByKind(int kind)
    {
     switch (kind)
     {
         case 0:curElosNext=MoveElosSquareDown(curElos);break;//下移
         case 1:curElosNext=MoveElosSquareLeft(curElos);break;//左移
         case 2:curElosNext=MoveElosSquareRight(curElos);break;//右移
     }
       // Log.d("num:",""+ TestArray());
        ClearElosSquare(curElos);//先清除
       // Log.d("num:",""+ TestArray());
        if(TestElosSquareRangeAndAble(curElosNext))//测试试探移动结果有没有超出范围
        {
            curElos=curElosNext;
            TransferElosSquareToSquareNet(curElos);
            //Log.d("num:",""+ TestArray());
            //TestArray();

        }
        else
        {
            ResumeElosSquare(curElos);
            int rowLine=EliminateSquareNet(curElos); //看看有无成功消除的
            if(rowLine>0)
            {
                AddGradeByRemoveRowNum(rowLine);
                ChangeStageByGameGrade();
            }
            if(kind==0)
            {
                curElos.TransferRightElosSquareToLeftType(curElos,nextElos);
                squareTypeChoice=nextSquareTypeChoice;
                squareImgChoice=nextSquareImgChoice;
                nextSquareTypeChoice=GenerateRandomSquareType();
                nextSquareImgChoice=GenerateNextImg();
                if(TestElosSquareRangeAndAble(curElos)==false)//game over
                {
                    //ShowGameResult();
                    Intent intent=new Intent(ELoS.this,GameOverActivity.class);
                    startActivity(intent);
                }
                else
                    TransferElosSquareToSquareNet(curElos);
                //消除右边之前俄罗斯块
                stateView.ClearStateElosSquare();

                nextElos.SetType(nextSquareTypeChoice,nextSquareImgChoice);
                //根据不同的类型设置不同的起始点
                nextElos.SetRightViewCentrePointByChoice(nextSquareTypeChoice);
                nextElos.GenernateElosSquare();
                stateView.TransferElosSquareToSquareNet(nextElos);
            }
        }
        gameView.postInvalidate();
        stateView.postInvalidate();
    }

    public ELoSSquare ChangeElosSquareByKind(ELoSSquare eLoSSquare)
    {
        ELoSSquare temp=new ELoSSquare();
        temp.setCentrePoint(eLoSSquare.centrePoint.X,eLoSSquare.centrePoint.Y);
        temp.imgType=eLoSSquare.imgType;
        switch (eLoSSquare.type)
        {
            case 1:temp.type=2;break;
            case 2:temp.type=1;break;
            case 3:temp.type=3;break;
            case 4:temp.type=5;temp.centrePoint.Y=temp.centrePoint.Y-1;break;
            case 5:temp.type=6;temp.centrePoint.X=temp.centrePoint.X+1;temp.centrePoint.Y=temp.centrePoint.Y-1;break;
            case 6:temp.type=7;temp.centrePoint.Y=temp.centrePoint.Y+1;break;
            case 7:temp.type=4;temp.centrePoint.X=temp.centrePoint.X-1;temp.centrePoint.Y=temp.centrePoint.Y+1;break;
            case 8:temp.type=9;temp.centrePoint.Y=temp.centrePoint.Y-1;break;
            case 9:temp.type=10;temp.centrePoint.X=temp.centrePoint.X+1;break;
            case 10:temp.type=11;temp.centrePoint.Y=temp.centrePoint.Y+1;break;
            case 11:temp.type=8;temp.centrePoint.X=temp.centrePoint.X-1;break;
            case 12:temp.type=13;break;
            case 13:temp.type=14;break;
            case 14:temp.type=15;break;
            case 15:temp.type=12;break;
            case 16:temp.type=17;break;
            case 17:temp.type=16;break;
            case 18:temp.type=19;break;
            case 19:temp.type=18;break;
        }
        temp.GenernateElosSquare();
        return temp;
    }

    //测试有无消除
    public int EliminateSquareNet(ELoSSquare eLoSSquare)
    {
        int moveRowNum=0;
        int[] squareRow=new int[5];
        squareRow[0]=100;//安排一个很大的数，便于插入排序
        int diffRowNum=0;
        if(eLoSSquare==null) return 0;
        int i,j;
        boolean sameFlag=false;
        for(i=0;i<4;i++)
        {
            for(int k=diffRowNum;k>=0;k--) {
                if (squareRow[k] == eLoSSquare.pointArray[i].X) {
                    sameFlag = true;
                    break;
                }
            }
            if(sameFlag)
            {
                sameFlag=false;
                continue;
            }

            for(j=diffRowNum;j>=0;j--)
            {
                if(squareRow[j]>eLoSSquare.pointArray[i].X)
                {
                       squareRow[j+1]=eLoSSquare.pointArray[i].X;
                       break;
                }
                else
                {
                        squareRow[j+1]=squareRow[j];
                }
            }
            diffRowNum++;
        }
        int elosSquareTopRow=squareRow[diffRowNum]-1;//记录俄罗斯方块上一行
        for(i=1;i<=diffRowNum;i++)
        {
            for(j=0;j<columnNum;j++)
            {
                if(squareNet[squareRow[i]][j].fillFlag==false)
                    break;
            }
            if(j==columnNum)
            {
                moveRowNum++;
                MoveSquareNetDown(squareRow[i]-1,squareRow[diffRowNum],1,false);
                for(int k=i+1;k<=diffRowNum;k++)
                    squareRow[k]=squareRow[k]+1;
            }
        }
        if(moveRowNum>0)
        {
            MoveSquareNetDown( elosSquareTopRow,0,moveRowNum,true);
        }
        return moveRowNum;
    }

    //根据一次性消除的行数获得游戏分数 也可增加一些特效，例如声音
    public void AddGradeByRemoveRowNum(int removeRowNum)
    {
        switch(removeRowNum)
        {
            case 1:gameGrade=gameGrade+10;break;
            case 2:gameGrade=gameGrade+30;break;
            case 3:gameGrade=gameGrade+60;break;
            case 4:gameGrade=gameGrade+100;break;
            default:break;
        }
    }
    //修改游戏关卡并且提升游戏难度
    public void ChangeStageByGameGrade()
    {
        int preGameStage=gameStage;
        gameStage=gameGrade/100+1;
        if(gameStage!=preGameStage)
        {
            //Toast.makeText(this,"stage:"+gameStage,Toast.LENGTH_SHORT).show();
            timeInterval=timeInterval-50;//停顿时间缩短50毫秒
        }
    }
    public void ShowGameResult()
    {
        Toast.makeText(this,"Game Over "+"Grade:"+gameGrade+" Stage:"+gameStage,Toast.LENGTH_SHORT).show();
        //running=false;
    }
    public void ShowStartGame()
    {
        Toast.makeText(this,"Grade:"+gameGrade+" Stage:"+gameStage,Toast.LENGTH_SHORT).show();
        //running=false;
    }
    //整体向下移动
    //param@removeFlag 为true表示要将最上面几行清零
    public void MoveSquareNetDown(int startRow,int endRow,int moveRowNum,boolean removeFlag)
    {
        //if(startRow<endRow) return;
        if(endRow<0||startRow<0||endRow>=rowNum||startRow+moveRowNum>=rowNum) return;
        for(int i=startRow;i>=endRow;i--)
        {
            for(int j=0;j<columnNum;j++)
            {
                squareNet[i+moveRowNum][j].fillFlag=squareNet[i][j].fillFlag;
                squareNet[i+moveRowNum][j].drImg=squareNet[i][j].drImg;
            }
        }
        if(removeFlag==true)
        {
            for(int i=endRow;i<endRow+moveRowNum;i++)
            {
                for(int j=0;j<columnNum;j++)
                    squareNet[i][j].fillFlag=false;
            }
        }
    }
    //清除俄罗斯方块
    public void ClearElosSquare(ELoSSquare eLoSSquare)
    {
        if(eLoSSquare==null)
            return;
        for(int i=0;i<4;i++)
        {
            squareNet[eLoSSquare.pointArray[i].X][eLoSSquare.pointArray[i].Y].fillFlag=false;
            squareNet[eLoSSquare.pointArray[i].X][eLoSSquare.pointArray[i].Y].drImg=null;
        }
    }
    //恢复俄罗斯方块
    public void ResumeElosSquare(ELoSSquare eLoSSquare)
    {
        if(eLoSSquare==null) return;
        for(int i=0;i<4;i++)
        {
            squareNet[eLoSSquare.pointArray[i].X][eLoSSquare.pointArray[i].Y].fillFlag=true;
            squareNet[eLoSSquare.pointArray[i].X][eLoSSquare.pointArray[i].Y].drImg=drImgArray[squareImgChoice];
        }
    }
    //将俄罗斯方块值传递进数组中
    public void TransferElosSquareToSquareNet(ELoSSquare elosSquare)
    {
        for(int i=0;i<4;i++)
        {
            squareNet[elosSquare.pointArray[i].getX()][elosSquare.pointArray[i].getY()].fillFlag=true;
            squareNet[elosSquare.pointArray[i].getX()][elosSquare.pointArray[i].getY()].drImg=drImgArray[squareImgChoice];
        }
    }
    public void SetDrawable(Context context)
    {
        Resources resources=context.getResources();
        drImgArray=new Drawable[3];
        drImgArray[0]= resources.getDrawable(R.drawable.square_blue);
        drImgArray[1]=resources.getDrawable(R.drawable.square_red);
        drImgArray[2]=resources.getDrawable(R.drawable.square_red_yellow);
        drImgGameBackground=resources.getDrawable(R.drawable.slight_red);
        drImgGameSquareBkg=resources.getDrawable(R.drawable.red_round_slight_blue_center);
        drImgStateBackground=resources.getDrawable(R.drawable.slight_grey);
        drImgStateSquareBkg=resources.getDrawable(R.drawable.slight_blue);
    }

    public void ClearSquareNet()
    {
        for(int i=0;i<rowNum;i++) {
            for (int j = 0; j < columnNum; j++) {
                 squareNet[i][j].fillFlag=false;
                squareNet[i][j].drImg=null;
            }
        }
    }

    public void ClearGameGrageAndStage()
    {
        gameGrade=0;
        gameStage=1;
        gameTime=0;
        gameTimeMips=0;
        timeInterval=600;
    }

    public void InitSquareNet()
    {
        squareNet=new Square[rowNum][columnNum];
        int i,j;
        for(i=0;i<rowNum;i++)
        {
            for(j=0;j<columnNum;j++)
            {
                squareNet[i][j]=new Square();
                squareNet[i][j].SetRect(new Rect(j*squareHeight+nXOffset,i*squareWidth+nYOffset,(j+1)*squareHeight+nXOffset,(i+1)*squareWidth+nYOffset));
            }
        }
    }
    public void DrawSquareNet(Canvas canvas)
    {
        int i,j;
        for(i=0;i<rowNum;i++)
        {
            for(j=0;j<columnNum;j++)
            {
                if(squareNet[i][j].fillFlag==true)
                {
                    DrawSquare(squareNet[i][j],canvas);
                }
            }
        }
    }
    public void DrawSquare(Square square,Canvas canvas)
    {
        if(square.fillFlag==false)
            return;
        Drawable drawable=square.GetDrawable();
        if(drawable==null)
            return;
        drawable.setBounds(square.GetRect());
        drawable.draw(canvas);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume()
    {
        super.onResume();
        pause=false;
        setContentView(R.layout.main);
        LinearLayout elos=(LinearLayout) findViewById(R.id.elos);
        Button left=(Button) findViewById(R.id.left);
        Button down=(Button) findViewById(R.id.down);
        Button change=(Button) findViewById(R.id.change);
        Button right=(Button) findViewById(R.id.right);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        params.leftMargin = 1;
        params.rightMargin = 1;
        params.weight = 1;
        elos.addView(gameView = new GameView(this), params);
        params=new LinearLayout.LayoutParams(params);
        params.weight=4;
        elos.addView(stateView=new StateView(this),params);
        SetDrawable(this);
        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(running&&!pause) MoveElosSquareByKind(1);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running&&!pause) MoveElosSquareByKind(2);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running&&!pause) MoveElosSquareByKind(0);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running==false||pause==true) return;
                curElosNext=ChangeElosSquareByKind(curElos);
                ClearElosSquare(curElos);
                if(TestElosSquareRangeAndAble(curElosNext))
                {
                    curElos=curElosNext;
                    TransferElosSquareToSquareNet(curElos);
                }
                else
                    ResumeElosSquare(curElos);
                gameView.postInvalidate();
                stateView.postInvalidate();
            }
        });
    }
    @Override
    public void onPause()
    {
        super.onPause();
        pause=true;
        Log.d("ELoS Activity State:","onPause");
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        ClearGameGrageAndStage();
        Log.d("ELoS Activity State:","onRestart");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        pause=true;
        Log.d("ELoS Activity State:","onStop");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("ELoS Activity State:","onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0,0,0,"start");
        menu.add(0,1,1,"exit");
        menu.add(0,2,2,"pause");
        menu.add(0,3,3,"continue");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if(menuItem.getItemId()==0)
        {
            running=true;
            pause=false;
            //怎样将第三项设置成pause
            ClearSquareNet();
            ClearGameGrageAndStage();
            //timeInterval=800;
           // stateView.postInvalidate();
            gameView.postInvalidate();
            squareTypeChoice=GenerateRandomSquareType();
            squareImgChoice=GenerateNextImg();
            curElos.SetType(squareTypeChoice,squareImgChoice);
            switch (squareTypeChoice)
            {
               // case 1:curElos.setCentrePoint(1,4);break;
                //case 2:curElos.setCentrePoint(0,4);break;
                case 1:curElos.setCentrePoint(1, 4);break;//|
                case 2:curElos.setCentrePoint(0, 4);break;//----
                case 3:curElos.setCentrePoint(0, 4);break;
                case 4:curElos.setCentrePoint(0, 5);break;
                case 5:curElos.setCentrePoint(0, 4);break;
                case 6:curElos.setCentrePoint(1, 3);break;
                case 7:curElos.setCentrePoint(2, 5);break;
                case 8:curElos.setCentrePoint(0, 5);break;
                case 9:curElos.setCentrePoint(0, 4);break;
                case 10:curElos.setCentrePoint(2, 4);break;
                case 11:curElos.setCentrePoint(1, 5);break;
                case 12:curElos.setCentrePoint(1, 4);break;
                case 13:curElos.setCentrePoint(1, 4);break;
                case 14:curElos.setCentrePoint(1, 5);break;
                case 15:curElos.setCentrePoint(0, 4);break;
                case 16:curElos.setCentrePoint(1, 4);break;
                case 17:curElos.setCentrePoint(1, 4);break;
                case 18:curElos.setCentrePoint(1, 4);break;
                case 19:curElos.setCentrePoint(1, 4);break;
                default:break;
            }
            ShowStartGame();


            curElos.GenernateElosSquare();
            stateView.ClearStateElosSquare();
            nextSquareTypeChoice=GenerateRandomSquareType();
            nextSquareImgChoice=GenerateNextImg();
            nextElos.SetType(nextSquareTypeChoice,nextSquareImgChoice);
            //根据不同的类型设置不同的起始点
            //switch (nextSquareTypeChoice)
          //  {
               // case 1:nextElos.setCentrePoint(1, 2);break;//|
              //  case 2:nextElos.setCentrePoint(0, 2);break;//----
              //  default:break;
            //}
            nextElos.SetRightViewCentrePointByChoice(nextElos.type);
            nextElos.GenernateElosSquare();

            TransferElosSquareToSquareNet(curElos);
            stateView.TransferElosSquareToSquareNet(nextElos);
            stateView.postInvalidate();
            gameView.postInvalidate();
            if(gameRuningThread==null)
            {
                gameRuningThread=new GameRuningThread();
                gameRuningThread.start();
            }
        }
        if(menuItem.getItemId()==1)
        {this.finish();}
        if(menuItem.getItemId()==2)
        {
            pause = true;
            gameView.setEnabled(false);
        }
        if(menuItem.getItemId()==3)
        {
            pause=false;
            menuItem.setEnabled(true);
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public class GameRuningThread extends Thread
    {
        @Override
        public void run()
        {
            super.run();
            //if(gameOverActivityRunningFlag)
               // finish();
            while(running)
            {
                while(pause==false)
                {
                    stateView.postInvalidate();
                    try{
                        sleep(timeInterval);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    gameTimeMips=gameTimeMips+timeInterval;
                    if(gameTimeMips/1000>=1)
                    {
                        gameTime++;
                        gameTimeMips=gameTimeMips-1000;
                    }

                    curElosNext=MoveElosSquareDown(curElos);//试探移动
                    Log.d("num:",""+ TestArray());
                    ClearElosSquare(curElos);//先清除
                    Log.d("num:",""+ TestArray());
                    if(TestElosSquareRangeAndAble(curElosNext))//测试试探移动结果有没有超出范围
                    {
                        curElos=curElosNext;
                        TransferElosSquareToSquareNet(curElos);
                        Log.d("num:",""+ TestArray());
                    }
                    else
                    {
                        ResumeElosSquare(curElos);
                        int rowLine=EliminateSquareNet(curElos); //看看有无成功消除的
                        if(rowLine>0)
                        {
                            AddGradeByRemoveRowNum(rowLine);
                            ChangeStageByGameGrade();
                        }
                        curElos.TransferRightElosSquareToLeftType(curElos,nextElos);
                        squareTypeChoice=nextSquareTypeChoice;
                        squareImgChoice=nextSquareImgChoice;
                        nextSquareTypeChoice=GenerateRandomSquareType();
                        nextSquareImgChoice=GenerateNextImg();
                        if(TestElosSquareRangeAndAble(curElos)==false)//game over
                        {
                            ClearSquareNet();
                            pause=true;
                            gameOverActivityRunningFlag=true;
                            Intent intent=new Intent(ELoS.this,GameOverActivity.class);
                            startActivity(intent);
                        }
                        else
                            TransferElosSquareToSquareNet(curElos);
                        //消除右边之前俄罗斯块
                        stateView.ClearStateElosSquare();

                        nextElos.SetType(nextSquareTypeChoice,nextSquareImgChoice);
                        //根据不同的类型设置不同的起始点
                        nextElos.SetRightViewCentrePointByChoice(nextSquareTypeChoice);
                        nextElos.GenernateElosSquare();
                        stateView.TransferElosSquareToSquareNet(nextElos);
                    }
                   gameView.postInvalidate();
                }
            }
        }
    }
    public int TestArray()
    {
        int num=0;
        for(int i=0;i<rowNum;i++)
        {
            for(int j=0;j<columnNum;j++)
                if(squareNet[i][j].fillFlag==true)
                 num++;
        }
        return num;
    }

    public class GameView extends View{

        public GameView(Context context)
        {
            super(context);
        }

        public void DrawGameBounderWithImg(Canvas canvas)
        {
            //Rect rt=new Rect(0,0,canvas.getWidth(),canvas.getHeight());
            //drImgGameBackground.setBounds(rt);//设置游戏界面背景
           // drImgGameBackground.draw(canvas);
            for(int i=0;i<rowNum;i++)
            {
                for(int j=0;j<columnNum;j++)
                {
                    drImgGameSquareBkg.setBounds(squareNet[i][j].GetRect());//设置游戏界面背景
                    drImgGameSquareBkg.draw(canvas);
                }
            }
        }
        public void DrawGameBounder(Canvas canvas)
        {
            Paint pt=new Paint();
            pt.setColor(Color.RED);
            for(int i=1;i<rowNum;i++)
            {
                canvas.drawLine(nXOffset,nYOffset+i*squareHeight,nXOffset+gameSquareScreenWidth,nYOffset+i*squareHeight,pt);
            }
            for(int i=1;i<columnNum;i++)
            {
                canvas.drawLine(nXOffset+i*squareWidth,nYOffset,nXOffset+i*squareWidth,nYOffset+gameSquareScreenHeight,pt);
            }
        }
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Rect rt=new Rect(0,0,canvas.getWidth(),canvas.getHeight());
           drImgGameBackground.setBounds(rt);//设置游戏界面背景
            drImgGameBackground.draw(canvas);

            if(initialGameScreen==false)
            {
                gameScreenHeight=canvas.getHeight();
                gameScreenWidth=canvas.getWidth();
                squareWidth=gameScreenWidth/10;
                squareHeight=squareWidth;
                rowNum=gameScreenHeight/squareHeight;
                columnNum=10;
                gameSquareScreenHeight=squareHeight*rowNum;
                gameSquareScreenWidth=squareWidth*columnNum;
                nXOffset=(gameScreenWidth-gameSquareScreenWidth)/2;
                nYOffset=(gameScreenHeight-gameSquareScreenHeight)/2;
                InitSquareNet();
                initialGameScreen=true;
            }
            DrawGameBounderWithImg(canvas);
           // DrawGameBounder(canvas);
            //drImgGameSquareBkg.setBounds(new Rect(nXOffset,nYOffset,nXOffset+gameSquareScreenWidth,nYOffset+gameSquareScreenHeight));
           // drImgGameSquareBkg.draw(canvas);
            DrawSquareNet(canvas);
            if(gameOverFlag)
            {
                Toast.makeText(ELoS.this,"Game Over, The Grade is:"+gameGrade,Toast.LENGTH_SHORT).show();
                gameOverFlag=false;
                //再加上一个切换页面
            }
        }
    }

    public class StateView extends View
    {
        private int screenWidth;
        private int screenHeight;
        private int squareDisplayWidth;
        private int squareDisplayHeight;
        private int stateSquareWidth;
        private int stateSquareHeight;
        private int xOffset;
        private int yOffset;
        private boolean initialStateScreen=false;
        private Square[][] nextSquareNet;
        public void InitNextSquareNet()
        {
            nextSquareNet=new Square[6][6];
            int i,j;
            for(i=0;i<6;i++)
            {
                for(j=0;j<6;j++)
                {
                    nextSquareNet[i][j]=new Square();
                    nextSquareNet[i][j].SetRect(new Rect(j*stateSquareHeight+xOffset,i*stateSquareWidth+yOffset,(j+1)*stateSquareHeight+xOffset,(i+1)*stateSquareWidth+yOffset));
                }
            }
        }
        public void DrawNextSquareNet(Canvas canvas)
        {
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<6;j++) {
                    if (nextSquareNet[i][j].fillFlag == true)
                    {
                        DrawSquare(nextSquareNet[i][j],canvas);
                    }
                }
            }
        }
        public StateView(Context context)
        {
            super(context);
        }
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            if(initialStateScreen==false)
            {
                screenWidth=canvas.getWidth();
                screenHeight=canvas.getHeight();
                stateSquareWidth=screenWidth/6;
                stateSquareHeight=stateSquareWidth;
                squareDisplayWidth=stateSquareWidth*6;
                squareDisplayHeight=stateSquareHeight*6;
                xOffset=(screenWidth-squareDisplayWidth)/2;
                yOffset=20;
                InitNextSquareNet();
                initialStateScreen=true;
            }
            Paint pt=new Paint();
            pt.setColor(Color.GRAY);
            canvas.drawRect(new Rect(0,0,screenWidth,screenHeight),pt);
            drImgStateBackground.setBounds(new Rect(0,0,screenWidth,screenHeight));
            drImgStateBackground.draw(canvas);
            drImgStateSquareBkg.setBounds(new Rect(xOffset,yOffset,squareDisplayWidth+xOffset,squareDisplayHeight+yOffset));
            drImgStateSquareBkg.draw(canvas);
            DrawNextSquareNet(canvas);
            DrawGradeAndTime(canvas,xOffset,squareDisplayHeight+160);
            //ClearStateElosSquare();
        }
        private void DrawGradeAndTime(Canvas canvas,int x,int y)
        {
            Paint pt=new Paint();
            pt.setColor(Color.GRAY);
            pt.setTextSize(20);
            canvas.drawText("stage:"+gameStage,x,y,pt);
            canvas.drawText("grade:"+gameGrade,x,y+80,pt);
            canvas.drawText("time:"+gameTime,x,y+160,pt);
        }
        public void TransferElosSquareToSquareNet(ELoSSquare elosSquare)
        {
            for(int i=0;i<4;i++)
            {
                nextSquareNet[elosSquare.pointArray[i].getX()][elosSquare.pointArray[i].getY()].fillFlag=true;
                nextSquareNet[elosSquare.pointArray[i].getX()][elosSquare.pointArray[i].getY()].drImg=drImgArray[nextSquareImgChoice];
            }
        }
        public void ClearStateElosSquare()
        {
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<6;j++)
                {
                    nextSquareNet[i][j].fillFlag=false;
                }
            }
        }
    }

}
