package com.example.teamsup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CreateprActivity extends AppCompatActivity {
    //프로젝트 방을 만드는 화면의 액티비티
    public static Context context_create;
    Account account;
    User user;
    public String room_name;
    public String passwd;
    public String DeadLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpr);
        Intent in = getIntent();
        account = (Account) getIntent().getSerializableExtra("account");
        user = (User) getIntent().getSerializableExtra("user");
        context_create = this;
        EditText name = (EditText)findViewById(R.id.cpr_name);
        EditText pw = (EditText)findViewById(R.id.cpr_pw);
        EditText deadLine = (EditText)findViewById(R.id.cpr_deadLine);

        Button create = (Button)findViewById(R.id.cpr_button);
        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                room_name = name.getText().toString();
                passwd = pw.getText().toString();
                DeadLine = deadLine.getText().toString();

                //User의 createPR메소드를 사용해 ProjectRoom객체 생성
                ProjectRoom pr = user.createPR(account);

                BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
                bt.finish();

                Intent in = new Intent(CreateprActivity.this, BottomActivity.class);
                in.putExtra("account", account);
                startActivity(in);

                Intent in2 = new Intent(CreateprActivity.this, MainActivity.class);
                in2.putExtra("account", account);
                in2.putExtra("pr", pr);
                startActivity(in2);
                finish();
                return;
            }
        });
        Button cancle = (Button)findViewById(R.id.cpr_cancle);
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
}
