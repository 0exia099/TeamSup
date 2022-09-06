package com.example.teamsup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    //시작시 아이콘 띄우는 액티비티
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.start,R.anim.none);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.none, R.anim.end);
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.end);
            }
        }, 2000);

    }
}
