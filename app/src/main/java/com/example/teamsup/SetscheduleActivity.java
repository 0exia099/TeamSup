package com.example.teamsup;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class SetscheduleActivity extends AppCompatActivity {
    //일정 설정화면 액티비티
    Account account;
    User user;
    ProjectRoom pr;
    public static Context setSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setschedule);

        account = (Account) getIntent().getSerializableExtra("account");
        user = (User) getIntent().getSerializableExtra("user");
        pr = (ProjectRoom) getIntent().getSerializableExtra("pr");

        setSchedule = this;

        EditText work[] = new EditText[7];
        work[0] = (EditText)findViewById(R.id.sch_1);
        work[1] = (EditText)findViewById(R.id.sch_2);
        work[2] = (EditText)findViewById(R.id.sch_3);
        work[3] = (EditText)findViewById(R.id.sch_4);
        work[4] = (EditText)findViewById(R.id.sch_5);
        work[5] = (EditText)findViewById(R.id.sch_6);
        work[6] = (EditText)findViewById(R.id.sch_7);

        FirebaseDatabase.getInstance().getReference("PR").child(pr.getHostEmail()).child(pr.getRoomName()).child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String msg = postSnapshot.getValue().toString();
                    work[i++].setText(msg);
                    if(i==7)
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("app", "Failed to read value.", error.toException());
            }
        });

        Button set = (Button)findViewById(R.id.sch_button);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinkedList<String> schedule = new LinkedList<String>();
                for(int i = 0;i<7;i++){
                    schedule.add(work[i].getText().toString());
                }

                pr.getSchedule().setSchedule(schedule);

                finish();
            }
        });
        Button cancle = (Button)findViewById(R.id.sch_cancle);
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
        account = null;
        user = null;
        pr = null;
    }
}
