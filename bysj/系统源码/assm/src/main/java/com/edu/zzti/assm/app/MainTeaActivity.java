package com.edu.zzti.assm.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.loader.AsyncImageLoader;
import com.edu.zzti.assm.loader.ImageCallBackImpl;
import com.edu.zzti.assm.service.QueryLexiconAsync;
import com.edu.zzti.assm.util.ViewParams;
import com.edu.zzti.assm.view.CircleImageView;
import com.edu.zzti.assm.view.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class MainTeaActivity extends BaseActivity {
    private SlidingMenu mLeftMenu;
    private ImageView mMenuBtn;
    private CircleImageView mPhoto;
    private TextView mSearch;
    private TextView mPracticeSetting;
    private TextView mLookPractice;
    private TextView mPracticeCount;
    private TextView mSetting;
    private Button mSearchBtn;
    private TextView mResultTxt;
    private EditText mWordQuery;
    private TextView mUserName;
    private MyApp myApp;


    @Override
    public void initLayout() {
        setContentView(R.layout.ui_main_tea);
    }

    @Override
    public void initCompontent() {
        mLeftMenu = this.getById(R.id.id_menu, SlidingMenu.class);
        mMenuBtn = this.getById(R.id.ui_menuBtn, ImageView.class);
        mUserName = this.getById(R.id.id_user, TextView.class);
        mSearch = this.getById(R.id.ui_search, TextView.class);
        mPracticeSetting = this.getById(R.id.ui_practice_setting, TextView.class);
        mLookPractice = this.getById(R.id.ui_look_practice, TextView.class);
        mPracticeCount = this.getById(R.id.ui_practice_count, TextView.class);
        mSetting = this.getById(R.id.ui_setting, TextView.class);
        mSearchBtn = this.getById(R.id.ui_btn_search,Button.class);
        mResultTxt= this.getById(R.id.ui_result_txt,TextView.class);
        mWordQuery = this.getById(R.id.ui_edit_query,EditText.class);

        mPhoto = this.getById(R.id.id_photo,CircleImageView.class);

        new Thread() {
            @Override
            public void run() {
                HttpHelper helper = new HttpHelper();
                String info = null;
                try {
                    MyResponse response = helper.get(context.getApplicationContext().getString(R.string.ass_get_lexicon_url)+"?"+new Random().nextInt(100));
                    info = response.asString();
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(info);
                        String result =jsonObject.get("word").toString()+" : \n\n" +
                                "   中文释义："+jsonObject.get("zhMean").toString()+"\n\n"+
                                "   English："+jsonObject.get("enMean").toString();

                        mResultTxt.setText(result);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        params=new ViewParams();
        params.getTextViewMap().put("result",mResultTxt);

        myApp = (MyApp) getApplication();
        JSONObject object = (JSONObject) myApp.getObj();
        try {
            mUserName.setText(object.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftMenu.toggle();
            }
        });

        //单词查询
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mWordQuery.getText().toString();
                if(text.trim().equals("")){
                    Toast.makeText(context, "请输入要查询的词汇...", Toast.LENGTH_SHORT).show();
                }else{

                    QueryLexiconAsync async =  new QueryLexiconAsync(context,text,params);
                    async.startTask();
                }
            }
        });

        //图片点击进入个人信息
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InfoActivity.class);
                context.startActivity(intent);
            }
        });
        //菜单切换
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftMenu.toggle();
            }
        });

        //练习设置
        mPracticeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeUnitActivity.class);
                intent.putExtra("intentType",2);
                context.startActivity(intent);
            }
        });
        //查看练习
        mLookPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeUnitActivity.class);
                intent.putExtra("intentType",1);
                context.startActivity(intent);
            }
        });
        //练习统计
        mPracticeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeUnitActivity.class);
                intent.putExtra("intentType",4);
                context.startActivity(intent);
            }
        });


        //进入个人信息页面
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InfoActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        initPhoto();
        super.onResume();
    }

    private void initPhoto(){
        String imgurl ="";
        try {
            imgurl = ((JSONObject)myApp.getObj()).getString("imgurl");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        if(!imgurl.equals("")){
            AsyncImageLoader loader  = new AsyncImageLoader();
            Bitmap bitmap =loader.loadAsyncImage(imgurl, new ImageCallBackImpl(mPhoto));
            if(bitmap!=null) {
                mPhoto.setImageBitmap(bitmap);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("确定退出应用？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .create();
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
