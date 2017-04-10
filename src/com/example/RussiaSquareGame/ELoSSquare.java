package com.example.RussiaSquareGame;

import android.graphics.Rect;

/**
 * Created by peng on 2017/1/20.
 */
public class ELoSSquare {
    public CoordinatePair centrePoint;
    public int type;
    public int imgType;
    public CoordinatePair[] pointArray;
    public ELoSSquare()
    {
        pointArray=new CoordinatePair[4];//the size of array is 4 because ElosSquare is made up of four square;
        for(int i=0;i<4;i++)
        {
            pointArray[i]=new CoordinatePair();
        }
    }
    public ELoSSquare(CoordinatePair coordinatePair,int _type,int _imgType)
    {
        centrePoint=coordinatePair;
        type=_type;
        imgType=_imgType;
        pointArray=new CoordinatePair[4];//the size of array is 4 because ElosSquare is made up of four square;
        pointArray[0]=centrePoint;
    }
    public void SetType(int _type,int _imgType)
    {
        type=_type;
        imgType=_imgType;
    }
    public void setCentrePoint(int x,int y)
    {
        if(centrePoint==null)
        centrePoint=new CoordinatePair(x,y);
        else
        centrePoint.setX_Y(x,y);
        pointArray[0]=centrePoint;
    }
    public void SetRightViewCentrePointByChoice(int choice)
    {
        switch (choice)
        {
            case 1:setCentrePoint(2, 2);break;//|
            case 2:setCentrePoint(1, 2);break;//----
            case 3:setCentrePoint(1, 2);break;
            case 4:setCentrePoint(1, 3);break;
            case 5:setCentrePoint(1, 2);break;
            case 6:setCentrePoint(2, 1);break;
            case 7:setCentrePoint(3, 3);break;
            case 8:setCentrePoint(1, 3);break;
            case 9:setCentrePoint(1, 2);break;
            case 10:setCentrePoint(3, 2);break;
            case 11:setCentrePoint(2, 3);break;
            case 12:setCentrePoint(2, 2);break;
            case 13:setCentrePoint(2, 2);break;
            case 14:setCentrePoint(2, 3);break;
            case 15:setCentrePoint(1, 2);break;
            case 16:setCentrePoint(2, 2);break;
            case 17:setCentrePoint(2, 2);break;
            case 18:setCentrePoint(2, 2);break;
            case 19:setCentrePoint(2, 2);break;
            default:break;
        }
    }
    public void SetLeftViewCentrePointByChoice(int choice)
    {
        switch (choice)
        {
            case 1:setCentrePoint(1, 4);break;//|
            case 2:setCentrePoint(0, 4);break;//----
            default:break;
        }
    }
    public void GenernateElosSquare()
    {
        //for(int i=1;i<=3;i++)
          //  pointArray[i]=new CoordinatePair();
     switch(type)
     {
         case 1:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y);//  -
             pointArray[2].setX_Y(centrePoint.X+1, centrePoint.Y);// *
             pointArray[3].setX_Y(centrePoint.X+2, centrePoint.Y);// -
             break;}                                             //  -
         case 2:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);// -*--
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);
             pointArray[3].setX_Y(centrePoint.X,centrePoint.Y+2);
             break;
         }
         case 3:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y+1);// *-
             pointArray[2].setX_Y(centrePoint.X+1,centrePoint.Y);// --
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y+1);
             break;
         }
         case 4:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-2);// --*
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y-1);//   -
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);
             break;
         }
         case 5:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y+1);//   *-
             pointArray[2].setX_Y(centrePoint.X+1,centrePoint.Y);//   -
             pointArray[3].setX_Y(centrePoint.X+2,centrePoint.Y);//   -
             break;
         }
         case 6:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y);// -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);// *--
             pointArray[3].setX_Y(centrePoint.X,centrePoint.Y+2);
             break;
         }
         case 7:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);//  -
             pointArray[2].setX_Y(centrePoint.X-1,centrePoint.Y);//  -
             pointArray[3].setX_Y(centrePoint.X-2,centrePoint.Y);// -*
             break;
         }
         case 8:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);//  -*
             pointArray[2].setX_Y(centrePoint.X+1,centrePoint.Y);//   -
             pointArray[3].setX_Y(centrePoint.X+2,centrePoint.Y);//   -
             break;
         }
         case 9:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y+1);//  *--
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+2);//  -
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);
             break;
         }
         case 10:{
             pointArray[1].setX_Y(centrePoint.X-2,centrePoint.Y);//  -
             pointArray[2].setX_Y(centrePoint.X-1,centrePoint.Y);//  -
             pointArray[3].setX_Y(centrePoint.X,centrePoint.Y+1);//  *-
             break;
         }
         case 11:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-2);//    -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y-1);//  --*
             pointArray[3].setX_Y(centrePoint.X-1,centrePoint.Y);
             break;
         }
         case 12:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y);//  -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);//  *-
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);//  -
             break;
         }
         case 13:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);//   -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);//  -*-
             pointArray[3].setX_Y(centrePoint.X-1,centrePoint.Y);
             break;
         }
         case 14:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);//  -
             pointArray[2].setX_Y(centrePoint.X-1,centrePoint.Y);// -*
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);//  -
             break;
         }
         case 15:{
             pointArray[1].setX_Y(centrePoint.X,centrePoint.Y-1);//  -*-
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);//   -
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);
             break;
         }
         case 16:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y-1);// --
             pointArray[2].setX_Y(centrePoint.X-1,centrePoint.Y);//    *-
             pointArray[3].setX_Y(centrePoint.X,centrePoint.Y+1);
             break;
         }
         case 17:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y+1);// -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);//  *-
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y);//  -
             break;
         }
         case 18:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y);//    --
             pointArray[2].setX_Y(centrePoint.X-1,centrePoint.Y+1);// -*
             pointArray[3].setX_Y(centrePoint.X,centrePoint.Y-1);
             break;
         }
         case 19:{
             pointArray[1].setX_Y(centrePoint.X-1,centrePoint.Y);//  -
             pointArray[2].setX_Y(centrePoint.X,centrePoint.Y+1);//  *-
             pointArray[3].setX_Y(centrePoint.X+1,centrePoint.Y+1);// -
             break;
         }
         default:{break;}
     }
    }

    //将stateView中俄罗斯方块形状映射到gameView中
    public void TransferRightElosSquareToLeftType(ELoSSquare dst,ELoSSquare rsc)
    {
        dst.type=rsc.type;
        dst.imgType=rsc.imgType;
        if(dst.centrePoint==null)
        {
            dst.setCentrePoint(rsc.centrePoint.X-1,rsc.centrePoint.Y+2);
        }
        else
        {
            dst.centrePoint.X=rsc.centrePoint.X-1;
            dst.centrePoint.Y=rsc.centrePoint.Y+2;
        }
        dst.GenernateElosSquare();
    }

    protected class CoordinatePair
    {
        public int X;
        public int Y;
        public CoordinatePair(int x,int y)
        {
            X=x;
            Y=y;
        }
        public CoordinatePair()
        {}
        public void setX(int x) {
            X = x;
        }

        public void setY(int y)
        {
            Y = y;
        }

        public void setX_Y(int x,int y)
        {
            X= x;
            Y = y;
        }

        public int getX() {
            return X;
        }

        public int getY() {
            return Y;
        }
    }
}

