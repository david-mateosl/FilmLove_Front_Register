package com.example.filmlove_front_register;

;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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

        IniciarPantallas.menuFotoPerfil(imagenPerfil);
        IniciarPantallas.cerrarSesion(cerrarSesion, PrincipalActivity.this);
        IniciarPantallas.favoritos(favoritos, PrincipalActivity.this);
        IniciarPantallas.peliculas(peliculas, PrincipalActivity.this);
        IniciarPantallas.series(series, PrincipalActivity.this);
        IniciarPantallas.generos(generos, PrincipalActivity.this);
    }

    // Resto del código de la clase PrincipalActivity
    // ...

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(imagenPerfil);
        }
    }
}
