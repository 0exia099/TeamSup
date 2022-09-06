package com.example.teamsup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.LinkedList;

public class Schedule implements Serializable {
    //일정 클래스
    private LinkedList<String> schedule;

    public Schedule(LinkedList<String> schedule){
        this.schedule =schedule;
    }
    public void setSchedule(LinkedList<String> list){
        ProjectRoom pr = ((SetscheduleActivity) SetscheduleActivity.setSchedule).pr;
        this.schedule = list;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");

        int i = 1;
        for(String s : schedule){
            if(s.getBytes().length <= 0){
                myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("schedule").child(Integer.toString(i++)).setValue("");
            }
            else{
                myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("schedule").child(Integer.toString(i++)).setValue(s);
            }
        }
    }
    public LinkedList<String> getSchedule(){
        return schedule;
    }
}
