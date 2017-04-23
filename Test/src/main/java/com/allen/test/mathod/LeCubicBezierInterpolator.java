package com.allen.test.mathod;

import com.allen.test.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.Interpolator;


/**
 * Created by zhouxufeng on 16-9-10.
 */
public class LeCubicBezierInterpolator implements Interpolator {

    private final static int ACCURACY = 4096;

    private int mLastI = 0;

    private final PointF mControlPoint1 = new PointF();

    private final PointF mControlPoint2 = new PointF();

    public LeCubicBezierInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    /** @hide */
    public LeCubicBezierInterpolator(Resources res, Resources.Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.CubicBezierInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.CubicBezierInterpolator);
        }

        mControlPoint1.x = a.getFloat(R.styleable.CubicBezierInterpolator_startX, 1.0f);
        mControlPoint1.y = a.getFloat(R.styleable.CubicBezierInterpolator_startY, 1.0f);
        mControlPoint2.y = a.getFloat(R.styleable.CubicBezierInterpolator_endX, 1.0f);
        mControlPoint2.y = a.getFloat(R.styleable.CubicBezierInterpolator_endY, 1.0f);
        a.recycle();
    }

    /**

     * 璁剧疆涓棿涓や釜鎺у埗鐐�.<br>

     * 鍦ㄧ嚎宸ュ叿: http://cubic-bezier.com/<br>

     *

     * @param x1

     * @param y1

     * @param x2

     * @param y2

     */

    public  LeCubicBezierInterpolator(float x1, float y1, float x2, float y2) {

        mControlPoint1.x = x1;

        mControlPoint1.y = y1;

        mControlPoint2.x = x2;

        mControlPoint2.y = y2;
    }



    @Override

    public float getInterpolation(float input) {

        float t = input;

        // 杩戜技姹傝Вt鐨勫�糩0,1]

        for (int i = mLastI; i < ACCURACY; i++) {

            t = 1.0f * i / ACCURACY;

            double x = cubicCurves(t, 0, mControlPoint1.x, mControlPoint2.x, 1);

            if (x >= input) {

                mLastI = i;

                break;

            }

        }

        double value = cubicCurves(t, 0, mControlPoint1.y, mControlPoint2.y, 1);

        if (value > 0.999d) {

            value = 1;

            mLastI = 0;

        }

        return (float) value;

    }



    /**

     * 姹備笁娆¤礉濉炲皵鏇茬嚎(鍥涗釜鎺у埗鐐�)涓�涓偣鏌愪釜缁村害鐨勫��.<br>

     * 鍙傝�冭祫鏂�: <em> http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/ </em>

     *

     * @param t

     *            鍙栧�糩0, 1]

     * @param value0

     * @param value1

     * @param value2

     * @param value3

     * @return

     */

    public static double cubicCurves(double t, double value0, double value1,

                                     double value2, double value3) {

        double value;

        double u = 1 - t;

        double tt = t * t;

        double uu = u * u;

        double uuu = uu * u;

        double ttt = tt * t;



        value = uuu * value0;

        value += 3 * uu * t * value1;

        value += 3 * u * tt * value2;

        value += ttt * value3;

        return value;

    }
}
