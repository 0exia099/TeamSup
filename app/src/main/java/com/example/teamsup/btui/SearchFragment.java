package com.example.teamsup.btui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.teamsup.Account;
import com.example.teamsup.CreateprActivity;
import com.example.teamsup.R;
import com.example.teamsup.User;
import com.example.teamsup.BottomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment implements View.OnClickListener {
    //프로젝트방 검색 화면을 나타내는 프래그먼트
    //상단 editText에 입력한 이메일로 프로젝트 방을 검색하거나 프로젝트방을 생성 할 수 있다.
    User user;
    Account account;
    private ListView listview = null;
    private ListViewAdapter adapter = null;
    EditText email;
    BottomActivity bt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
        user = bt.user;
        account = bt.account;
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        bt.searchView = v;
        email = (EditText)v.findViewById(R.id.ser_email);
        Button search = (Button)v.findViewById(R.id.ser_search);
        search.setOnClickListener(this);
        Button create = (Button) v.findViewById(R.id.ser_create);
        create.setOnClickListener(this);
        this.listview = (ListView) v.findViewById(R.id.ser_list);
        adapter = new ListViewAdapter();

        return v;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ser_search://프로젝트 방 검색 버튼 클릭시
                //EditText에 입력한 호스트이메일을 User클래스의 searchPR메소드로 넘겨 해당 호스트로 하는 프로젝트 방을 가지고온다.
                String E_Mail = email.getText().toString();
                String host = E_Mail.replaceAll("[.]", ",");
                user.searchPR(host);
                break;
            case R.id.ser_create://프로젝트 방 생성 버튼 클릭시
                //CreateprActivity를 실행 해 프로젝트 방 생성 화면으로 이동.
                Intent in = new Intent(getActivity(), CreateprActivity.class);
                in.putExtra("account", account);
                in.putExtra("user", user);
                startActivity(in);
                break;
        }
    }
}