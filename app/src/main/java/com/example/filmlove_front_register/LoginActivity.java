package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class LoginActivity extends Activity {

    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View vista){
        Intent iLogin = new Intent(this, PrincipalActivity.class);
        finish();
        startActivity(iLogin);
    }

    public void registro(View view){
        Intent iRegistro = new Intent(this, RegisterActivity.class);
        startActivity(iRegistro);
        finish();
    }
}