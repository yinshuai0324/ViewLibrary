package com.yinshuai.library.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;

import com.yinshuai.library.view.R;

public class RoundWaveLayout extends SquareLayout {
    public static final int CORNER_TYPE_NONE = 0x00;
    public static final int CORNER_TYPE_LEFT_TOP = 0x01;
    public static final int CORNER_TYPE_RIGHT_TOP = 0x02;
    public static final int CORNER_TYPE_RIGHT_BOTTOM = 0x04;
    public static final int CORNER_TYPE_LEFT_BOTTOM = 0x08;
    public static final int CORNER_TYPE_ALL = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_LEFT = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_RIGHT = CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM;

    public static final int WAVE_TYPE_NONE = 0x00;
    public static final int WAVE_TYPE_LEFT = 0x01;
    public static final int WAVE_TYPE_TOP = 0x02;
    public static final int WAVE_TYPE_RIGHT = 0x04;
    public static final int WAVE_TYPE_BOTTOM = 0x08;

    public static final int CUT_CORNER_TYPE_NONE = 0x00;
    public static final int CUT_CORNER_LEFT_TOP = 0x01;
    public static final int CUT_CORNER_RIGHT_TOP = 0x02;
    public static final int CUT_CORNER_RIGHT_BOTTOM = 0x04;
    public static final int CUT_CORNER_LEFT_BOTTOM = 0x08;


    int w = 0;
    int h = 0;

    private Paint cutOuterBorderPaint = null;

    private int bodyColor = 0;

    Paint borderPaint = null;
    Paint gradientPaint = null;
    int borderColor = 0;
    int borderWidth = 0;

    private boolean needSaveLayer = true;
    private boolean needCenterCircle = false;
    int centerCircleSize = 0;

    private float shadowPadding = 0;

    private Path roundpath;
    private Path roundpathForBorder;

    private int cornerType = CORNER_TYPE_NONE;
    private int cornerRadius = 0;

    private boolean isOpenGradient;
    private int startColor;
    private int endColor;

    private float[] corner1 = new float[]{0, 0};
    private float[] corner2 = new float[]{0, 0};
    private float[] corner3 = new float[]{0, 0};
    private float[] corner4 = new float[]{0, 0};


    private Path cutWavePath;

    int waveType = WAVE_TYPE_NONE;
    int cutCornerType = CUT_CORNER_TYPE_NONE;

    int cutCornerSize = 10;
    int cutCornerOffsetX = 0;
    int cutCornerOffsetY = 0;

    int waveSize = 10;
    int waveOffsetY = 0;
    int waveSpace = 0;
    int waveRadius = 1;

    public RoundWaveLayout(Context context) {
        this(context, null);
    }

