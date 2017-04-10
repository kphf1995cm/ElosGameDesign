package com.example.RussiaSquareGame;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by peng on 2017/1/20.
 */
public class Square {
    public boolean fillFlag;// identity the Square have value
    public Drawable drImg;
    public Rect rtSquare;
    public Square()
    {
        fillFlag=false;
        drImg=null;
        rtSquare=new Rect();
    }
    public void SetParms(boolean bool,Drawable drawable,Rect rect)
    {
        fillFlag=bool;
        drImg=drawable;
        rtSquare=rect;
    }
    public Square(boolean bool,Drawable drawable,Rect rect)
    {
        fillFlag=bool;
        drImg=drawable;
        rtSquare=rect;
    }
    public void SetFillFlag(boolean bool)
    {fillFlag=bool;}
    public boolean GetFillFlag(){return fillFlag;}
    public void SetRect(Rect rect)
    {
        rtSquare.set(rect);
    }
    public Rect GetRect(){return rtSquare;}
    public void SetDrawable(Drawable drawable)
    {
     drImg=drawable;
    }
    public Drawable GetDrawable()
    {return drImg;}
}
