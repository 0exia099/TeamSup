package com.example.teamsup;

import java.io.Serializable;

public abstract class RealTimeRoom implements Serializable {
    //ChatRoom, AnnouncementRoom의 부모 클래스인 가상클래스.
    protected String name;
    protected ProjectRoom pr;

    public RealTimeRoom(String name, ProjectRoom pr){
        this.name = name;
        this.pr = pr;
    }
    public abstract void sendMessage(Message msg);
}
