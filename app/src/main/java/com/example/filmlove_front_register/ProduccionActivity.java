package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class ProduccionActivity extends Activity {

    ViewFlipper v_fliper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produccion);

        int images[] = {R.drawable.imagen1,R.drawable.imagen2,R.drawable.imagen3};
        v_fliper = findViewById(R.id.galeriaProduccion);

        for (int image: images){
            flipperImages(image);
        }
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(3000);
        v_fliper.setAutoStart(true);

        v_fliper.setInAnimation(this, android.R.anim.slide_in_left);
        v_fliper.setOutAnimation(this, android.R.anim.slide_out_right);

    }
}