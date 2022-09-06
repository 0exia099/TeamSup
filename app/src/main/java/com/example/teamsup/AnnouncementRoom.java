package com.example.teamsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamsup.ui.announcement.AnnounceAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AnnouncementRoom extends RealTimeRoom{
    //공지사항 방 객체를 만드는 클래스
    public AnnouncementRoom(String name, ProjectRoom pr) {
        //생성자
        super(name, pr);
    }
    public void viewAnnounce(){
        //파이어베이스에서 해당 방의 공지사항을 가져와 공지사항 화면에 출력
        Account account = ((MainActivity) MainActivity.context_main).account;
        RecyclerView recyclerView;
        AnnounceAdapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<Message> myDataset;

        recyclerView = (RecyclerView) ((MainActivity) MainActivity.context_main).announcementView;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(((MainActivity) MainActivity.context_main)._MainActivity);

        recyclerView.setLayoutManager(layoutManager);
        myDataset = new ArrayList<Message>();
        mAdapter = new AnnounceAdapter(myDataset, account.getProfile().getName());
        recyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");

        myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("announce").child("announcement").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                ((AnnounceAdapter)mAdapter).setArrayData(msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void sendMessage(Message msg){
        //받은 메시지 파이어베이스에 저장
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");
        myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("announce").child("announcement").push().setValue(msg);
    }
}
