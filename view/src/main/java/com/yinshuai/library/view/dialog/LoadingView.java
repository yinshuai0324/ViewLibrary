package com.yinshuai.library.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinshuai.library.view.R;


public class LoadingView {
    private Context mContext;
    private String msg;

    private ImageView iv_img;
    private TextView tv_msg;

    private Dialog dialog;

    private Animation rotateAnimation;


    private boolean isOutside = false;
    private boolean isBack = false;

    public LoadingView(Context context) {
        mContext = context;
        createLoadingView();
    }

    public LoadingView(Context context, String msg) {
        mContext = context;
        this.msg = msg;
        createLoadingView();
    }

    public LoadingView(Context context, String msg, boolean outside) {
        mContext = context;
        this.msg = msg;
        this.isOutside = outside;
        createLoadingView();
    }

    public LoadingView(Context context, String msg, boolean outside, boolean back) {
        mContext = context;
        this.msg = msg;
        this.isOutside = outside;
        this.isBack = back;
        createLoadingView();
    }

    public void createLoadingView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_loading, null);
        iv_img = (ImageView) view.findViewById(R.id.loading_image);
        tv_msg = (TextView) view.findViewById(R.id.loading_msg);

        tv_msg.setText(msg);

        dialog = new Dialog(mContext, R.style.loading);
        // 是否可以按“返回键”消失
        dialog.setCancelable(isBack);
        // 点击加载框以外的区域
        dialog.setCanceledOnTouchOutside(isOutside);
        // 设置布局
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading);
    }

    public Dialog show() {
        iv_img.startAnimation(rotateAnimation);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public LoadingView setMsg(String msg) {
        tv_msg.setText(msg);
        return this;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public boolean isShow() {
        if (dialog != null) {
            return dialog.isShowing();
        }

        return true;
    }
}
