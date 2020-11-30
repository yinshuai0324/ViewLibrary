package com.yinshuai.library.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yinshuai.library.view.R;


/**
 * @author yinshuai
 * @date 16-3-4
 */
public class NetImageView extends SquareImageView {

    private int defaultResId = 0;
    private int errorResId = 0;
    private int loadingResId = 0;
    private String imageUrl = "";

    private OnImageLoadListener loadListener;


    public NetImageView(Context context) {
        this(context, null);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NetImageView);
        errorResId = a.getResourceId(R.styleable.NetImageView_errorRes, R.drawable.ic_default);
        loadingResId = a.getResourceId(R.styleable.NetImageView_loadingRes, R.drawable.ic_default);
        defaultResId = a.getResourceId(R.styleable.NetImageView_defaultRes, R.drawable.ic_default);
        setDefaultSrc(defaultResId);
        setUrl(a.getString(R.styleable.NetImageView_url));
    }


    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setImageResource(defaultResId);
        } else {
            imageUrl = url;
            Glide.with(this)
                    .load(imageUrl)
                    .error(errorResId)
                    .override(this.getWidth(), this.getHeight())
                    .placeholder(defaultResId)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (loadListener != null) {
                                loadListener.loadFailed(imageUrl, e);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (loadListener != null) {
                                loadListener.loadSucceed(imageUrl, resource);
                            }
                            return false;
                        }
                    }).into(this);
        }
    }


    public void setDefaultResId(int resId) {
        defaultResId = resId;
    }

    public void setDefaultSrc(int resId) {
        defaultResId = resId;
        setImageResource(resId);
    }

    public String getUrl() {
        return imageUrl;
    }


    public void setOnImageLoadListener(OnImageLoadListener listener) {
        this.loadListener = listener;
    }

    public interface OnImageLoadListener {
        void loadSucceed(String url, Drawable resource);

        void loadFailed(String url, Exception exception);
    }

}
