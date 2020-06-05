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

        netImage.setUrl("http://img0.imgtn.bdimg.com/it/u=1354448862,1057878114&fm=11&gp=0.jpg");
        roundNetImage.setUrl("http://img3.imgtn.bdimg.com/it/u=1368451564,780267377&fm=11&gp=0.jpg");
    }

}
