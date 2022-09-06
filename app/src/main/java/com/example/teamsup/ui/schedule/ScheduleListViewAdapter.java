package com.example.teamsup.ui.schedule;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.teamsup.R;

import java.util.ArrayList;

public class ScheduleListViewAdapter extends BaseAdapter {
    //일정 리스트의 어댑터
    ArrayList<Schedule_listItem> items = new ArrayList<Schedule_listItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Schedule_listItem item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final Schedule_listItem listItem = items.get(position);//해당 위치의 리스트 아이템

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.schedule_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView list_msg = (TextView) convertView.findViewById(R.id.scheduleM);

        list_msg.setText(listItem.getMsg());
        list_msg.setGravity(Gravity.CENTER);
        list_msg.setBackgroundResource(R.drawable.my_msg);

        return convertView;  //뷰 객체 반환
    }
}
