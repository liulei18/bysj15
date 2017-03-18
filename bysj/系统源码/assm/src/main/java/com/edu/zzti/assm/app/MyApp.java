package com.edu.zzti.assm.app;

import android.app.Application;

import com.edu.zzti.assm.model.Unit;

/**
 * Created by feng on 2015/4/27.
 */
public class MyApp extends Application {

    private Object obj;

    private Unit unit;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
