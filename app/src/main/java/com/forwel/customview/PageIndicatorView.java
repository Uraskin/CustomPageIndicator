package com.forwel.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PageIndicatorView extends View implements PageIndicator {

    private static final int INSIDE_PADDING = 80;

    private Paint mPaint;

    private int mCurrentIndex = 0;

    private int mPageCount;         // total indicators count

    private Float mRadiusActive;    // active size

    private Float mRadiusNonActive; // non active size

    private int mColorActive;       // active color

    private int mColorNonActive;    // non active color

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageIndicatorView);

        try {
            mPageCount = typedArray.getInt(R.styleable.PageIndicatorView_pi_page_count, 0);
            mRadiusActive = typedArray.getDimension(R.styleable.PageIndicatorView_pi_radius_active, 0);
            mRadiusNonActive = typedArray.getDimension(R.styleable.PageIndicatorView_pi_radius_non_active, 0);
            mColorActive = typedArray.getInt(R.styleable.PageIndicatorView_pi_color_active, 0);
            mColorNonActive = typedArray.getInt(R.styleable.PageIndicatorView_pi_color_non_active, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void setPageCount(int count) {
        mPageCount = count;
    }

    @Override
    public void setActiveIndicator(int index) {
        mCurrentIndex = index;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //float maxWidth = mRadiusActive * mPageCount + ((mPageCount-1) * INSIDE_PADDING);
        float maxHeight = mRadiusActive;

        //int desiredWidth = Math.round(maxWidth + getPaddingLeft() + getPaddingRight());
        int desiredHeight = Math.round(maxHeight*2f + getPaddingBottom() + getPaddingTop());

        //int width = doMeasureSize(desiredWidth, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = doMeasureSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int doMeasureSize(int size, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.EXACTLY :
                return  specSize;
            case MeasureSpec.AT_MOST:
                if (size < specSize)
                    return size;
                else
                    return specSize;
            case MeasureSpec.UNSPECIFIED:
            default:return size;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //calculate start draw point
        float maxWidth = mRadiusActive * mPageCount + ((mPageCount-1) * INSIDE_PADDING);
        int desiredWidth = Math.round(maxWidth + getPaddingLeft() + getPaddingRight());
        int x = (getWidth() - desiredWidth) / 2 + (Math.round(mRadiusActive /2)*mPageCount);

        int y = getHeight() / 2;


        for (int i = 0; i < mPageCount; i++) {
            if (i == mCurrentIndex) {
                mPaint.setColor(mColorActive);
                canvas.drawCircle(x + i * INSIDE_PADDING, y, mRadiusActive, mPaint);
            } else {
                mPaint.setColor(mColorNonActive);
                canvas.drawCircle(x + i * INSIDE_PADDING, y, mRadiusNonActive, mPaint);
            }
        }
    }
}
