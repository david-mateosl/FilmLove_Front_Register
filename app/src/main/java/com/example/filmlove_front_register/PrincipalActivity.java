package com.example.filmlove_front_register;

;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import Modelo.Usuario;


public class PrincipalActivity extends Activity{
    static final int REQUEST_CODE_IMAGE_PICK = 1;
    TextView cerrarSesion;
    TextView favoritos;
    TextView peliculas;
    TextView series;
    TextView generos;
    ImageView imagenPerfil;
    SearchView barraBusqueda;
    ViewFlipper v_fliper;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Obtener el objeto usuario de la actividad anterior
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        // Ahora puedes utilizar el objeto usuario en esta actividad
        if (usuario != null) {
            Toast.makeText(this, usuario.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error inesperado en el inicio de sesion", Toast.LENGTH_SHORT).show();
        }

        imagenPerfil = findViewById(R.id.imagenPerfil);
        cerrarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        peliculas = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);

        IniciarPantallas.menuFotoPerfil(imagenPerfil,PrincipalActivity.this);
        IniciarPantallas.cerrarSesion(cerrarSesion, PrincipalActivity.this);
        IniciarPantallas.favoritos(favoritos, PrincipalActivity.this);
        IniciarPantallas.peliculas(peliculas, PrincipalActivity.this);
        IniciarPantallas.series(series, PrincipalActivity.this);
        IniciarPantallas.generos(generos, PrincipalActivity.this);

        int images[] = {R.drawable.imagen1,R.drawable.imagen2,R.drawable.imagen3};
        v_fliper = findViewById(R.id.galeriaProducciones);

        for (int image: images){
            flipperImages(image);
        }
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        // Asignar una etiqueta única a cada imagen
        imageView.setTag(image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageTag = (int) v.getTag();

                if (imageTag == R.drawable.imagen1) {
                    // Acción a realizar cuando se hace clic en la imagen 1
                    Toast.makeText(PrincipalActivity.this, "Ha pulsado la imagen 1", Toast.LENGTH_SHORT).show();
                    Intent iProduccion = new Intent(PrincipalActivity.this, ProduccionActivity.class);
                    startActivity(iProduccion);
                } else if (imageTag == R.drawable.imagen2) {
                    // Acción a realizar cuando se hace clic en la imagen 2
                    Toast.makeText(PrincipalActivity.this, "Ha pulsado la imagen 2", Toast.LENGTH_SHORT).show();
                    Intent iProduccion = new Intent(PrincipalActivity.this,ProduccionActivity.class);
                    startActivity(iProduccion);
                } else if (imageTag == R.drawable.imagen3) {
                    // Acción a realizar cuando se hace clic en la imagen 3
                    Toast.makeText(PrincipalActivity.this, "Ha pulsado la imagen 3", Toast.LENGTH_SHORT).show();
                    Intent iProduccion = new Intent(PrincipalActivity.this, ProduccionActivity.class);
                    startActivity(iProduccion);
                }
            }
        });

        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(3000);
        v_fliper.setAutoStart(true);

        v_fliper.setInAnimation(this, android.R.anim.slide_in_left);
        v_fliper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    public void iniciarProduccion(View view){
        Intent iProduccion = new Intent(this, ProduccionActivity.class);
        startActivity(iProduccion);
    }

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
