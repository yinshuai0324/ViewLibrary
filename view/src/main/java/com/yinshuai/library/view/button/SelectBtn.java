package com.yinshuai.library.view.button;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

import com.yinshuai.library.view.R;

/**
 * 描述:
 * 创建时间：2019/3/26-5:55 PM
 *
 * @author: yinshuai
 */
public class SelectBtn extends AppCompatImageView implements SelectItem {

    Drawable selectImg = null;
    Drawable noSelectImg = null;
    Drawable cantSelectImg = null;
    int selectColor = 0;
    int noSelectColor = 0;
    boolean isSelect = false;
    boolean playAnimator = false;

    public SelectBtn(Context context) {
        super(context);
    }

    public SelectBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectBtn);
        selectImg = a.getDrawable(R.styleable.SelectBtn_selectImg);
        noSelectImg = a.getDrawable(R.styleable.SelectBtn_noSelectImg);
        cantSelectImg = a.getDrawable(R.styleable.SelectBtn_cantSelectImg);
        isSelect = a.getBoolean(R.styleable.SelectBtn_isSelectB, false);
        selectColor = a.getColor(R.styleable.SelectBtn_selectColor, 0);
        noSelectColor = a.getColor(R.styleable.SelectBtn_noSelectColor, 0);
        playAnimator = a.getBoolean(R.styleable.SelectBtn_playAnimator, false);
        setSelect(isSelect);
    }

    @Override
    public void setSelect(boolean boo) {
        isSelect = boo;
        drawImg();
    }

    public void toggleSelect() {
        setSelect(!isSelect);
    }

    @Override
    public boolean getSelect() {
        return isSelect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSelect) {
            if (selectColor != 0) {
                canvas.drawColor(selectColor);
            }
        } else {
            if (noSelectColor != 0) {
                canvas.drawColor(noSelectColor);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            drawImg();
        } else {
            if (cantSelectImg != null) {
                setImageDrawable(cantSelectImg);
            }
        }
    }

    @Override
    public boolean getIsSelect() {
        return isSelect;
    }


    public void SetImgs(Drawable selectImg, Drawable noSelectImg) {
        this.selectImg = selectImg;
        this.noSelectImg = noSelectImg;
        drawImg();
    }

    public void SetSelectImg(Drawable drawable) {
        selectImg = drawable;
    }

    public void SetNoselectImg(Drawable drawable) {
        noSelectImg = drawable;
    }

    public void drawImg() {
        if (isSelect) {
            setImageDrawable(selectImg);
            if (playAnimator) {
                playAnimation();
            }
        } else {
            setImageDrawable(noSelectImg);
        }
    }

    public void playAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.7f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.7f, 1f);
        animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
        animatorSet.setDuration(200);
        //两个动画同时开始
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }
}
