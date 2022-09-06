package com.example.teamsup;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.teamsup.btui.ListViewAdapter;
import com.example.teamsup.btui.Pro_listItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

public class User implements Serializable {
    //사용자 클래스
    private Account account;
    private ArrayList<String> searchList;

    public User(Account account){
        this.account = account;
        this.searchList = new ArrayList<String>();
    }

    public Account getAccount(){
        return this.account;
    }

    public void searchPR(String email){
        //프로젝트 방 검색 메소드
        BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
        User user = bt.user;
        View v = bt.searchView;
        ListView listview = (ListView) v.findViewById(R.id.ser_list);
        ListViewAdapter adapter = new ListViewAdapter();
        String host = email;
        final int[] i = {1};
        FirebaseDatabase.getInstance().getReference("PR").child(host).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                        adapter.addItem(new Pro_listItem(Integer.toString(i[0]++), postSnapshot.getKey().toString()+"("+host+")"));
                        listview.setAdapter(adapter);
                        searchList.add(postSnapshot.getKey().toString()+"("+host+")");
                        bt.user = user;
                    }
                }
            }
        });
    }
    public static ProjectRoom joinPR(Profile profile, ProjectRoom pr){
        //프로젝트 방 가입 메소드
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("PR").child(pr.getHostEmail()).child(pr.getRoomName()).child("teamMember").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Profile> teamMember = new ArrayList<Profile>();
                Boolean contain = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String temail = postSnapshot.getKey().toString();
                    if (temail.equals(profile.getEMail()))
                        contain = true;
                }
                if (!contain){
                    myRef.child("PR").child(pr.getHostEmail()).child(pr.getRoomName()).child("teamMember").child(profile.getEMail()).setValue(profile);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("app", "Failed to read value.", error.toException());
            }
        });
        return pr;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ProjectRoom createPR(Account account){
        //프로젝트방 생성 메소드
        String roomName = ((CreateprActivity)CreateprActivity.context_create).room_name;
        String hostEmail= account.getProfile().getEMail();
        String roomPW= ((CreateprActivity)CreateprActivity.context_create).passwd;
        LinkedList<String> schedule = new LinkedList<String>();

        ArrayList<Profile> teamMember = new ArrayList<Profile>();
        teamMember.add(account.getProfile());
        String deadLine = ((CreateprActivity)CreateprActivity.context_create).DeadLine;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDay = sdf.format(date);
        String name = account.getProfile().getName();

        schedule.add("default" + startDay + "/" + deadLine);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PR");

        Hashtable<String, String> pr = new Hashtable<String, String>();
        pr.put("pw", roomPW);
        pr.put("dead line", deadLine);
        pr.put("start day", startDay);

        myRef.child(hostEmail).child(roomName).setValue(pr);

        myRef.child(hostEmail).child(roomName).child("teamMember").child(hostEmail).setValue(teamMember.get(0));
        myRef.child(hostEmail).child(roomName).child("schedule").child("1").setValue(schedule.get(0));
        myRef.child(hostEmail).child(roomName).child("chat").child("start").setValue("start");
        myRef.child(hostEmail).child(roomName).child("announce").child("start").setValue("start");

        ProjectRoom PR = new ProjectRoom(roomName, hostEmail, roomPW, schedule, teamMember, deadLine, startDay, name);

        FirebaseDatabase.getInstance().getReference("account").child(hostEmail).child("hostPR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean contain = false;
                ArrayList<ProjectRoom> list = new ArrayList<ProjectRoom>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String rname = postSnapshot.getKey().toString();
                    String hostEmail = account.getProfile().getEMail();
                    if(rname.equals(PR.getRoomName()))
                        if(hostEmail.equals(PR.getHostEmail()))
                            contain = true;
                    String pw = postSnapshot.child("pw").getValue().toString();
                    String deadLine = postSnapshot.child("dead line").getValue().toString();
                    String start = postSnapshot.child("start day").getValue().toString();

                    ProjectRoom project = new ProjectRoom(rname, hostEmail, pw, deadLine, start, name);
                    list.add(project);
                }
                if(!contain) {
                    list.add(PR);
                    Hashtable<String, String> pr = new Hashtable<String, String>();
                    pr.put("pw", roomPW);
                    pr.put("dead line", deadLine);
                    pr.put("start day", startDay);

                    database.getReference().child("account").child(hostEmail).child("hostPR").child(roomName).setValue(pr);
                }

                database.getReference().child("account").child(hostEmail).child("participate").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean contain = false;
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            if(postSnapshot.child(roomName).getKey()!=null)
                                if(postSnapshot.child(roomName).child("pw").getValue()!=null)
                                    contain = true;
                        }
                        if(!contain) {
                            Hashtable<String, String> pr = new Hashtable<String, String>();
                            pr.put("pw", roomPW);
                            pr.put("dead line", deadLine);
                            pr.put("start day", startDay);

                            database.getReference().child("account").child(hostEmail).child("participate").child(hostEmail).child(roomName).setValue(pr);
                        }
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

        return PR;
    }
    public void settingProfile(String name, String phoneNum, Boolean iso){
        //프로필 변경내용을 파이어베이스에 저장하는 메소드
        FirebaseDatabase.getInstance().getReference("account").child(account.getProfile().getEMail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                account.settingProfile(name, account.getProfile().getEMail(), phoneNum, iso);
                String eMail = account.getProfile().getEMail();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("account").child(eMail).child("pw").setValue(account.getPw());
                myRef.child("account").child(eMail).child("name").setValue(account.getProfile().getName());
                myRef.child("account").child(eMail).child("phoneNum").setValue(account.getProfile().getPhoneNum());
                myRef.child("account").child(eMail).child("iso").setValue(account.getProfile().getIsolation());

                FirebaseDatabase.getInstance().getReference("account").child(account.getProfile().getEMail()).child("participate").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            String hostEmail = postSnapshot.getKey().toString();
                            for(DataSnapshot nowSnapshot: postSnapshot.getChildren()){
                                String room = nowSnapshot.getKey().toString();
                                myRef.child("PR").child(hostEmail).child(room).child("teamMember").child(eMail).child("name").setValue(account.getProfile().getName());
                                myRef.child("PR").child(hostEmail).child(room).child("teamMember").child(eMail).child("phoneNum").setValue(account.getProfile().getPhoneNum());
                                myRef.child("PR").child(hostEmail).child(room).child("teamMember").child(eMail).child("isolation").setValue(account.getProfile().getIsolation());
                            }
                        }
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
    public void logout(){
        //로그아웃 메소드
        BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
        SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
        e.clear();
        e.commit();
        bt.finish();
    }
}
