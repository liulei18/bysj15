package com.edu.zzti.assm.util;

import java.util.HashMap;
import java.util.Map;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by feng on 2015/2/12.
 */
public class ViewParams {

    private Map<String,Button> buttonMap = new HashMap<String,Button>();
    private Map<String,TextView> textViewMap = new HashMap<String,TextView>();
    private Map<String,EditText> editTextMap = new HashMap<String,EditText>();
    private Map<String,ImageView> imageViewMap = new HashMap<String,ImageView>();
    private Map<String,ListView> listViewMap = new HashMap<String,ListView>();

    public Map<String, Button> getButtonMap() {
        return buttonMap;
    }

    public Map<String, TextView> getTextViewMap() {
        return textViewMap;
    }

    public Map<String, EditText> getEditTextMap() {
        return editTextMap;
    }

    public Map<String, ImageView> getImageViewMap() {
        return imageViewMap;
    }

    public Map<String, ListView> getListViewMap() {
        return listViewMap;
    }
}
