package com.example.teamsup.ui.teammember;

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
import com.example.teamsup.btui.Pro_listItem;

import java.util.ArrayList;

public class TeamListViewAdapter extends BaseAdapter {
    ArrayList<Team_listItem> items = new ArrayList<Team_listItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Team_listItem item) {
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
        final Team_listItem listItem = items.get(position);//해당 위치의 리스트 아이템

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.team_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView list_name = (TextView) convertView.findViewById(R.id.team_name);
        TextView list_email = (TextView) convertView.findViewById(R.id.team_email);
        TextView list_num = (TextView) convertView.findViewById(R.id.team_num);
        TextView list_covid = (TextView) convertView.findViewById(R.id.team_covid);

        if(listItem!=null) {
            list_name.setText("이름 : " + listItem.getTeam().getName());
            list_email.setText("이메일 : " + listItem.getTeam().getEMail());
            list_num.setText("연락처 : " + listItem.getTeam().getPhoneNum());
            if (listItem.getTeam().getIsolation())
                list_covid.setText("격리여부 : " + "O");
            else
                list_covid.setText("격리여부 : " + "X");
        }

        return convertView;  //뷰 객체 반환
    }
}
