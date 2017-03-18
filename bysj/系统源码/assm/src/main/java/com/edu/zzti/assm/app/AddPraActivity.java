package com.edu.zzti.assm.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.model.Chapter;
import com.edu.zzti.assm.service.PraAddAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;


public class AddPraActivity extends BaseActivity {

    private ImageView mImgBack;
    private EditText mPraName; //练习名称
    private EditText mPraDx; //单选数量
    private EditText mPraPd; //判断数量
    private EditText mPraJd; //简答数量
    private EditText mPraTime; //简答数量
    private Button mPraJie ;  //章节选择
    private Button mPraPre ;  //预览
    private MyApp myApp ;
    private Context context= this;

    private  List mSelectedItems;
    private List<Chapter> chapters =new ArrayList<>();
    @Override
    public void initLayout() {
        setContentView(R.layout.ui_add_pra);
        myApp = (MyApp)getApplication();
    }

    @Override
    public void initCompontent() {
        mImgBack = this.getById(R.id.ui_back,ImageView.class);
        mPraName = this.getById(R.id.ui_pra_name,EditText.class);
        mPraDx = this.getById(R.id.ui_pra_dx,EditText.class);
        mPraPd = this.getById(R.id.ui_pra_pd,EditText.class);
        mPraJd = this.getById(R.id.ui_pra_jd,EditText.class);
        mPraJie = this.getById(R.id.ui_btn_pra_jie,Button.class);
        mPraPre = this.getById(R.id.ui_btn_pra_pre,Button.class);
        mPraTime =this.getById(R.id.ui_pra_time,EditText.class);
    }

    @Override
    public void initListener() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPraActivity.this.finish();
            }
        });

        mPraJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Chapter> sets =  myApp.getUnit().getChapters();
                Object[] objects = sets.toArray();
                chapters = new ArrayList<Chapter>();
                for( Object obj: objects){
                    chapters.add((Chapter)obj);
                }
                Collections.sort(chapters ,new Comparator<Chapter>() {
                    @Override
                    public int compare(Chapter lhs, Chapter rhs) {
                        if(null!=lhs&&null!=rhs){
                            if(lhs.getId()<rhs.getId()){
                                return -1;
                            }else if (lhs.getId()>rhs.getId()){
                                return 1;
                            }
                        }
                        return 0;
                    }
                });
                List<String> list = new ArrayList<String>();
                for(Chapter chapter :chapters){
                    list.add(chapter.getChaName());
                }
               String[] array= list.toArray(new String []{});

               mSelectedItems = new ArrayList();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("请选择章节")
                        .setMultiChoiceItems(array, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            mSelectedItems.add(which);
                                        } else if (mSelectedItems.contains(which)) {
                                            mSelectedItems.remove(Integer.valueOf(which));
                                        }
                                    }
                                })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                for (int i = chapters.size()-1; i >-1 ; i--) {
                                    if (!mSelectedItems.contains(i)) {
                                        chapters.remove(i);
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                chapters.clear();
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });


        mPraPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chapters.size() >0) {
                    if(!mPraName.getText().toString().trim().equals("")) {
                        Integer praDx = Integer.valueOf(mPraDx.getText().toString().trim());
                        Integer praPd = Integer.valueOf(mPraPd.getText().toString().trim());
                        Integer praJd = Integer.valueOf(mPraJd.getText().toString().trim());
                        if(praDx+praPd+praJd!=0) {
                            Map map = new HashMap<>();
                            map.put("praName", mPraName.getText().toString().trim());
                            map.put("praDx", praDx);
                            map.put("praPd", praPd);
                            map.put("praJd", praJd);
                            Calendar nowTime = Calendar.getInstance();
                            nowTime.add(Calendar.MINUTE, Integer.valueOf(mPraTime.getText().toString().trim()));
                            map.put("praCutTime", nowTime.getTime());
                            JSONObject jsonObject = (JSONObject) myApp.getObj();
                            String teacherId = "";
                            try {
                                teacherId = ((JSONObject) myApp.getObj()).getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("teacherId", teacherId);
                            map.put("unitId", myApp.getUnit().getId().toString());
                            String chapterIds = "";
                            for (Chapter chapter : chapters) {
                                chapterIds = chapterIds + chapter.getId() + ",";
                            }
                            chapterIds = chapterIds.substring(0, chapterIds.lastIndexOf(","));
                            map.put("chapterIds", chapterIds);

                            PraAddAsync async = new PraAddAsync(context,map,params);
                            async.startTask();


                        }else{
                            Toast.makeText(context,"没有设置一道题哦...",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context,"请设置测试题名称...",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"请选择章节...",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
