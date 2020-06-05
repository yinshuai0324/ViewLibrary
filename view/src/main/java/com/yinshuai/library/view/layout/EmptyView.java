package com.yinshuai.library.view.layout;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yinshuai.library.view.R;


/**
 * 描述:
 * 创建时间：2019-05-30-16:22
 *
 * @author: yinshuai
 */
public class EmptyView extends LinearLayout {
    private Context mContext;

    private TextView tips;
    private ImageView img;


    public EmptyView(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    public EmptyView(Context context, String tips, int res) {
        super(context);
        mContext = context;
        initView();
        setTips(tips);
        setImg(res);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_empty, this);
        img = findViewById(R.id.img);
        tips = findViewById(R.id.tips);
    }

    public void setImg(int res) {
        if (res != 0) {
            img.setImageResource(res);
        }
    }


    public void setTips(String tips) {
        if (!TextUtils.isEmpty(tips)) {
            this.tips.setText(tips);
        }
    }
}
