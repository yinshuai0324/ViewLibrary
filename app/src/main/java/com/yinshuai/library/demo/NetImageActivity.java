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


        netImage.setUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1967835199,1119386598&fm=26&gp=0.jpg");
        roundNetImage.setUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1967835199,1119386598&fm=26&gp=0.jpg");
}

}
