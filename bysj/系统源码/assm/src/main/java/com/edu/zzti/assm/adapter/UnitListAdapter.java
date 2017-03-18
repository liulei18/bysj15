package com.edu.zzti.assm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.zzti.assm.app.R;
import com.edu.zzti.assm.model.Unit;

import java.util.List;

/**
 * Created by feng on 2015/4/27.
 */
public class UnitListAdapter extends BaseAdapter {
    private Context context = null;
    private List<Unit> list = null;

    public UnitListAdapter(Context context, List<Unit> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ui_practice_unit_item,null);

            holder.txtView = (TextView)convertView.findViewById(R.id.ui_uint_txt);
            convertView.setTag(holder);
        }else{
            holder  =(ViewHolder)convertView.getTag();
        }
        holder.txtView.setText(list.get(position).getUnitName());
        return convertView;
    }
    class ViewHolder{
        TextView txtView;
    }

}
