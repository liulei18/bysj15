package com.edu.zzti.assm.app;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.service.LoginAsync;


public class LoginActivity extends BaseActivity {

//    private TextView mRegTxt;
    private Button mLogBtn;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioStu;
    private RadioButton mRadioTea;
    private Context context = this;
    private CheckBox mAutobox;
    private EditText mName;
    private EditText mPwd;
    private MyApp myApp;

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_login);
    }

    @Override
    public void initCompontent() {
//        mRegTxt = this.getById(R.id.ui_register_text, TextView.class);
        mLogBtn = this.getById(R.id.ui_login_btn, Button.class);
        mRadioGroup =this.getById(R.id.ui_radio_group,RadioGroup.class);
        mRadioStu =this.getById(R.id.ui_radio_stu,RadioButton.class);
        mRadioTea =this.getById(R.id.ui_radio_tea,RadioButton.class);
        mRadioStu.setChecked(true);
        mName = this.getById(R.id.ui_login_name,EditText.class);
        mPwd = this.getById(R.id.ui_password,EditText.class);
        mAutobox = this.getById(R.id.ui_auto_login,CheckBox.class);

        myApp =(MyApp)getApplication();
    }

    @Override
    public void initListener() {
//        mRegTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RegActivity.class);
//                context.startActivity(intent);
//            }
//        });

        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = mAutobox.isChecked();
                Intent intent =null;
                if(mRadioStu.isChecked()){
                    String userName = mName.getText().toString();
                    String userPwd = mPwd.getText().toString();
                    LoginAsync loginAsync = new LoginAsync(context,userName,userPwd,"student",params,myApp,LoginActivity.this,flag);
                    loginAsync.startTask();
                }else if(mRadioTea.isChecked()){
                    String userName = mName.getText().toString();
                    String userPwd = mPwd.getText().toString();
                    LoginAsync loginAsync = new LoginAsync(context,userName,userPwd,"teacher",params,myApp,LoginActivity.this,flag);
                    loginAsync.startTask();
                }
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == mRadioStu.getId()){
                    mRadioTea.setChecked(false);
                }else if(checkedId == mRadioTea.getId()){
                    mRadioStu.setChecked(false);
                }
            }
        });

    }



}
