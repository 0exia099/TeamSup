package com.example.teamsup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Login {
    //로그인에 관련된 클래스
    private String id;
    private String pw;

    public Login(String id, String pw){
        //생성자
        this.id = id;
        this.pw = pw;
    }

    public Account login(){
        //Login객체의 id와 pw를 이용해 계정있는지 확인 후 계정을 파이어베이스에서 가져와 로그인.
        LoginActivity loginActivity = (LoginActivity) ((LoginActivity) LoginActivity.context_Login)._LoginActivity;
        Profile profile = new Profile("", id, "0");
        Account account = new Account(id, pw, profile);

        FirebaseDatabase.getInstance().getReference("account").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("app", "불러오기");
                    Log.d("app", task.getResult().getKey().toString());
                    if (task.getResult().child(id).exists()) {
                        Log.d("app", "find id");
                        if (task.getResult().child(id).child("pw").getValue().toString().equals(pw)) {
                            Log.d("app", "find pw");
                            account.getProfile().setName(task.getResult().child(id).child("name").getValue().toString());
                            account.getProfile().setEMail(id);
                            account.getProfile().setPhoneNum(task.getResult().child(id).child("phoneNum").getValue().toString());
                            account.getProfile().setIsolation((Boolean) task.getResult().child(id).child("iso").getValue());
                            Intent in = new Intent(loginActivity, BottomActivity.class);
                            in.putExtra("account", account);
                            loginActivity.startActivity(in);
                            return;

                        } else {
                            Toast.makeText(loginActivity, "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(loginActivity, "이메일로된 계정이 없습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        return account;
    }
    public void autoLogin(){
        //자동로그인
        SharedPreferences auto = ((LoginActivity) LoginActivity.context_Login).auto;
        SharedPreferences.Editor autoConnectEdit = auto.edit();
        autoConnectEdit.putString("id", id);
        autoConnectEdit.putString("pw", pw);
        autoConnectEdit.commit();
    }
}