    public RoundWaveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundWaveLayout);

        isOpenGradient = a.getBoolean(R.styleable.RoundWaveLayout_isOpenGradient, false);
        startColor = a.getColor(R.styleable.RoundWaveLayout_startColor, 0);
        endColor = a.getColor(R.styleable.RoundWaveLayout_endColor, 0);

        bodyColor = a.getColor(R.styleable.RoundWaveLayout_bodyColor, bodyColor);
        borderColor = a.getColor(R.styleable.RoundWaveLayout_borderColor, borderColor);
        borderWidth = (int) a.getDimension(R.styleable.RoundWaveLayout_borderWidth, borderWidth);

        cornerType = a.getInt(R.styleable.RoundWaveLayout_cornerType, CORNER_TYPE_NONE);
        cornerRadius = (int) a.getDimension(R.styleable.RoundWaveLayout_radius, cornerRadius);

        setCorner(cornerType, cornerRadius);

        waveSize = (int) a.getDimension(R.styleable.RoundWaveLayout_waveSize, 0);
        waveOffsetY = (int) a.getDimension(R.styleable.RoundWaveLayout_waveOffsetY, 0);
        waveSpace = (int) a.getDimension(R.styleable.RoundWaveLayout_waveSpace, 1);
        if (waveSpace < 1) waveSpace = 1;
        waveType = a.getInteger(R.styleable.RoundWaveLayout_waveType, WAVE_TYPE_NONE);

        cutCornerType = a.getInteger(R.styleable.RoundWaveLayout_cutCornerType, CUT_CORNER_TYPE_NONE);
        cutCornerSize = (int) a.getDimension(R.styleable.RoundWaveLayout_cutCornerSize, 0);
        cutCornerOffsetX = (int) a.getDimension(R.styleable.RoundWaveLayout_cutCornerOffsetX, 0);
        cutCornerOffsetY = (int) a.getDimension(R.styleable.RoundWaveLayout_cutCornerOffsetY, 0);

        if (Build.VERSION.SDK_INT >= 11)
            needSaveLayer = a.getBoolean(R.styleable.RoundWaveLayout_needSaveLayer, needSaveLayer);
        else
            needSaveLayer = true;

        needCenterCircle = a.getBoolean(R.styleable.RoundWaveLayout_needCenterCircle, needCenterCircle);
        centerCircleSize = (int) a.getDimension(R.styleable.RoundWaveLayout_centerCircleSize, 0);

        waveRadius = waveSize / 2;
        if (waveRadius < 1) waveRadius = 1;

        iniView(context);
    }

    public void setCorner(int cornerType, int cornerRadius) {
        setCorner((0xff & (cornerType & CORNER_TYPE_LEFT_TOP)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_RIGHT_TOP)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_RIGHT_BOTTOM)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_LEFT_BOTTOM)) > 0 ? cornerRadius : 0);
    }

    private void iniView(Context context) {

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);


        if (isOpenGradient) {
            gradientPaint = new Paint();
        }


        shadowPadding = getShadowPadding();

        cutOuterBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cutOuterBorderPaint.setStyle(Paint.Style.FILL);
        cutOuterBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            w = this.getMeasuredWidth();
            h = this.getMeasuredHeight();

            Path shadowPath = new Path();

            /************************************************************************roundpath*/
            float[] corners = new float[8];
            corners[0] = corner1[0];
            corners[1] = corner1[1];
            corners[2] = corner2[0];
            corners[3] = corner2[1];
            corners[4] = corner3[0];
            corners[5] = corner3[1];
            corners[6] = corner4[0];
            corners[7] = corner4[1];

            float pathW = w - (shadowPadding * 2);
            float pathH = h - (shadowPadding * 2);
            float borderPadd = shadowPadding;

            roundpath = new Path();

            roundpath.arcTo(new RectF(borderPadd, borderPadd, corners[0] * 2 + borderPadd, corners[1] * 2 + borderPadd), 180, 90);

            roundpath.arcTo(new RectF(pathW - corners[2] * 2 + borderPadd, borderPadd, pathW + borderPadd, corners[3] * 2 + borderPadd), -90, 90);

            roundpath.arcTo(new RectF(pathW - corners[4] * 2 + borderPadd, pathH - corners[5] * 2 + borderPadd, pathW + borderPadd, pathH + borderPadd), 0, 90);

            roundpath.arcTo(new RectF(borderPadd, pathH - corners[7] * 2 + borderPadd, corners[6] * 2 + borderPadd, pathH + borderPadd), 90, 90);

            roundpath.close();

            roundpathForBorder = new Path();
            roundpathForBorder.addPath(roundpath);
            roundpathForBorder.setFillType(Path.FillType.WINDING);

            shadowPath.addPath(roundpath);


            roundpath.setFillType(Path.FillType.EVEN_ODD);
            roundpath.addRect(0, 0, w, h, Path.Direction.CW);

            /*******************************************************************cutPath*/
            cutWavePath = new Path();

            if (((waveType & WAVE_TYPE_LEFT) & 0xff) > 0) {
                for (int i = 0; i < h + waveRadius; i += waveSize + waveSpace) {
                    cutWavePath.addCircle(0 - waveOffsetY, i, waveRadius, Path.Direction.CW);
                }
            }
            if (((waveType & WAVE_TYPE_TOP) & 0xff) > 0) {
                for (int i = 0; i < w + waveRadius; i += waveSize + waveSpace) {
                    cutWavePath.addCircle(i, 0 - waveOffsetY, waveRadius, Path.Direction.CW);
                }
            }
            if (((waveType & WAVE_TYPE_RIGHT) & 0xff) > 0) {
                for (int i = 0; i < h + waveRadius; i += waveSize + waveSpace) {
                    cutWavePath.addCircle(w + waveOffsetY, i, waveRadius, Path.Direction.CW);
                }
            }
            if (((waveType & WAVE_TYPE_BOTTOM) & 0xff) > 0) {
                for (int i = 0; i < w + waveRadius; i += waveSize + waveSpace) {
                    cutWavePath.addCircle(i, h + waveOffsetY, waveRadius, Path.Direction.CW);
                }
            }

            if (((cutCornerType & CUT_CORNER_LEFT_TOP) & 0xff) > 0) {
                cutWavePath.addCircle(0 - cutCornerOffsetX, 0 - cutCornerOffsetY, cutCornerSize, Path.Direction.CW);
            }
            if (((cutCornerType & CUT_CORNER_RIGHT_TOP) & 0xff) > 0) {
                cutWavePath.addCircle(w + cutCornerOffsetX, 0 - cutCornerOffsetY, cutCornerSize, Path.Direction.CW);
            }
            if (((cutCornerType & CUT_CORNER_RIGHT_BOTTOM) & 0xff) > 0) {
                cutWavePath.addCircle(w + cutCornerOffsetX, h + cutCornerOffsetY, cutCornerSize, Path.Direction.CW);
            }
            if (((cutCornerType & CUT_CORNER_LEFT_BOTTOM) & 0xff) > 0) {
                cutWavePath.addCircle(0 - cutCornerOffsetX, h + cutCornerOffsetY, cutCornerSize, Path.Direction.CW);
            }

            if (needCenterCircle) {
                cutWavePath.addCircle(w / 2, 0, centerCircleSize, Path.Direction.CW);
            }
        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (canvas == null) {
            super.dispatchDraw(null);
            return;
        }

        if (needSaveLayer) {
//            canvas.saveLayerAlpha(0, 0, w, h, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
            canvas.saveLayerAlpha(0, 0, w, h, 255, Canvas.ALL_SAVE_FLAG);
        }

        if (bodyColor != 0) {
            canvas.drawColor(bodyColor);
        }

        if (isOpenGradient) {
            drawGradient(canvas);
        }

        super.dispatchDraw(canvas);

        if (borderWidth > 0) {
            if (roundpathForBorder != null && !roundpathForBorder.isEmpty())
                canvas.drawPath(roundpathForBorder, borderPaint);
        }

        if (cornerRadius > 0 && roundpath != null && !roundpath.isEmpty())
            canvas.drawPath(roundpath, cutOuterBorderPaint);

        if (cutWavePath != null && !cutWavePath.isEmpty())
            canvas.drawPath(cutWavePath, cutOuterBorderPaint);


        if (needSaveLayer) {
            canvas.restore();
        }
    }


    private void drawGradient(Canvas canvas) {
        RectF rectF = new RectF(0, 0, getRight(), getBottom());
        LinearGradient linearGradient = new LinearGradient(
                0, 0,
                rectF.right, rectF.top,
                startColor, endColor,
                Shader.TileMode.MIRROR
        );
        gradientPaint.setShader(linearGradient);
        canvas.drawRect(rectF, gradientPaint);
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

        invalidate();
    }

    public void setBodyColor(int color) {
        bodyColor = color;
        invalidate();
    }

    public void setBorderColor(int color) {
        borderColor = color;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public void setBorderWidth(int width) {
        borderWidth = width;
        borderPaint.setStrokeWidth(borderWidth);

        invalidate();
    }


    public void setGradient(int startColor, int endColor) {
        this.isOpenGradient = true;
        this.startColor = startColor;
        this.endColor = endColor;
        invalidate();
    }

}
