package com.example.filmlove_front_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ViewFlipper;


public class ProduccionActivity extends Activity {

    ViewFlipper v_fliper;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produccion);

        ratingBar = findViewById(R.id.barraDeBotacion);
        v_fliper = findViewById(R.id.galeriaProduccion);

        int images[] = {R.drawable.imagen1,R.drawable.imagen2,R.drawable.imagen3};

        for (int image: images){
            cargarImagenesProduccion(image);
        }


        votar(ratingBar);

    }

    public void votar (RatingBar ratingBar){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProduccionActivity.this);
                dialogBuilder.setTitle("Comentario");

                final EditText editText = new EditText(ProduccionActivity.this);
                dialogBuilder.setView(editText);

                dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String comment = editText.getText().toString();
                        // Aquí puedes realizar acciones con el comentario ingresado
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    public void cargarImagenesProduccion(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(3000);
        v_fliper.setAutoStart(true);

        v_fliper.setInAnimation(this, android.R.anim.slide_in_left);
        v_fliper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    public void inicio(View view){
        Intent iInicio = new Intent(this,PrincipalActivity.class);
        startActivity(iInicio);
    }

}