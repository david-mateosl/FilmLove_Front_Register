package com.example.filmlove_front_register;

;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;


public class PrincipalActivity extends Activity implements IniciarPantallas.OnActivityResultListener {
    static final int REQUEST_CODE_IMAGE_PICK = 1;
    TextView cerrarSesion;
    TextView favoritos;
    TextView peliculas;
    TextView series;
    TextView generos;
    ImageView imagenPerfil;
    SearchView barraBusqueda;
    ViewFlipper v_fliper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        imagenPerfil = findViewById(R.id.profileImageView);
        cerrarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        peliculas = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);

        IniciarPantallas.setOnActivityResultListener(this);

        IniciarPantallas.menuFotoPerfil(imagenPerfil,PrincipalActivity.this);
        IniciarPantallas.cerrarSesion(cerrarSesion, PrincipalActivity.this);
        IniciarPantallas.favoritos(favoritos, PrincipalActivity.this);
        IniciarPantallas.peliculas(peliculas, PrincipalActivity.this);
        IniciarPantallas.series(series, PrincipalActivity.this);
        IniciarPantallas.generos(generos, PrincipalActivity.this);

        int images[] = {R.drawable.imagen1,R.drawable.imagen2,R.drawable.imagen3};
        v_fliper = findViewById(R.id.viewFlipper);

        for (int image: images){
            flipperImages(image);
        }
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(3000);
        v_fliper.setAutoStart(true);

        v_fliper.setInAnimation(this, android.R.anim.slide_in_left);
        v_fliper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    // Resto del código de la clase PrincipalActivity
    // ...

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IniciarPantallas.REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(imagenPerfil);
        }
    }
}
