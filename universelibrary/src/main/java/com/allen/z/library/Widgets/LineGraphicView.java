package com.allen.z.library.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.allen.z.library.R;

import java.util.ArrayList;

/**********************************************************
 * @文件名称：LineGraphicView.java
 * @文件作者：rzq
 * @创建时间：2015年5月27日 下午3:05:19
 * @文件描述：自定义简单曲线图
 * @修改历史：2015年5月27日创建初始版本
 **********************************************************/
class LineGraphicView extends View
{
    /**
     * 公共部分
     */
    private static final int CIRCLE_SIZE = 10;

    private static enum Linestyle
    {
        Line, Curve
    }

    private Context mContext;
    private Paint mPaint;
    private Resources res;
    private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Curve;

    private float canvasHeight;
    private float canvasWidth;
    private float bheight = 0;
    private float blwidh;
    private boolean isMeasure = true;

    /**
     * Y轴间距值
     */
    private int yStart;
    private int yEnd;
    private int yNum;
    private String yUnit;

    private int marginTop = 20;
    private int marginBottom = 40;
    /**
     * X轴间距值
     */
    private int xStart;
    private int xEnd;
    private int xNum;
    private String xUnit;

    private int marginLeft = 30;
    private int marginRight = 40;
    /**
     * 曲线上总点数
     */
    private Point[] mPoints;


    public LineGraphicView(Context context)
    {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            bheight = canvasHeight - marginBottom;
            blwidh = dip2px(marginLeft);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(res.getColor(R.color.color_f2f2f2));

        drawAllXLine(canvas);
        // 画直线（纵向）
        drawAllYLine(canvas);
        // 点的操作设置
//        mPoints = getPoints();

        mPaint.setColor(res.getColor(R.color.color_ff4631));
        mPaint.setStrokeWidth(dip2px(2.5f));
        mPaint.setStyle(Paint.Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLine(canvas);
        }

        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mPoints.length; i++)
        {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 2, mPaint);
        }
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
//        float spacingHeight = (bheight - marginTop) / yNum;
//        int spacingLabel = (yEnd - yStart) / yNum;
//        for (int i = 0; i < yNum; i++)
//        {
//            canvas.drawLine(blwidh, bheight - spacingHeight * i, (canvasWidth - blwidh),
//                    bheight - spacingHeight * i, mPaint);// Y坐标
//            drawText(String.valueOf(averageValue * i), blwidh / 2, bheight - (bheight / spacingHeight) * i + marginTop,
//                    canvas);
//        }
    }

    /**
     * 画所有纵向表格，包括Y轴    
     */
    private void drawAllYLine(Canvas canvas)
    {
//        for (int i = 0; i < yRawData.size(); i++)
//        {
//            xList.add(blwidh + (canvasWidth - blwidh) / yRawData.size() * i);
//            canvas.drawLine(blwidh + (canvasWidth - blwidh) / yRawData.size() * i, marginTop, blwidh
//                    + (canvasWidth - blwidh) / yRawData.size() * i, bheight + marginTop, mPaint);
//            drawText(xRawDatas.get(i), blwidh + (canvasWidth - blwidh) / yRawData.size() * i, bheight + dip2px(26),
//                    canvas);// X坐标
//        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(res.getColor(R.color.color_999999));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

//    private Point[] getPoints()
//    {
////        Point[] points = new Point[yRawData.size()];
////        for (int i = 0; i < yRawData.size(); i++)
////        {
////            int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));
////
////            points[i] = new Point(xList.get(i), ph + marginTop);
////        }
//        return points;
//    }

    public void setXLabels(int xStart, int xEnd, int xNum, String xUnit)
    {

    }

    public void setYLabels(int yStart, int yEnd, int yNum, String yUnit)
    {

    }


    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

}