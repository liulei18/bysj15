package com.edu.zzti.assm.app;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.edu.zzti.assm.adapter.UnitListAdapter;
import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.model.Unit;
import com.edu.zzti.assm.service.UnitAsync;

import java.util.ArrayList;
import java.util.List;


public class PracticeUnitActivity extends BaseActivity {
    private ImageView mBack;
    private ListView  mListView;
    private Context context =this;
    private MyApp myApp ;
    private Integer intentType;

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_practice_unit);
        intentType =  getIntent().getIntExtra("intentType",1);
        myApp = (MyApp)getApplication();
    }

    @Override
    public void initCompontent() {
        mBack =  this.getById(R.id.ui_back,ImageView.class);
        mListView =  this.getById(R.id.ui_unit_list,ListView.class);
        params.getListViewMap().put("mListView",mListView);
        UnitAsync async = new UnitAsync(context,params);
        async.startTask();
//        UnitListAdapter adapter = new UnitListAdapter(context,list);
//        mListView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeUnitActivity.this.finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Unit unit =(Unit) mListView.getItemAtPosition(position);

                if(intentType==2){
                    Intent intent  =new Intent(context,AddPraActivity.class);
                    myApp.setUnit(unit);
                    context.startActivity(intent);
                }else{
                    Intent intent  =new Intent(context,PracticeListActivity.class);
                    myApp.setUnit(unit);
                    intent.putExtra("unit",unit.getUnitName())  ;
                    intent.putExtra("intentType",intentType)  ;
                    context.startActivity(intent);

                }


            }
        });
    }
}
