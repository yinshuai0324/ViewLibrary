package com.yinshuai.library.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.yinshuai.library.view.img.NetImageView;
import com.yinshuai.library.view.img.RoundNetworkImageView;

public class NetImageActivity extends AppCompatActivity {
    private NetImageView netImage;
    private RoundNetworkImageView roundNetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_image);

        initView();
    }

    public void initView() {
        netImage = findViewById(R.id.netImage);
        roundNetImage = findViewById(R.id.roundNetImage);


        netImage.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606713927042&di=b0b187f28183d4814ad77e17f5124016&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F27%2F67%2F01300000921826141299672233506.jpg");
        roundNetImage.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606713927042&di=b0b187f28183d4814ad77e17f5124016&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F27%2F67%2F01300000921826141299672233506.jpg");

        roundNetImage.setOnImageLoadListener(new NetImageView.OnImageLoadListener() {
            @Override
            public void loadSucceed(String url, Drawable resource) {
                Log.i("=====>>>>", "图片加载成功:" + url);
            }

            @Override
            public void loadFailed(String url, Exception exception) {
                Log.i("=====>>>>", "失败:" + url);
            }
        });
    }

}
