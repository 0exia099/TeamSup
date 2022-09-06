package com.example.teamsup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class JoinprojectActivity extends AppCompatActivity {
    //프로젝트 방에 입장하는 화면의 액티비티
    User user;
    Account account;
    String roomN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinproject);

        account = (Account) getIntent().getSerializableExtra("account");
        user = (User) getIntent().getSerializableExtra("user");
        roomN = (String) getIntent().getSerializableExtra("room");

        String roomName = roomN.substring(0,roomN.lastIndexOf("("));
        String roomHost = roomN.substring(roomN.lastIndexOf("(")+1,roomN.lastIndexOf(")"));

        TextView room = (TextView)findViewById(R.id.jp_room);
        EditText pw = (EditText)findViewById(R.id.jp_pw);

        room.setText(roomN);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Button join = (Button)findViewById(R.id.jp_button);
        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                myRef.child("PR").child(roomHost).child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String rpw = dataSnapshot.child("pw").getValue().toString();
                        if(pw.getText().toString().equals(rpw)){
                            String deadLine = dataSnapshot.child("dead line").getValue().toString();
                            String startDay = dataSnapshot.child("start day").getValue().toString();
                            String name = account.getProfile().getName();
                            ProjectRoom pr = new ProjectRoom(roomName, roomHost, rpw, deadLine, startDay, name);

                            //비밀번호가 일치하면 User의 joinPR메소드를 통해 프로젝트방에 입장.
                            user.joinPR(account.getProfile(), pr);

                            //해당 User가 입장된 프로젝트 방으로 변경
                            pr = new ProjectRoom(roomName, roomHost, rpw, deadLine, startDay, name);

                            ProjectRoom finalPr = pr;
                            myRef.child("account").child(account.getProfile().getEMail()).child("participate").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Boolean contain = false;
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.child(roomName).getKey()!=null) {
                                            if(postSnapshot.child(roomName).child("pw").getValue()!=null)
                                            contain = true;
                                        }
                                    }
                                    if (!contain) {
                                        String email = account.getProfile().getEMail();
                                        Hashtable<String, String> project = new Hashtable<String, String>();
                                        project.put("pw", rpw);
                                        project.put("dead line", deadLine);
                                        project.put("start day", startDay);

                                        database.getReference().child("account").child(email).child("participate").child(finalPr.getHostEmail()).child(roomName).setValue(project);
                                    }


                                    myRef.child("account").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String eMail = account.getProfile().getEMail().toString();
                                            account.getProfile().setName(dataSnapshot.child(eMail).child("name").getValue().toString());
                                            account.getProfile().setEMail(eMail);
                                            account.getProfile().setPhoneNum(dataSnapshot.child(eMail).child("phoneNum").getValue().toString());
                                            account.getProfile().setIsolation((Boolean)dataSnapshot.child(eMail).child("iso").getValue());

                                            BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
                                            bt.finish();

                                            Intent in = new Intent(JoinprojectActivity.this, BottomActivity.class);
                                            in.putExtra("account", account);
                                            startActivity(in);

                                            Intent in2 = new Intent(JoinprojectActivity.this, MainActivity.class);
                                            in2.putExtra("account", account);
                                            in2.putExtra("pr", finalPr);
                                            startActivity(in2);

                                            finish();
                                            return;
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
                        else{
                            Toast.makeText(JoinprojectActivity.this,"비밀번호를 확인해 주세요.",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("app", "Failed to read value.", error.toException());
                    }
                });
            }
        });
        Button cancle = (Button)findViewById(R.id.jp_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        user = null;
        account = null;
        roomN = null;
    }
}
