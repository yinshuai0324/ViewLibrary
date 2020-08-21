package com.yinshuai.library.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        netImage.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598039976631&di=4f78e558157d270bc267994e54d964f0&imgtype=0&src=http%3A%2F%2Fphoto.meifajie.com%2Fpictures%2F2015-06%2F61_1435126528.jpg");
        roundNetImage.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598039976631&di=4f78e558157d270bc267994e54d964f0&imgtype=0&src=http%3A%2F%2Fphoto.meifajie.com%2Fpictures%2F2015-06%2F61_1435126528.jpg");
    }

}
