package com.yinshuai.library.view.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.yinshuai.library.view.R;


/**
 * @author yinshuai
 * @date 2015/5/11
 */
public class RoundButton extends AppCompatTextView implements View.OnTouchListener {

    //   Paint mPaint=null;
    public static final int CORNER_TYPE_NONE = 0x00;
    public static final int CORNER_TYPE_LEFT_TOP = 0x01;
    public static final int CORNER_TYPE_RIGHT_TOP = 0x02;
    public static final int CORNER_TYPE_RIGHT_BOTTOM = 0x04;
    public static final int CORNER_TYPE_LEFT_BOTTOM = 0x08;
    public static final int CORNER_TYPE_ALL = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_LEFT = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_RIGHT = CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM;


    float w;
    float h;

    //高度=宽度*w2h

    private float w2h = 0;//宽度固定  高度根据宽度调整 宽和高的比值
    //宽度=高度*h2w
    private float h2w = 0;//高度固定 宽度根据高度调整
    private float percentW = 0;
    private float percentH = 0;
    boolean isMeasuredW = false;
    boolean isMeasuredH = false;

    int cornerType = CORNER_TYPE_NONE;
    int cornerRadius = 0;

    boolean isRound = false;

    boolean forceTouchEffectUp = true;

    boolean disableTouchEffect = false;

    boolean isSelect = false;


    Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float borderWidth = 4;

    private boolean iniViewFinish = false;

    boolean isDisable = false;

    int disableTColor = 0;
    int disableBColor = 0;
    int disableBorderColor = 0;

    int pressTColor = 0;
    int pressBColor = 0;
    int pressBorderColor = 0;

    int selectTColor = 0;
    int selectBColor = 0;
    int selectBorderColor = 0;

    int unSelectTColor = 0;
    int unSelectBColor = 0;
    int unSelectBorderColor = 0;

    boolean enableToggle = false;


    float[] corner1 = new float[]{0, 0};
    float[] corner2 = new float[]{0, 0};
    float[] corner3 = new float[]{0, 0};
    float[] corner4 = new float[]{0, 0};

