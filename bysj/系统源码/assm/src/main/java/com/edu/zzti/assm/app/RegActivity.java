package com.edu.zzti.assm.app;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.edu.zzti.assm.core.BaseActivity;


public class RegActivity extends BaseActivity {
    private  ImageView mImgBack;
    private Context context = this;
    @Override
    public void initLayout() {
        setContentView(R.layout.ui_reg);
    }

    @Override
    public void initCompontent() {
        mImgBack = this.getById(R.id.ui_register_back,ImageView.class);

    }

    @Override
    public void initListener() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.this.finish();
            }
        });
    }
}
