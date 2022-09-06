package com.example.teamsup.btui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.teamsup.Account;
import com.example.teamsup.BottomActivity;
import com.example.teamsup.Profile;
import com.example.teamsup.ProfilesetActivity;
import com.example.teamsup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener  {
    //프로필 화면을 나타내는 프래그먼트
    //BottomActivity에서 account가지고와 해당 account에 맞는 프로필과 참여중인 프로젝트 방을 파이어베이스에서 가지고온다.
    Account account;
    private ListView listview = null;
    private ListViewAdapter adapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        account = ((BottomActivity) BottomActivity.context_main).account;    //account가지고옴
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //프로필 정보 가지고와 화면의 TextView에 출력.
        TextView name = (TextView)v.findViewById(R.id.pro_name);
        TextView email = (TextView)v.findViewById(R.id.pro_email);
        TextView phone = (TextView)v.findViewById(R.id.pro_phone);
        TextView covid = (TextView)v.findViewById(R.id.pro_covid);
        Profile pr = account.getProfile();
        name.setText("이름 : " + pr.getName());
        email.setText("이메일 : " + pr.getEMail());
        phone.setText("연락처 : " + pr.getPhoneNum());
        if(pr.getIsolation())
            covid.setText("격리여부 : O");
        else
            covid.setText("격리여부 : X");

        Button set_profile = (Button)v.findViewById(R.id.set_profile);
        set_profile.setOnClickListener(this);
        Button logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        this.listview = (ListView) v.findViewById(R.id.pro_listview);
        adapter = new ListViewAdapter();

        //파이어베이스에서 참여중인 프로젝트방 가지고와 출력
        FirebaseDatabase.getInstance().getReference("account").child(account.getProfile().getEMail()).child("participate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String hostEmail = postSnapshot.getKey().toString();
                    for(DataSnapshot nowSnapshot : postSnapshot.getChildren()){
                        String rname = nowSnapshot.getKey().toString();
                        adapter.addItem(new Pro_listItem(Integer.toString(i++ + 1), rname+"("+hostEmail+")"));
                        listview.setAdapter(adapter);
                    }
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("app", "Failed to read value.", error.toException());
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_profile:// 프로필 변경버튼 클릭시
                //ProfilesetActivity를 실행해 프로필 변경 화면으로 이동.
                Log.d("app", "set");
                Intent in = new Intent(getActivity(), ProfilesetActivity.class);
                in.putExtra("account", account);
                startActivity(in);
                break;
            case R.id.logout:// 로그아웃버튼 클릭시
                //User클래스의 logout()메소드로 로그아웃
                BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
                bt.user.logout();
                Log.d("app", "finish");
                break;
        }
    }
}