package com.example.teamsup;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilesetActivity extends AppCompatActivity {
    //프로필 변경 화면 액티비티
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileset);

        account = (Account) getIntent().getSerializableExtra("account");

        TextView email = (TextView)findViewById(R.id.pset_email);
        EditText name = (EditText)findViewById(R.id.pset_name);
        EditText num = (EditText)findViewById(R.id.pset_num);
        CheckBox covid = (CheckBox)findViewById(R.id.pset_covid);

        email.setText(account.getProfile().getEMail());
        name.setText(account.getProfile().getName());
        num.setText(account.getProfile().getPhoneNum());
        if(account.getProfile().getIsolation())
            covid.setChecked(true);
        else
            covid.setChecked(false);

        Button set = (Button)findViewById(R.id.pset_button);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = name.getText().toString();
                String phoneNum = num.getText().toString();
                Boolean iso = false;
                if(covid.isChecked())
                    iso = true;

                Boolean finalIso = iso;
                //Account의 settingProfile로 현재 프로필 변경
                account.settingProfile(user_name, account.getProfile().getEMail(), phoneNum, iso);
                User user1 = new User(account);
                //User의 settingProfile로 파이어베이스에 저장
                user1.settingProfile(user_name, phoneNum, finalIso);

                BottomActivity bt = (BottomActivity) ((BottomActivity) BottomActivity.context_main)._BottomActivity;
                bt.finish();

                Intent in = new Intent(ProfilesetActivity.this, BottomActivity.class);
                in.putExtra("account", account);
                startActivity(in);
                finish();
            }
        });
        Button cancle = (Button)findViewById(R.id.pset_cancle);
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
    }
}
