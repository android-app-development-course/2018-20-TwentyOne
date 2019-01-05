package com.example.testing.tone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Vector;



/**
 * Created by hhhqzh on 2018/12/12.
 */

//适配器
public class MyShowAdapter extends BaseAdapter {

    private Context context;//上下文对象
    private Vector<Thing> thingList;//ListView显示的数据

    public MyShowAdapter(Context context, Vector<Thing> thingList) {
        this.context = context;
        this.thingList = thingList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return thingList == null ? 0 : thingList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return thingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mThing.setText(thingList.get(position).get_thing());
        viewHolder.mEncourage.setText(thingList.get(position).get_encourage());
        viewHolder.mTime.setText(thingList.get(position).get_time());
        viewHolder.checkBox.setChecked(thingList.get(position).get_flag());
        return convertView;
    }

    private final class ViewHolder {

        TextView mThing;
        TextView mEncourage;
        TextView mTime;//内容
        CheckBox checkBox;

        /**
         * 构造器
         *
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            mThing = (TextView) view.findViewById(R.id.item_thing);
            mEncourage = (TextView) view.findViewById(R.id.item_encourage);
            mTime = (TextView) view.findViewById(R.id.item_time);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}

