package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class RegisterActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void volver(View view){
        Intent intentVolver = new Intent(this, LoginActivity.class);
        startActivity(intentVolver);
        finish();
    }
}