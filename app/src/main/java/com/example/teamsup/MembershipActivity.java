package com.example.teamsup;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MembershipActivity extends AppCompatActivity {
    //회원가입 화면 액티비티
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        EditText email = (EditText)findViewById(R.id.mem_email);
        EditText pw = (EditText)findViewById(R.id.mem_password);
        EditText pwck = (EditText)findViewById(R.id.mem_pwck);
        EditText name = (EditText)findViewById(R.id.mem_name);
        EditText num = (EditText)findViewById(R.id.mem_num);
        Button login = (Button)findViewById(R.id.mem_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = email.getText().toString();
                String passwd = pw.getText().toString();
                String passwdck = pwck.getText().toString();
                String user_name = name.getText().toString();
                String phoneNum = num.getText().toString();

                String id = eMail.replaceAll("[.]", ",");
                if(!passwd.equals(passwdck)){
                    Toast.makeText(MembershipActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                Membership membership = new Membership(id, passwd, user_name, phoneNum);

                FirebaseDatabase.getInstance().getReference("account").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            ArrayList<ProjectRoom> list = new ArrayList<ProjectRoom>();
                            for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                                if(postSnapshot.getKey().toString().equals(id)) {
                                    Toast.makeText(MembershipActivity.this,"이미 존재하는 이메일 입니다.",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            membership.membership();    //회원가입
                            finish();
                            return;
                        }
                    }
                });
            }
        });
        Button mem = (Button)findViewById(R.id.mem_cancle);
        mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
