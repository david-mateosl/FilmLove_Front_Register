package com.example.filmlove_front_register;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.drawerlayout.widget.DrawerLayout;

public class PrincipalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void openMenu(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        RelativeLayout drawer = findViewById(R.id.drawer);
        drawerLayout.openDrawer(drawer);
    }
}
