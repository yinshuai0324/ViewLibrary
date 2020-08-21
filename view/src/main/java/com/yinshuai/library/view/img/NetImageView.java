package com.yinshuai.library.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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

    public NetImageView(Context context) {
        this(context, null);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NetImageView);
        errorResId = a.getResourceId(R.styleable.NetImageView_errorRes, R.drawable.ic_loading_error);
        loadingResId = a.getResourceId(R.styleable.NetImageView_loadingRes, R.drawable.ic_loading);
        defaultResId = a.getResourceId(R.styleable.NetImageView_defaultRes, R.drawable.ic_default);
        setDefaultSrc(defaultResId);
        setUrl(a.getString(R.styleable.NetImageView_url));
    }


    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setImageResource(defaultResId);
        } else {
            imageUrl = url;
            Glide.with(this).load(imageUrl).error(errorResId).placeholder(loadingResId).transition(DrawableTransitionOptions.withCrossFade()).into(this);
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

}
