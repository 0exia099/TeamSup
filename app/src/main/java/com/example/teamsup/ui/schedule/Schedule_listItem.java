package com.example.teamsup.ui.schedule;

public class Schedule_listItem implements Comparable<Schedule_listItem>{
    //일정 리스트의 아이템
    String msg;

    public Schedule_listItem(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int compareTo(Schedule_listItem schedule_listItem) {
        return this.msg.compareTo(schedule_listItem.msg);
    }
}
