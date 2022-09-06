package com.example.teamsup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class ProjectRoom implements Comparable<ProjectRoom>, Serializable {
    //프로젝트 방 클래스
    private String roomName;
    private String hostEmail;
    private String roomPW;
    private ChatRoom chatRoom;
    private AnnouncementRoom announcementRoom;
    private Schedule schedule;
    private ArrayList<Profile> teamMember;
    private String deadLine;
    private String startDay;
    private String name;

    public ProjectRoom(String roomName, String hostEmail, String roomPW, LinkedList<String> schedule, ArrayList<Profile> teamMember, String deadLine, String startDay, String name){
        //생성자
        this.roomName = roomName;
        this.hostEmail = hostEmail;
        this.roomPW = roomPW;
        this.schedule = new Schedule(schedule);
        this.teamMember = teamMember;
        this.deadLine = deadLine;
        this.startDay = startDay;
        this.name = name;
        this.chatRoom = createCR();
        this.announcementRoom = createAR();
    }

    public ProjectRoom(String roomName, String hostEmail, String roomPW, String deadLine, String startDay, String name){
        //생성자
        this.roomName = roomName;
        this.hostEmail = hostEmail;
        this.roomPW = roomPW;
        this.teamMember = new ArrayList<Profile>();
        this.deadLine = deadLine;
        this.startDay = startDay;
        this.name = name;
        this.chatRoom = createCR();
        this.announcementRoom = createAR();
        FirebaseDatabase.getInstance().getReference("PR").child(hostEmail).child(roomName).child("teamMember").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String tname = postSnapshot.child("name").getValue().toString();
                    String temail = postSnapshot.child("email").getValue().toString();
                    String tphone = postSnapshot.child("phoneNum").getValue().toString();
                    Boolean tiso = (Boolean) postSnapshot.child("isolation").getValue();
                    Profile profile = new Profile(tname, temail, tphone);
                    profile.setIsolation(tiso);
                    teamMember.add(profile);
                }
                FirebaseDatabase.getInstance().getReference("PR").child(hostEmail).child(roomName).child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LinkedList<String> s = new LinkedList<String>();
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            String sche = postSnapshot.getValue().toString();
                            s.add(sche);
                        }
                        schedule = new Schedule(s);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("app", "Failed to read value.", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("app", "Failed to read value.", error.toException());
            }
        });
    }

    public ChatRoom createCR(){
        //ChatRoom객체 생성
        ChatRoom cr = new ChatRoom(name, this);
        return cr;
    }
    public AnnouncementRoom createAR(){
        //AnnouncementRoom객체 생성
        AnnouncementRoom ar = new AnnouncementRoom(name, this);
        return ar;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public ChatRoom getCR() {
        return chatRoom;
    }

    public AnnouncementRoom getAR() {
        return announcementRoom;
    }

    @Override
    public int compareTo(ProjectRoom projectRoom) {
        if(this.deadLine.compareTo(projectRoom.deadLine)>0)
            return 1;
        else if(this.deadLine.compareTo(projectRoom.deadLine)<0)
            return -1;
        else
            return 0;
    }
}
