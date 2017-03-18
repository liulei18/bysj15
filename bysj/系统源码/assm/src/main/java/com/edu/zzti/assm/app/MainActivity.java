package com.edu.zzti.assm.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity {
    private Context context =this;
	private SlidingMenu mLeftMenu ;
	private ImageView mMenuBtn;
	private CircleImageView mPhoto;
    private TextView mSearch;
    private TextView mIntoPractice;
    private TextView mMyPractice ;
    private TextView mIntoWeb;
    private TextView mSetting;
    private Button mSearchBtn;
    private TextView mResultTxt;
    private EditText mWordQuery;
    private TextView mUserName;


    private MyApp myApp;
    private long exitTime = 0;

    private ViewParams params;

	@Override
	public void initLayout() {
		setContentView(R.layout.ui_main);
	}

	@Override
	public void initCompontent() {
		mLeftMenu = this.getById(R.id.id_menu,SlidingMenu.class);
		mMenuBtn = this.getById(R.id.ui_menuBtn, ImageView.class);

        mPhoto = this.getById(R.id.id_photo,CircleImageView.class);
        mSearch = this.getById(R.id.ui_search,TextView.class);
        mIntoPractice = this.getById(R.id.ui_into_practice,TextView.class);
        mMyPractice = this.getById(R.id.ui_my_practice,TextView.class);
        mIntoWeb = this.getById(R.id.ui_into_web,TextView.class);
        mSetting = this.getById(R.id.ui_setting,TextView.class);

        mSearchBtn = this.getById(R.id.ui_btn_search,Button.class);
        mResultTxt= this.getById(R.id.ui_result_txt,TextView.class);

        mWordQuery = this.getById(R.id.ui_edit_query,EditText.class);
        mUserName= this.getById(R.id.id_user,TextView.class);

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

        myApp = (MyApp)getApplication();
        JSONObject object = (JSONObject) myApp.getObj();
        try {
            mUserName.setText(object.get("name").toString());
        } catch (JSONException e) {
//           e.printStackTrace();
        }

    }



	@Override
	public void initListener() {
        //左上按钮菜单切换
		mMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLeftMenu.toggle();
				
			}
		});

        //单词查询
        mSearchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            String text = mWordQuery.getText().toString();
                if(text.trim().equals("")){
                    Toast.makeText(context,"请输入要查询的词汇...",Toast.LENGTH_SHORT).show();
                }else{

                    QueryLexiconAsync async =  new QueryLexiconAsync(context,text,params);
                    async.startTask();
                }
            }
        });

        //图片点击进入个人信息
        mPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InfoActivity.class);
                context.startActivity(intent);
            }
        });
        //菜单切换
        mSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftMenu.toggle();
            }
        });
        //进入练习
        mIntoPractice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeUnitActivity.class);
                intent.putExtra("intentType",1);
                context.startActivity(intent);
            }
        });
        //查看我的练习
        mMyPractice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeUnitActivity.class);
                intent.putExtra("intentType",3);
                context.startActivity(intent);
            }
        });
        //进入网站
        mIntoWeb.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebActivity.class);
                context.startActivity(intent);
            }
        });
        //进入个人信息页面
        mSetting.setOnClickListener(new OnClickListener() {
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
