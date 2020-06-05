package com.yinshuai.library.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.yinshuai.library.view.R;

/**
 * Created by Jango on 2015/5/20.
 */
public class SquareLayout extends ShadowLayout {

    //高度=宽度*w2h
    private float w2h = 0;//宽度固定  高度根据宽度调整 宽和高的比值
    //宽度=高度*h2w
    private float h2w = 0;//高度固定 宽度根据高度调整
    private float percentW = 0;
    private float percentH = 0;

    private int maxW = 0;
    private int maxH = 0;


    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout);
        w2h = a.getFloat(R.styleable.SquareLayout_widthToHeight, 0);
        h2w = a.getFloat(R.styleable.SquareLayout_heightToWidth, 0);
        percentW = a.getFloat(R.styleable.SquareLayout_percentW, 0);
        percentH = a.getFloat(R.styleable.SquareLayout_percentH, 0);

        maxW = (int) a.getDimension(R.styleable.SquareLayout_maxW, maxW);
        maxH = (int) a.getDimension(R.styleable.SquareLayout_maxH, maxH);

    }


    public SquareLayout(Context context) {
        this(context, null);
    }

    public void setW2H(float f) {
        w2h = f;
        invalidate();
    }

    public void setH2W(float f) {
        h2w = f;
        invalidate();
    }


    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (w2h > 0 || h2w > 0 || percentW > 0 || percentH > 0) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            int childHeightSize = getMeasuredHeight();

            if (w2h > 0) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * w2h), MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            if (h2w > 0) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize * h2w), MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            if (percentW > 0 || percentH > 0) {
                if (percentH > 0) {
                    int h = (int) (childHeightSize * percentH);
                    if (maxH > 0) {
                        if (h > maxH)
                            h = maxH;
                    }
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
                if (percentW > 0) {
                    int w = (int) (childWidthSize * percentW);
                    if (maxW > 0) {
                        if (w > maxW)
                            w = maxW;
                    }
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

