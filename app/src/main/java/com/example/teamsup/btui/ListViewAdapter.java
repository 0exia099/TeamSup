package com.example.teamsup.btui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.teamsup.BottomActivity;
import com.example.teamsup.JoinprojectActivity;
import com.example.teamsup.R;

import java.util.ArrayList;
//프로필화면과 프로젝트방 검색화면의 리스트의 어뎁터
public class ListViewAdapter extends BaseAdapter {
    ArrayList<Pro_listItem> items = new ArrayList<Pro_listItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Pro_listItem item) {
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
        final Pro_listItem listItem = items.get(position);//해당 위치의 리스트 아이템

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pro_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView list_num = (TextView) convertView.findViewById(R.id.pro_list_num);
        TextView list_room = (TextView) convertView.findViewById(R.id.pro_list_room);

        list_num.setText(listItem.getNum());
        list_room.setText(listItem.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            //리스트의 해당항목 클릭시 JoinprojectActivity 실행
            @Override
            public void onClick(View view) {
                BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;

                Intent in = new Intent(bt, JoinprojectActivity.class);
                in.putExtra("room", listItem.getName());
                in.putExtra("account", bt.account);
                bt.startActivity(in);
            }
        });

        return convertView;  //뷰 객체 반환
    }
}
