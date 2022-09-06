package com.example.teamsup;

public class Message {
    //메시지 클래스
    private String sender;
    private String msg;
    private String time;

    public Message(String sender, String msg, String time){
        //생성자
        this.sender = sender;
        this.msg = msg;
        this.time = time;
    }
    public Message(){
        //생성자

    }

    public String getSender() {
        return sender;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
