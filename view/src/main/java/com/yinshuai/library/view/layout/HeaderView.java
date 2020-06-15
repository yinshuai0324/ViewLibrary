package com.yinshuai.library.view.layout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yinshuai.library.view.R;


/**
 * @Description:
 * @Author: 尹帅
 * @CreateDate: 2020-05-22 17:10
 * @UpdateDate: 2020-05-22 17:10
 */
public class HeaderView extends LinearLayout {
    private Context mContext;
    private TextView titleView;
    private View lineView;

    public HeaderView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    @SuppressLint("ResourceAsColor")
    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        setTitle(array.getString(R.styleable.HeaderView_title));
        setLineColor(array.getColor(R.styleable.HeaderView_lineColor, R.color.colorMain_FFE9CE));
        boolean isShowLine = array.getBoolean(R.styleable.HeaderView_isShowLine, true);
        lineView.setVisibility(isShowLine ? VISIBLE : INVISIBLE);
    }

    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_header, this);
        titleView = findViewById(R.id.title);
        lineView = findViewById(R.id.line);
    }


    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
    }

    public void setLineColor(int color) {
        lineView.setBackgroundColor(mContext.getResources().getColor(color));
    }


}