package com.yinshuai.library.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.yinshuai.library.view.R;


/**
 * @author yinshuai
 * @date 16-1-16
 */
public class SquareImageView extends AppCompatImageView {

    //高度=宽度*w2h
    private float w2h = 0;//宽度固定  高度根据宽度调整 宽和高的比值
    //宽度=高度*h2w
    private float h2w = 0;//高度固定 宽度根据高度调整
    private float percentW = 0;
    private float percentH = 0;


    private int maxW = 0;
    private int maxH = 0;

    private boolean reMeasure = false;
    private boolean flowVertical = false;

    public void setFlowVertical(boolean flowVertical) {
        this.flowVertical = flowVertical;
    }

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
        w2h = a.getFloat(R.styleable.SquareImageView_widthToHeight, 0);
        h2w = a.getFloat(R.styleable.SquareImageView_heightToWidth, 0);
        percentW = a.getFloat(R.styleable.SquareImageView_percentW, 0);
        percentH = a.getFloat(R.styleable.SquareImageView_percentH, 0);

        flowVertical = a.getBoolean(R.styleable.SquareImageView_flowVeritcal, false);
        reMeasure = a.getBoolean(R.styleable.SquareImageView_reMeasure, false);

        maxW = (int) a.getDimension(R.styleable.SquareImageView_maxW, maxW);
        maxH = (int) a.getDimension(R.styleable.SquareImageView_maxH, maxH);
    }

    public void setW2H(float f) {
        w2h = f;
        invalidate();
    }

    public void setH2W(float f) {
        h2w = f;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //垂直流
        if (flowVertical) {
            if (reMeasure || w2h <= 0) {
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    Rect rect = getDrawable().getBounds();
                    float w = rect.width();
                    float h = rect.height();

                    if (w > 0 && h > 0) {
                        w2h = h / w;
                    } else {
                        w = drawable.getIntrinsicWidth();
                        h = drawable.getIntrinsicHeight();
                        if (w > 0 && h > 0) {
                            w2h = h / w;
                        }
                    }
                }
            }
        }

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
                        if (h > maxH) {
                            h = maxH;
                        }
                    }
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
                if (percentW > 0) {
                    int w = (int) (childWidthSize * percentW);
                    if (maxW > 0) {
                        if (w > maxW) {
                            w = maxW;
                        }
                    }
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
