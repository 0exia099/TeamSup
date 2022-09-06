package com.example.teamsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamsup.btui.ProfileFragment;
import com.example.teamsup.btui.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public static Context context_main;
    public Account account;
    public User user;
    public Activity _BottomActivity;
    public View searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        _BottomActivity = BottomActivity.this;

        account = (Account) getIntent().getSerializableExtra("account");
        user = new User(account);

        context_main = this;

        bottomNavigationView = findViewById(R.id.bottomNav);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new ProfileFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_Profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ProfileFragment()).commit();
                        break;
                    case R.id.navigation_SearchProjectRoom:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SearchFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            finish();
            Log.d("app", "finish");
            return true;
        }

        return false;
    }
}
