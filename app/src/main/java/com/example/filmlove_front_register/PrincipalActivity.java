package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class PrincipalActivity extends Activity {

    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    TextView cerrarSesion;
    TextView favoritos;
    TextView peliculas;
    TextView series;
    TextView generos;
    ImageView imagenPerfil;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        cerrarSesion = findViewById(R.id.menuitem_cerrarsesion);
        favoritos = findViewById(R.id.menuitem_favoritos);
        peliculas = findViewById(R.id.menuitem_peliculas);
        series = findViewById(R.id.menuitem_series);
        generos = findViewById(R.id.menuitem_Generos);
        imagenPerfil = findViewById(R.id.profileImageView);

        cerrar_sesion(view);
        favoritos(view);
        peliculas(view);
        series(view);
        generos(view);

        menuFotoPerfil(view);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop() // Redondea la imagen
                    .into(imagenPerfil);
        }
    }

    public void selectImageFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }

    public void menuFotoPerfil(View view){
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí abres la actividad para seleccionar una imagen
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });
    }
    public void cerrar_sesion(View view){
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(PrincipalActivity.this, LoginActivity.class);
                startActivity(iLogin);

            }
        });
    }

    public void favoritos(View view) {
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iFavoritos = new Intent(PrincipalActivity.this, FavoritoActivity.class);
                startActivity(iFavoritos);
            }
        });
    }

    public void peliculas(View view) {
        peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPeliculas = new Intent(PrincipalActivity.this, PeliculaActivity.class);
                startActivity(iPeliculas);
            }
        });
    }

    public void series(View view) {
        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSeries = new Intent(PrincipalActivity.this, SerieActivity.class);
                startActivity(iSeries);
                finish();
            }
        });
    }

    public void generos(View view) {
        generos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGeneros = new Intent(PrincipalActivity.this, GeneroActivity.class);
                startActivity(iGeneros);
            }
        });
    }
}