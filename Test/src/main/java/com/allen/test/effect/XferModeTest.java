package com.allen.test.effect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;

public class XferModeTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(new XfermodesView(this));
    }

    static class XfermodesView extends View {
        private final Paint mBluePaint;
        private final Paint mRedPaint;

        XfermodesView(Context c) {
            super(c);

            mBluePaint = new Paint();
            mBluePaint.setColor(0xff0000ff);
            
            mRedPaint = new Paint();
            mRedPaint.setColor(0x7fff0000);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRGB(0xff, 0xff, 0xff);

            canvas.translate(100.0f, 100.0f);
            
            // SRC modes
            canvas.save();

            drawRects(canvas, Mode.SRC_OVER);
            canvas.translate(0.0f, 200.0f);

            drawRects(canvas, Mode.SRC_IN);
            canvas.translate(0.0f, 200.0f);            

            drawRects(canvas, Mode.SRC_OUT);
            canvas.translate(0.0f, 200.0f);

            drawRects(canvas, Mode.SRC_ATOP);
            canvas.translate(0.0f, 200.0f);
            
            drawRects(canvas, Mode.SRC);

            canvas.restore();
            
            canvas.translate(200.0f, 0.0f);
            
            // DST modes
            canvas.save();
//
            drawRects(canvas, Mode.DST_OVER);
            canvas.translate(0.0f, 200.0f);

            drawRects(canvas, Mode.DST_IN);
            canvas.translate(0.0f, 200.0f);            

            drawRects(canvas, Mode.DST_OUT);
            canvas.translate(0.0f, 200.0f);

            drawRects(canvas, Mode.DST_ATOP);
            canvas.translate(0.0f, 200.0f);
            
            drawRects(canvas, Mode.DST);

            canvas.restore();
            
            canvas.translate(200.0f, 0.0f);
            
            // Other modes
            canvas.save();
//
            drawRects(canvas, Mode.CLEAR);
            canvas.translate(0.0f, 200.0f);

            drawRects(canvas, Mode.XOR);
            
            canvas.translate(0.0f, 200.0f);
            
            mBluePaint.setAlpha(127);
            canvas.drawRect(0.0f, 0.0f, 100.0f, 100.0f, mBluePaint);
            
            canvas.translate(0.0f, 200.0f);
            
            mBluePaint.setAlpha(10);
            mBluePaint.setColor(0x7f0000ff);
            canvas.drawRect(0.0f, 0.0f, 100.0f, 100.0f, mBluePaint);
            
            mBluePaint.setColor(0xff0000ff);
            mBluePaint.setAlpha(255);

            canvas.restore();
        }

        private void drawRects(Canvas canvas, PorterDuff.Mode mode) {	
            canvas.drawRect(0.0f, 0.0f, 100.0f, 100.0f, mBluePaint);

            canvas.save();
            canvas.translate(50.0f, 50.0f);
            mRedPaint.setXfermode(new PorterDuffXfermode(mode));
            canvas.drawRect(0.0f, 0.0f, 100.0f, 100.0f, mRedPaint);
            canvas.restore();
        }
    }
}
