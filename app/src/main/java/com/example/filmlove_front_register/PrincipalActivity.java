package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.filmlove_front_register.Controlador.ControladorPeliculas;
import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.example.filmlove_front_register.Controlador.ControladorSeries;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import Callback.FilmsCallback;
import Callback.ProductionCallback;
import Callback.SeriesCallback;
import Modelo.Pelicula;
import Modelo.Production;
import Modelo.Serie;
import Modelo.Usuario;

public class PrincipalActivity extends Activity implements SearchView.OnQueryTextListener {
    TextView cerrarSesion;
    TextView favoritos;
    TextView peliculas;
    TextView series;
    TextView generos;
    ImageView imagenPerfil;
    SearchView barraBusqueda;
    ViewFlipper v_fliper;
    ImageView imagenPeliUno;
    ImageView imagenPeliDos;
    ImageView imagenPeliTres;
    TextView textoPeliUno;
    TextView textoPeliDos;
    TextView textoPeliTres;
    TextView textoSerie;
    ImageView imagenSerie;
    TextView sinopsisSerie;
    Usuario usuario;
    ControladorPeliculas controladorPeliculas = new ControladorPeliculas();
    ControladorSeries controladorSeries = new ControladorSeries();
    ControladorProducion controladorProducion = new ControladorProducion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        System.out.println(usuario);
        barraBusqueda = findViewById(R.id.barraDeBusqueda);

        imagenPerfil = findViewById(R.id.imagenPerfil);
        cerrarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        peliculas = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);

        textoPeliUno = findViewById(R.id.tituloPeliUno);
        textoPeliDos = findViewById(R.id.tituloPeliDos);
        textoPeliTres = findViewById(R.id.tituloPeliTres);

        imagenPeliUno = findViewById(R.id.imagenPeliUno);
        imagenPeliDos = findViewById(R.id.imagenPeliDos);
        imagenPeliTres = findViewById(R.id.imagenPeliTres);

        textoSerie = findViewById(R.id.titutloSerie);
        imagenSerie = findViewById(R.id.imagenSerie);
        sinopsisSerie = findViewById(R.id.sinopsisSerie);

        v_fliper = findViewById(R.id.galeriaProducciones);

        barraBusqueda.setOnQueryTextListener(this);

        cargarPeliculasAleatorias();
        cargarSerieAleatoria();
        cargarProduccionesAleatorias();

        IniciarPantallas.menuFotoPerfil(imagenPerfil, PrincipalActivity.this);
        IniciarPantallas.cerrarSesion(cerrarSesion, PrincipalActivity.this);
        IniciarPantallas.favoritos(favoritos, PrincipalActivity.this, usuario);
        IniciarPantallas.peliculas(peliculas, PrincipalActivity.this, usuario);
        IniciarPantallas.series(series, PrincipalActivity.this, usuario);
        IniciarPantallas.generos(generos, PrincipalActivity.this, usuario);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        buscarProduccionPorNombre(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(PrincipalActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = PrincipalActivity.this.getCurrentFocus();
                iniciarProduccion(view,production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(PrincipalActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarProduccionesAleatorias() {
        controladorPeliculas.obtenerProducionesAleatorias(new FilmsCallback() {
            @Override
            public void onFilmsLoaded(List<Pelicula> films) {
                String images[] = {films.get(0).getRutaImagen(), films.get(1).getRutaImagen(), films.get(2).getRutaImagen()};
                v_fliper = findViewById(R.id.galeriaProducciones);

                for (int i = 0; i < images.length; i++) {
                    ImageView imageView = new ImageView(PrincipalActivity.this);
                    Picasso.get().load(images[i]).into(imageView);
                    v_fliper.addView(imageView);

                    final Pelicula production = films.get(i);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buscarProduccionPorNombre(production.getTitulo());
                        }
                    });
                }

                v_fliper.setFlipInterval(3000); // Intervalo de cambio de imágenes en milisegundos
                v_fliper.setAutoStart(true); // Iniciar la reproducción automática

                // Configurar animaciones de entrada y salida
                v_fliper.setInAnimation(PrincipalActivity.this, android.R.anim.slide_in_left);
                v_fliper.setOutAnimation(PrincipalActivity.this, android.R.anim.slide_out_right);
            }

            @Override
            public void onFilmsLoadError() {
                Toast.makeText(PrincipalActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarPeliculasAleatorias() {
        controladorPeliculas.obtenerPeliculasAleatorias(new FilmsCallback() {
            @Override
            public void onFilmsLoaded(List<Pelicula> films) {
                ImageView[] imageViews = {imagenPeliUno, imagenPeliDos, imagenPeliTres};
                TextView[] textViews = {textoPeliUno, textoPeliDos, textoPeliTres};

                for (int i = 0; i < films.size() && i < imageViews.length; i++) {
                    String rutaImagen = films.get(i).getRutaImagen();
                    cargarImagenConGlide(rutaImagen, imageViews[i]);
                    String titulo = films.get(i).getTitulo();
                    if (titulo.length() > 16) {
                        StringBuilder sb = new StringBuilder(titulo);
                        int insertPosition = 16;
                        while (insertPosition < sb.length()) {
                            sb.insert(insertPosition, "\n");
                            insertPosition += 17;
                        }
                        titulo = sb.toString();
                    }
                    textViews[i].setText(titulo);

                    // Agregar OnClickListener a cada ImageView
                    final Pelicula pelicula = films.get(i);
                    imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buscarProduccionPorNombre(pelicula.getTitulo());
                        }
                    });
                }
            }

            @Override
            public void onFilmsLoadError() {
                Toast.makeText(PrincipalActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarImagenConGlide(String rutaImagen, ImageView imageView) {
        Glide.with(this)
                .load(rutaImagen)
                .into(imageView);
    }

    public void cargarSerieAleatoria() {
        controladorSeries.obtenerSerieAleatoria(new SeriesCallback() {
            @Override
            public void onSerieLoaded(Serie serie) {
                textoSerie.setText(serie.getTitulo());
                Picasso.get().load(serie.getRutaImagen()).into(imagenSerie);
                sinopsisSerie.setText(serie.getSinopsis());
                imagenSerie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buscarProduccionPorNombre(serie.getTitulo());
                    }
                });
            }

            @Override
            public void onSeriesLoaded(List<Serie> series) {

            }

            @Override
            public void onSeriesLoadError() {
                Toast.makeText(PrincipalActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void iniciarProduccion(View view,Production production) {
        Intent intent = new Intent(PrincipalActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
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
