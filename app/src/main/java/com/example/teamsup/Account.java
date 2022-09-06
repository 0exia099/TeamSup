package com.example.teamsup;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    //계정 객체를 만드는 클래스.
    private String id;  //계정 id(email)
    private String pw;  //계정 비밀번호
    private Profile profile;    //해당 계정의 프로필

    public Account(String id, String pw, Profile profile){
        //생성자
        this.id =id;
        this. pw = pw;
        this.profile = profile;
    }

    public void settingProfile(String name, String eMail, String phoneNum, Boolean isolation){
        //프로필을 변경하는 메소드. Profile클래스의 메소드를 사용한다.
        profile.setName(name);
        profile.setEMail(eMail);
        profile.setPhoneNum(phoneNum);
        profile.setIsolation(isolation);
    }

    public Profile getProfile(){
        //프로필을 반환하는 메소드
        return this.profile;
    }

    public String getPw() {
        //pw를 반환하는 메소드
        return pw;
    }
}
