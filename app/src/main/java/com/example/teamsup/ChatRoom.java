package com.example.teamsup;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamsup.ui.chatting.MyAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatRoom extends RealTimeRoom{
    //채팅방 객체를 만드는 클래스
    public ChatRoom(String name, ProjectRoom pr) {
        super(name, pr);
    }

    public void viewChat(){
        Account account = ((MainActivity) MainActivity.context_main).account;
        RecyclerView recyclerView;
        MyAdapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<Message> myDataset;

        recyclerView = (RecyclerView) ((MainActivity) MainActivity.context_main).chatView;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(((MainActivity) MainActivity.context_main)._MainActivity);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");

        myDataset = new ArrayList<Message>();
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(myDataset, account.getProfile().getName());
        recyclerView.setAdapter(mAdapter);

        myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("chat").child("chatting").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                ((MyAdapter)mAdapter).setArrayData(msg);
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
    public void sendMessage(Message msg) {
        //인자로 받은 메시지 파이어베이스에 저장
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");
        myRef.child(pr.getHostEmail()).child(pr.getRoomName()).child("chat").child("chatting").push().setValue(msg);
    }
}