    private Path roundpath;

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);
        unSelectBColor = a.getColor(R.styleable.RoundButton_bodyColor, 0);
        unSelectBorderColor = a.getColor(R.styleable.RoundButton_borderColor, 0xffffffff);
        borderWidth = (int) a.getDimension(R.styleable.RoundButton_borderWidth, 2);
        unSelectTColor = this.getCurrentTextColor();


        cornerType = a.getInt(R.styleable.RoundButton_cornerType, CORNER_TYPE_NONE);
        cornerRadius = (int) a.getDimension(R.styleable.RoundButton_radius, 0);

        setCorner();

        pressTColor = a.getColor(R.styleable.RoundButton_pressTextColor, colorDarker(unSelectTColor, 0.8f));
        pressBColor = a.getColor(R.styleable.RoundButton_pressBodyColor, colorDarker(unSelectBColor, 0.8f));
        pressBorderColor = a.getColor(R.styleable.RoundButton_pressBorderColor, colorDarker(unSelectBorderColor, 0.8f));
        forceTouchEffectUp = a.getBoolean(R.styleable.RoundButton_forceTouchEffectUp, true);


        disableTouchEffect = a.getBoolean(R.styleable.RoundButton_disableTouchEffect, false);

        selectTColor = a.getColor(R.styleable.RoundButton_textSelectColor, unSelectBColor);
        selectBColor = a.getColor(R.styleable.RoundButton_bodySelectColor, unSelectTColor);
        selectBorderColor = a.getColor(R.styleable.RoundButton_borderSelectColor, unSelectBorderColor);


        disableTColor = a.getColor(R.styleable.RoundButton_disableTextColor, unSelectTColor);
        disableBColor = a.getColor(R.styleable.RoundButton_disableBodyColor, unSelectBColor);
        disableBorderColor = a.getColor(R.styleable.RoundButton_disableBorderColor, unSelectBorderColor);

        enableToggle = a.getBoolean(R.styleable.RoundButton_enableToggle, enableToggle);

        setSelect(a.getBoolean(R.styleable.RoundButton_setSelect, false));

        isRound = a.getBoolean(R.styleable.RoundButton_isRound, false);

        w2h = a.getFloat(R.styleable.RoundButton_widthToHeight, 0);
        h2w = a.getFloat(R.styleable.RoundButton_heightToWidth, 0);
        percentW = a.getFloat(R.styleable.RoundButton_percentW, 0);
        percentH = a.getFloat(R.styleable.RoundButton_percentH, 0);


        iniView(context);
    }

    public Path getPath() {
        return roundpath;
    }

    public static int colorDarker(int color, float persent) {
        int r = (int) ((color >> 16 & 0xff) * persent);
        int g = (int) ((color >> 8 & 0xff) * persent);
        int b = (int) ((color & 0xff) * persent);
        return 0xff << 24 | r << 16 | g << 8 | b;
    }

    void setCorner() {
        setCorner((cornerType & CORNER_TYPE_LEFT_TOP) == CORNER_TYPE_LEFT_TOP ? cornerRadius : 0,
                (cornerType & CORNER_TYPE_RIGHT_TOP) == CORNER_TYPE_RIGHT_TOP ? cornerRadius : 0,
                (cornerType & CORNER_TYPE_RIGHT_BOTTOM) == CORNER_TYPE_RIGHT_BOTTOM ? cornerRadius : 0,
                (cornerType & CORNER_TYPE_LEFT_BOTTOM) == CORNER_TYPE_LEFT_BOTTOM ? cornerRadius : 0);
    }

    public void setCornerType(int type) {
        cornerType = type;
        setCorner();

    }

    void iniView(Context context) {
        setGravity(Gravity.CENTER);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        borderPaint.setStrokeWidth(borderWidth);

        borderPaint.setStyle(Paint.Style.STROKE);


        setOnTouchListener(this);

        iniViewFinish = true;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            createRoundPath();

            isMeasuredW = false;
            isMeasuredH = false;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

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
                    if (!isMeasuredH) {
                        isMeasuredH = true;
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize * percentH), MeasureSpec.EXACTLY);
                    }
                }
                if (percentW > 0) {
                    if (!isMeasuredW) {
                        isMeasuredW = true;
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * percentW), MeasureSpec.EXACTLY);
                    }

                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isRound) {
            float halfW = getMeasuredWidth() / 2f;
            float halfH = getMeasuredHeight() / 2f;
            cornerRadius = (int) Math.min(halfW, halfH);
            setCorner();
        }
    }

    public void setIsRound(boolean b) {
        isRound = b;
    }

    public void createRoundPath() {
        w = this.getMeasuredWidth();
        h = this.getMeasuredHeight();

        /************************************************************************roundpath*/
        float[] corners = new float[8];
        float offset = borderWidth / 2 + 1;
        corners[0] = corner1[0] - offset;
        corners[0] = Math.max(0, corners[0]);
        corners[1] = corner1[1] - offset;
        corners[1] = Math.max(0, corners[1]);
        corners[2] = corner2[0] - offset;
        corners[2] = Math.max(0, corners[2]);
        corners[3] = corner2[1] - offset;
        corners[3] = Math.max(0, corners[3]);
        corners[4] = corner3[0] - offset;
        corners[4] = Math.max(0, corners[4]);
        corners[5] = corner3[1] - offset;
        corners[5] = Math.max(0, corners[5]);
        corners[6] = corner4[0] - offset;
        corners[6] = Math.max(0, corners[6]);
        corners[7] = corner4[1] - offset;
        corners[7] = Math.max(0, corners[7]);

        float pathW = w - borderWidth - 2;
        float pathH = h - borderWidth - 2;
        float borderPadd = 0;

        roundpath = new Path();

        roundpath.arcTo(new RectF(borderPadd, borderPadd, corners[0] * 2 + borderPadd, corners[1] * 2 + borderPadd), 180, 90);

        roundpath.arcTo(new RectF(pathW - corners[2] * 2 + borderPadd, borderPadd, pathW + borderPadd, corners[3] * 2 + borderPadd), -90, 90);

        roundpath.arcTo(new RectF(pathW - corners[4] * 2 + borderPadd, pathH - corners[5] * 2 + borderPadd, pathW + borderPadd, pathH + borderPadd), 0, 90);

        roundpath.arcTo(new RectF(borderPadd, pathH - corners[7] * 2 + borderPadd, corners[6] * 2 + borderPadd, pathH + borderPadd), 90, 90);

        roundpath.close();

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(borderWidth / 2 + 1, borderWidth / 2 + 1);
        if (bodyPaint.getColor() != 0) {
            canvas.drawPath(roundpath, bodyPaint);
        }
        if (borderWidth > 0) {
            canvas.drawPath(roundpath, borderPaint);
        }
        canvas.restore();
        super.onDraw(canvas);
    }


    public void setTColor(int color) {
        unSelectTColor = color;
        pressTColor = colorDarker(unSelectTColor, 0.8f);
    }

    public void setBodyColor(int color) {
        unSelectBColor = color;
        pressBColor = colorDarker(unSelectBColor, 0.8f);
    }

    public void setBorderColor(int color) {
        unSelectBorderColor = color;
        pressBorderColor = colorDarker(unSelectBorderColor, 0.8f);
    }

    public void setBorderWidth(int width) {
        borderWidth = width;
        borderPaint.setStrokeWidth(borderWidth);
    }

    public void setSelectTColor(int color) {
        selectTColor = color;
    }

    public void setSelectBColor(int color) {
        selectBColor = color;
    }

    public void setSelectBorderColor(int color) {
        selectBorderColor = color;
    }

    public void setPressTColor(int color) {
        pressTColor = color;
    }

    public void setPressBColor(int color) {
        pressBColor = color;
    }

    public void setPressBorderColor(int color) {
        pressBorderColor = color;
    }


    private void drawDisable() {
        borderPaint.setColor(disableBorderColor);
        bodyPaint.setColor(disableBColor);
        setTextColor(disableTColor);
        invalidate();
    }

    private void drawSelect() {
        borderPaint.setColor(selectBorderColor);
        bodyPaint.setColor(selectBColor);
        setTextColor(selectTColor);
        invalidate();
    }

    public void drawDefault() {
        borderPaint.setColor(unSelectBorderColor);
        bodyPaint.setColor(unSelectBColor);
        setTextColor(unSelectTColor);
        invalidate();
    }

    private void drawPressed() {
        borderPaint.setColor(pressBorderColor);
        bodyPaint.setColor(pressBColor);
        setTextColor(pressTColor);
        invalidate();
    }


    public void setCorner(float corner_1, float corner_2, float corner_3, float corner_4) {
        corner1[0] = corner_1;
        corner1[1] = corner_1;
        corner2[0] = corner_2;
        corner2[1] = corner_2;
        corner3[0] = corner_3;
        corner3[1] = corner_3;
        corner4[0] = corner_4;
        corner4[1] = corner_4;

        createRoundPath();
        invalidate();
    }

    public void setCorner(float corner) {
        setCorner(corner, corner, corner, corner);
    }

    public void setSelect(boolean select) {
        isSelect = select;
        if (isSelect) {
            drawSelect();
        } else {
            drawDefault();
        }
    }

    public boolean getSelect() {
        return isSelect;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (iniViewFinish) {
            setDisable(!enabled);
        }
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
        if (isDisable) {
            drawDisable();
        } else {
            if (isSelect) {
                drawSelect();
            } else {
                drawDefault();
            }
        }
    }

    public void setDisableTouchEffect(boolean b) {
        disableTouchEffect = b;
    }


    public void setForceTouchEffectUp(boolean b) {
        forceTouchEffectUp = b;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isSelect && !enableToggle) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!disableTouchEffect) {
                    drawPressed();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                drawDefault();
                break;
            case MotionEvent.ACTION_UP:
                if (forceTouchEffectUp) {
                    drawDefault();
                }
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
                break;

        }
        return true;
    }


    OnClickListener onClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

}
