package com.example.teamsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    //로그인 화면 액티비티
    private Account user;
    private String eMail;
    public static Context context_Login;
    public Activity _LoginActivity;
    public SharedPreferences auto;
    CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_Login = this;
        setContentView(R.layout.activity_login);

        _LoginActivity = LoginActivity.this;

        auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        String log_id = auto.getString("id", null);
        String log_pw = auto.getString("pw", null);

        EditText id = (EditText)findViewById(R.id.log_email);
        EditText pw = (EditText)findViewById(R.id.log_password);
        autoLogin = (CheckBox)findViewById(R.id.log_checkbox);

        Button login = (Button)findViewById(R.id.log_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String E_Mail = id.getText().toString();
                eMail = E_Mail.replaceAll("[.]", ",");
                String passwd = pw.getText().toString();
                if (eMail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Insert E-Mail", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Insert password", Toast.LENGTH_LONG).show();
                    return;
                }

                Login login_object = new Login(eMail, passwd);

                if(autoLogin.isChecked()) {
                    SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
                    e.clear();
                    e.commit();
                    login_object.autoLogin();
                }
                user = login_object.login();

            }
        });
        Button mem = (Button)findViewById(R.id.log_mem);
        mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, MembershipActivity.class);
                startActivity(in);
            }
        });
        if(log_id != null && log_pw != null){
            id.setText(log_id);
            pw.setText(log_pw);
            autoLogin.setChecked(true);
            login.performClick();
        }
    }

}
