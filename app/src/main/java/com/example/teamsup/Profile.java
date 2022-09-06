package com.example.teamsup;

import java.io.Serializable;

public class Profile implements Serializable {
    //프로필 클래스
    private String name;
    private String eMail;
    private String phoneNum;
    private Boolean isolation;

    public Profile(String name, String eMail, String phoneNum){
        //생성자
        this.name = name;
        this.eMail = eMail;
        this.phoneNum = phoneNum;
        this.isolation = false;
    }

    public String getName(){
        return this.name;
    }
    public String getEMail(){
        return this.eMail;
    }
    public String getPhoneNum(){
        return this.phoneNum;
    }
    public Boolean getIsolation(){
        return this.isolation;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setIsolation(Boolean isolation) {
        this.isolation = isolation;
    }
}
