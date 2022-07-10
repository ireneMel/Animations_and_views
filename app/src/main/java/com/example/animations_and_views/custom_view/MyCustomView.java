package com.example.animations_and_views.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.icu.util.LocaleData;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.example.animations_and_views.R;

public class MyCustomView extends View {

    private Paint mPaint, mPaintButton;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private int mButtonColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;

    private float prev_x;
    private float prev_y;

    private static final int OFFSET = 40;
    private static final float TOUCH_TOLERANCE = 4; //точність - мінімальна кількість пікселів для перемальоки, щоб оновлення не відбувалось дуже часто

    public MyCustomView(Context context) {
        this(context, null);
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBackgroundColor = ResourcesCompat.getColor(getResources(), R.color.background, null);
        mDrawColor = ResourcesCompat.getColor(getResources(), R.color.draw, null);
        mButtonColor = ResourcesCompat.getColor(getResources(), R.color.white, null);

        mPath = new Path();
        mPaint = new Paint();
        mPaintButton = new Paint();
        mPaint.setColor(mDrawColor);
        mPaintButton.setColor(mButtonColor);
        // Smooths out edges of what is drawn without affecting shape.
        mPaint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); // default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        mPaint.setStrokeWidth(10); // default: Hairline-width (really thin)

        mPaint.setTextSize(95);

         r = new Rect(OFFSET, OFFSET, OFFSET * 9, OFFSET * 6);
    }

    Rect r;

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        prev_x = x;
        prev_y = y;
    }

    private boolean isEraser(float x, float y) {
       return (x >= OFFSET && x <= OFFSET * 9 && y >= OFFSET && y <= OFFSET * 7);
    }

    private void eraserPressed(float x, float y) {
        if (isEraser(x,y)) {
            Log.v("ERASER", "erases");
            //clip eraser, draw canvas again
            mExtraCanvas.drawColor(mBackgroundColor);
            mPaint.setColor(mDrawColor);
            invalidate();
        } else {
            touchStart(x, y);
        }
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - prev_x);
        float dy = Math.abs(y - prev_y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(prev_x, prev_y, (x + prev_x) / 2, (y + prev_y) / 2);
            prev_x = x;
            prev_y = y;
            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
        //draw button to erase
        canvas.drawRect(r, mPaintButton);
        canvas.drawText("Erase", OFFSET * 2, OFFSET * 4, mPaint);

        canvas.clipOutRect(r);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mExtraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mExtraCanvas = new Canvas(mExtraBitmap);
        mExtraCanvas.drawColor(mBackgroundColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eraserPressed(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mPath.reset();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            default:
        }
        return true;
    }
}
