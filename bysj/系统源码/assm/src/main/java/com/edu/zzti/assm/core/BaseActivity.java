package com.edu.zzti.assm.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.edu.zzti.assm.util.ViewParams;



public abstract class BaseActivity extends Activity {

    public Context context = this;
    protected ViewParams params = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        this.params= new ViewParams();
        initCompontent();
        initListener();
    }

    public <T extends View> T getById(int id, Class<T> clazz) {

        return clazz.cast(this.findViewById(id));

    }

    public abstract void initLayout();

    public abstract void initCompontent();

    public abstract void initListener();


}
