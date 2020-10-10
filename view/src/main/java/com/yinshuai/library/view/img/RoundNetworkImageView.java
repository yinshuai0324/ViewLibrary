package com.yinshuai.library.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.yinshuai.library.view.R;


/**
 * @author yinshuai
 * @date 2015/7/27
 */
public class RoundNetworkImageView extends NetImageView {
    //0x0f 是同时四个角
    public static final int CORNER_TYPE_NONE = 0x00;
    public static final int CORNER_TYPE_LEFT_TOP = 0x01;
    public static final int CORNER_TYPE_RIGHT_TOP = 0x02;
    public static final int CORNER_TYPE_RIGHT_BOTTOM = 0x04;
    public static final int CORNER_TYPE_LEFT_BOTTOM = 0x08;
    public static final int CORNER_TYPE_ALL = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_LEFT = CORNER_TYPE_LEFT_TOP | CORNER_TYPE_LEFT_BOTTOM;
    public static final int CORNER_TYPE_RIGHT = CORNER_TYPE_RIGHT_TOP | CORNER_TYPE_RIGHT_BOTTOM;

    Path roundpath;

    Path roundpathForBorder;

    boolean needBuffLayer = true;

    int cornerType = CORNER_TYPE_NONE;
    int cornerRadius = 0;

    Paint borderPaint = null;
    int borderColor = 0;
    int borderWidth = 4;
    int roundPadding = 0;

    Paint borderClearPaint = null;


    Paint cutOuterBorderPaint = null;


    int bodyColor = 0;

    float[] corner1 = new float[]{0, 0};
    float[] corner2 = new float[]{0, 0};
    float[] corner3 = new float[]{0, 0};
    float[] corner4 = new float[]{0, 0};

    public RoundNetworkImageView(Context context) {
        super(context);
        iniView();
    }

    public RoundNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundNetworkImageView);
        bodyColor = a.getColor(R.styleable.RoundNetworkImageView_bodyColor, bodyColor);
        borderColor = a.getColor(R.styleable.RoundNetworkImageView_borderColor, 0xffffffff);
        borderWidth = (int) a.getDimension(R.styleable.RoundNetworkImageView_borderWidth, borderWidth);
        roundPadding = (int) a.getDimension(R.styleable.RoundNetworkImageView_roundPadding, roundPadding);
        cornerType = a.getInt(R.styleable.RoundNetworkImageView_cornerType, CORNER_TYPE_NONE);
        cornerRadius = (int) a.getDimension(R.styleable.RoundNetworkImageView_radius, 0);

        setCorner(cornerType, cornerRadius);
        iniView();
    }

    private void iniView() {

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);

        borderClearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (roundPadding > 0) {
            borderClearPaint.setStrokeWidth(roundPadding);
        } else {
            borderClearPaint.setStrokeWidth(0);
        }
        borderClearPaint.setStyle(Paint.Style.STROKE);
        borderClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        cutOuterBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cutOuterBorderPaint.setStyle(Paint.Style.FILL);
        cutOuterBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = this.getWidth();
        float h = this.getHeight();

        if (roundpath == null) {
            float[] corners = new float[8];
            corners[0] = corner1[0];
            corners[1] = corner1[1];
            corners[2] = corner2[0];
            corners[3] = corner2[1];
            corners[4] = corner3[0];
            corners[5] = corner3[1];
            corners[6] = corner4[0];
            corners[7] = corner4[1];

            float pathW = w - 2;
            float pathH = h - 2;
            int borderPadd = 0;

            roundpath = new Path();

            roundpath.arcTo(new RectF(-borderPadd, -borderPadd, corners[0] * 2, corners[1] * 2), 180, 90);

            roundpath.arcTo(new RectF(pathW - corners[2] * 2, -borderPadd, pathW + borderPadd, corners[3] * 2), -90, 90);

            roundpath.arcTo(new RectF(pathW - corners[4] * 2, pathH - corners[5] * 2, pathW + borderPadd, pathH + borderPadd), 0, 90);

            roundpath.arcTo(new RectF(-borderPadd, pathH - corners[7] * 2, corners[6] * 2, pathH + borderPadd), 90, 90);


//            roundpath.arcTo(new RectF(-borderPadd - 1, -borderPadd - 1, corners[0] * 2, corners[1] * 2), 180, 90);
//
//            roundpath.arcTo(new RectF(pathW - corners[2] * 2, -borderPadd - 1, pathW + borderPadd + 1, corners[3] * 2), -90, 90);
//
//            roundpath.arcTo(new RectF(pathW - corners[4] * 2, pathH - corners[5] * 2, pathW + borderPadd + 1, pathH + borderPadd + 1), 0, 90);
//
//            roundpath.arcTo(new RectF(-borderPadd - 1, pathH - corners[7] * 2 + 1, corners[6] * 2, pathH + borderPadd + 1), 90, 90);

            roundpath.close();

            roundpathForBorder = new Path();
            roundpathForBorder.addPath(roundpath);
            roundpathForBorder.setFillType(Path.FillType.WINDING);

            roundpath.setFillType(Path.FillType.INVERSE_WINDING);

        }


        if (needBuffLayer) {
            canvas.saveLayer(0, 0, w, h, null, Canvas.ALL_SAVE_FLAG);
        }
        if (bodyColor != 0) {
            canvas.drawColor(bodyColor);
        }
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(1, 1);

        if (borderWidth > 0 && borderColor != 0) {
            canvas.drawPath(roundpathForBorder, borderClearPaint);
            canvas.drawPath(roundpathForBorder, borderPaint);
        }

        canvas.drawPath(roundpath, cutOuterBorderPaint);

        if (needBuffLayer) {
            canvas.restore();
        }
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

    public void setCornerRadius() {

    }

    public void setBorderWidth(int width) {
        borderWidth = width;
        borderPaint.setStrokeWidth(borderWidth);

        if (borderWidth - 1 > 0) {
            borderClearPaint.setStrokeWidth(borderWidth - 1);
        } else {
            borderClearPaint.setStrokeWidth(0);
        }

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
        invalidate();
    }

    public void setCorner(int cornerType, int cornerRadius) {
        setCorner((0xff & (cornerType & CORNER_TYPE_LEFT_TOP)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_RIGHT_TOP)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_RIGHT_BOTTOM)) > 0 ? cornerRadius : 0,
                (0xff & (cornerType & CORNER_TYPE_LEFT_BOTTOM)) > 0 ? cornerRadius : 0);
    }
}
