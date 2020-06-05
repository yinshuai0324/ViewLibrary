package com.yinshuai.library.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.yinshuai.library.view.R;
import com.yinshuai.library.view.cache.ShadowCache;

import java.lang.ref.SoftReference;


/**
 * Created by Jango on 2015/6/2.
 */
public class ShadowLayout extends RelativeLayout {

    private Context mContex = null;
    private String shadowName = "";
    private float shadowPadding = 0;
    private int shadowColor = 0x30000000;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SoftReference<Bitmap> shadowBitmap = null;

    public ShadowLayout(Context context) {
        super(context);
        mContex = context;
        iniView(context);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContex = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        shadowName = a.getString(R.styleable.ShadowLayout_shadowName);
        shadowColor = a.getColor(R.styleable.ShadowLayout_shadowColor, shadowColor);
        shadowPadding = a.getDimension(R.styleable.ShadowLayout_shadowPadding, 0);

        iniView(context);
    }

    private void iniView(Context context) {
//        if(Build.VERSION.SDK_INT>=11)
//            setLayerType(LAYER_TYPE_SOFTWARE,null);
        if (Build.VERSION.SDK_INT >= 11)
            setLayerType(LAYER_TYPE_HARDWARE, null);

        if (TextUtils.isEmpty(shadowName)) {
            shadowName = context.getClass().getSimpleName();
        }
    }

    public float getShadowPadding() {
        return shadowPadding;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if ((changed || shadowBitmap == null) && shadowPadding > 0) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();

            String shadowCacheName = shadowName + w + "_" + h;

            shadowBitmap = ShadowCache.cacheMap.get(shadowCacheName);

            if (shadowBitmap == null || shadowBitmap.get() == null) {
                Bitmap tempShadowBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);
                Canvas shadowCanvas = new Canvas(tempShadowBitmap);

                mPaint.setColor(shadowColor);
                mPaint.setAntiAlias(true);
                mPaint.setDither(true);
                // 设置画笔遮罩滤镜,传入度数和样式
//                mPaint.setMaskFilter(new BlurMaskFilter(shadowPadding, BlurMaskFilter.Blur.OUTER));
                mPaint.setShadowLayer(shadowPadding / 2, 0, shadowPadding / 2, shadowColor);

                Bitmap tempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);
                Canvas tempCanvas = new Canvas(tempBitmap);
                dispatchDraw(tempCanvas);
                shadowCanvas.drawBitmap(tempBitmap, 0, 0, mPaint);
                tempBitmap.recycle();


                shadowBitmap = new SoftReference<>(tempShadowBitmap);
                if (!TextUtils.isEmpty(shadowName)) {
                    ShadowCache.cacheMap.put(shadowCacheName, shadowBitmap);
                }
            }

            if (shadowBitmap != null && shadowBitmap.get() != null) {
                setBackgroundDrawable(new BitmapDrawable(shadowBitmap.get()));
            }
        }
    }


    public void freeShadow() {
        if (shadowBitmap != null && shadowBitmap.get() != null) {
            shadowBitmap.get().recycle();
            shadowBitmap.clear();
            System.gc();
        }
    }
}
