/**
 * Copyright 2016 andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.refactor.cutoloadingview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/9 14:42
 * Description : Cuto loading view.
 */
public class CutoLoadingView extends View {

    private static final int DEFAULT_WIDTH  = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private static final int DEFAULT_CIRCLE_RADIUS = 4;

    private static final int DEFAULT_MAX_PROGRESS  = 100;
    private static final int DEFAULT_STROKE_SIZE   = 2;
    private static final int DEFAULT_PAINT_COLOR   = Color.WHITE;
    private static final int DEFAULT_DURATION      = 1600;

    private static final int STATUS_FULL_STEPS  = 0;
    private static final int STATUS_LOADING     = 1;

    private static final int X              = 0;
    private static final int Y              = 1;

    private ValueAnimator mValueAnimator;
    private Paint mPaint;
    private int mDuration; // millisecond

    private int mPaintColor;
    private int mStrokeSize;
    private int mCircleRadius;

    private int mRotateDegree;
    private float mTotalProgressVal;
    private float mCurrentProgressVal;

    private float mGap; // give padding space for the shape in this area. mGap = Math.sqrt(r * r + r * r) - r;
    private float[] mCenterPos;
    private float[] mLeftTopPos, mLeftBottomPos, mRightTopPos, mRightBottomPos;

    @IntDef({STATUS_FULL_STEPS, STATUS_LOADING})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface Status {}

    private @Status int mCurState = STATUS_FULL_STEPS;

    public CutoLoadingView(Context context) {
        this(context, null);
    }

    public CutoLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutoLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CutoLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        // TODO: 16/10/9 Get attrs
        mPaintColor = DEFAULT_PAINT_COLOR;
        mStrokeSize = dp2px(DEFAULT_STROKE_SIZE);
        mCircleRadius = dp2px(DEFAULT_CIRCLE_RADIUS);
        mTotalProgressVal = DEFAULT_MAX_PROGRESS;
        mDuration = DEFAULT_DURATION;

        mCenterPos = new float[2];
        mLeftTopPos = new float[2];
        mRightTopPos = new float[2];
        mLeftBottomPos = new float[2];
        mRightBottomPos = new float[2];

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(mStrokeSize);
        mPaint.setColor(mPaintColor);

    }

    public void stopLoadingAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mRotateDegree = 0;
        mCurState = STATUS_FULL_STEPS;
        invalidate();
    }

    public void startLoadingAnim() {
        mValueAnimator = ValueAnimator.ofFloat(mTotalProgressVal * 0.6f, mTotalProgressVal);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurState = STATUS_LOADING;
                setProgress((Float) valueAnimator.getAnimatedValue());
                Log.d("valueAnimatorValue", String.valueOf(valueAnimator.getAnimatedValue()));
            }
        });
        mValueAnimator.start();
    }

    @SuppressWarnings("unused")
    private void startPreLoadingAnim() {
        mValueAnimator = ValueAnimator.ofFloat(0, mTotalProgressVal);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurState = valueAnimator.getAnimatedFraction() < 0.5f ? STATUS_FULL_STEPS : STATUS_LOADING;
                setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });
        mValueAnimator.start();
    }

    public void setProgress(float progress) {
        int calcRotateDegree = progress < mTotalProgressVal * 0.5 ? (int) (180 * progress / mTotalProgressVal * 2) : 0;
        mCurrentProgressVal = progress;
        mRotateDegree = calcRotateDegree;
        invalidate();
    }

    public void setMax(float max) {
        mTotalProgressVal = max;
    }

    public void setDuration(int millisecond) {
        mDuration = millisecond;
    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidthSize(widthMeasureSpec), measureHeightSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (STATUS_FULL_STEPS == mCurState) {
            canvas.rotate(mRotateDegree, mCenterPos[X], mCenterPos[Y]);
        }

        int indexOfDrawStrokeAndFill = mCurrentProgressVal >= mTotalProgressVal * 0.6 ? (int) ((mCurrentProgressVal / mTotalProgressVal - 0.5) * 10 - 1) : -1;
        // draw circle
        if (-1 == indexOfDrawStrokeAndFill) {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mLeftTopPos[X], mLeftTopPos[Y], mCircleRadius, mPaint);
            canvas.drawCircle(mRightTopPos[X], mRightTopPos[Y], mCircleRadius, mPaint);
            canvas.drawCircle(mRightBottomPos[X], mRightBottomPos[Y], mCircleRadius, mPaint);
            canvas.drawCircle(mLeftBottomPos[X], mLeftBottomPos[Y], mCircleRadius, mPaint);
        } else {
            // have rotated 180 degree
            mPaint.setStyle(0 == indexOfDrawStrokeAndFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
            canvas.drawCircle(mLeftTopPos[X], mLeftTopPos[Y], mCircleRadius, mPaint);
            mPaint.setStyle(1 == indexOfDrawStrokeAndFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
            canvas.drawCircle(mRightTopPos[X], mRightTopPos[Y], mCircleRadius, mPaint);
            mPaint.setStyle(2 == indexOfDrawStrokeAndFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
            canvas.drawCircle(mRightBottomPos[X], mRightBottomPos[Y], mCircleRadius, mPaint);
            mPaint.setStyle(3 == indexOfDrawStrokeAndFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
            canvas.drawCircle(mLeftBottomPos[X], mLeftBottomPos[Y], mCircleRadius, mPaint);
        }

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // draw line left --> right
        canvas.drawLine(mLeftTopPos[X] + mCircleRadius, mLeftTopPos[Y], mRightTopPos[X] - mCircleRadius, mRightTopPos[Y], mPaint);
        canvas.drawLine(mLeftBottomPos[X] + mCircleRadius, mLeftBottomPos[Y], mRightBottomPos[X] - mCircleRadius, mRightBottomPos[Y], mPaint);

        // draw line top --> bottom
        canvas.drawLine(mLeftTopPos[X], mLeftTopPos[Y] + mCircleRadius, mLeftBottomPos[X], mLeftBottomPos[Y] - mCircleRadius, mPaint);
        canvas.drawLine(mRightTopPos[X], mRightTopPos[Y] + mCircleRadius, mRightBottomPos[X], mRightBottomPos[Y] - mCircleRadius, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();
        mCenterPos[X] = (width - paddingRight + paddingLeft) >> 1;
        mCenterPos[Y] = (height - paddingBottom + paddingTop) >> 1;
        mGap = (float) (Math.sqrt(mCircleRadius * mCircleRadius + mCircleRadius * mCircleRadius) - mCircleRadius);
        mGap += mCircleRadius;
        mGap += mPaint.getStrokeWidth();

        mLeftTopPos[X] = mGap + paddingLeft + mCircleRadius;
        mRightTopPos[X] = width - mGap - paddingRight - mCircleRadius;
        mLeftBottomPos[X] = mLeftTopPos[X];
        mRightBottomPos[X] = mRightTopPos[X];

        mLeftTopPos[Y] = paddingTop + mGap + mCircleRadius;
        mLeftBottomPos[Y] = height - mGap - paddingBottom - mCircleRadius;
        mRightTopPos[Y] = mLeftTopPos[Y];
        mRightBottomPos[Y] = mLeftBottomPos[Y];

    }

    /**
     * measure width
     * @param measureSpec spec
     * @return width
     */
    private int measureWidthSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_WIDTH);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * measure height
     * @param measureSpec spec
     * @return height
     */
    private int measureHeightSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_HEIGHT);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * dp to px
     * @param dpValue dp
     * @return px
     */
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
