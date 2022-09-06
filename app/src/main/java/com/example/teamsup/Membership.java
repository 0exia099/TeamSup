package com.example.teamsup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class Membership {
    //회원가입 클래스
    private Account account;

    public Membership(String id, String pw, String name, String phoneNum){
        //생성자
        Profile pf = new Profile(name, id, phoneNum);
        account = new Account(id, pw, pf);
    }
    public void membership(){
        //생성한 계정을 파이어베이스에 저장하여 회원가입을 하는 메소드.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Profile pf = account.getProfile();

        Hashtable<String, String> profile = new Hashtable<String, String>();
        profile.put("pw", account.getPw());
        profile.put("name", pf.getName());
        profile.put("phoneNum", pf.getPhoneNum());

        myRef.child("account").child(pf.getEMail()).setValue(profile);
        myRef.child("account").child(pf.getEMail()).child("iso").setValue(false);
    }
}
