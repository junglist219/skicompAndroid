package de.skicomp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import de.skicomp.R;
import de.skicomp.models.SkiArea;

/**
 * Created by benjamin.schneider on 19.06.17.
 */

public class SlopeCircle extends View {

    private Paint mCircleEasy;
    private Paint mCircleModerate;
    private Paint mCircleExpert;
    private Paint mCircleFreeride;

    private float mRadius;
    private RectF mArcBounds = new RectF();

    private float easy;
    private float moderate;
    private float expert;
    private float freeride;

    public SlopeCircle(Context context) {
        super(context);
        initPaints();
    }

    public SlopeCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public SlopeCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mCircleEasy = initPaintWithColor(R.color.slope_easy);
        mCircleModerate = initPaintWithColor(R.color.slope_moderate);
        mCircleExpert = initPaintWithColor(R.color.slope_expert);
        mCircleFreeride = initPaintWithColor(R.color.slope_freeride);
    }

    private Paint initPaintWithColor(int colorResID) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), colorResID));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        paint.setStrokeCap(Paint.Cap.BUTT);
        return paint;
    }

    public void setSkiArea(SkiArea skiArea) {
        this.easy = skiArea.getSlopesEasy();
        this.moderate = skiArea.getSlopesModerate();
        this.expert = skiArea.getSlopesExpert();
        this.freeride = skiArea.getSlopesFreeride();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRadius = Math.min(w, h) / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float inset = mRadius / 4f;
        mArcBounds.set(inset, inset, mRadius * 2 - inset, mRadius * 2 - inset);

        float slopeLength = easy + moderate + expert + freeride;

        // easy
        float easyStart = 0;
        float easyLength = (easy / slopeLength) * 360.0f;

        // moderate
        float moderateStart = easyStart + easyLength;
        float moderateLength = (moderate / slopeLength) * 360.0f;

        // expert
        float expertStart = moderateStart + moderateLength;
        float expertLength = (expert / slopeLength) * 360.0f;

        // freeride
        float freerideStart = expertStart + expertLength;
        float freerideLength = (freeride / slopeLength) * 360.0f;

        canvas.drawArc(mArcBounds, easyStart, easyLength, false, mCircleEasy);
        canvas.drawArc(mArcBounds, moderateStart, moderateLength, false, mCircleModerate);
        canvas.drawArc(mArcBounds, expertStart, expertLength, false, mCircleExpert);
        canvas.drawArc(mArcBounds, freerideStart, freerideLength, false, mCircleFreeride);
    }

}
