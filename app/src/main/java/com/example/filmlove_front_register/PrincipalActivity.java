package com.example.filmlove_front_register;

import static com.example.filmlove_front_register.IniciarPantallas.REQUEST_CODE_IMAGE_PICK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.filmlove_front_register.Controlador.ControladorPeliculas;
import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.example.filmlove_front_register.Controlador.ControladorSeries;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    ImageView imagenLogo;
    Usuario usuario;
    ControladorPeliculas controladorPeliculas = new ControladorPeliculas();
    ControladorSeries controladorSeries = new ControladorSeries();
    ControladorProducion controladorProducion = new ControladorProducion();
    UsuarioDAO usuarioDAO;
    byte[] imagenPerfilBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        System.out.println(usuario);

        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        imagenPerfil = findViewById(R.id.imagenPerfil);
        cerrarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        peliculas = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

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

        cargarPeliculasAleatorias();
        cargarSerieAleatoria();
        cargarProduccionesAleatorias();

        IniciarPantallas.menuFotoPerfil(imagenPerfil, PrincipalActivity.this);
        IniciarPantallas.cerrarSesion(cerrarSesion, PrincipalActivity.this);
        IniciarPantallas.favoritos(favoritos, PrincipalActivity.this, usuario);
        IniciarPantallas.peliculas(peliculas, PrincipalActivity.this, usuario);
        IniciarPantallas.series(series, PrincipalActivity.this, usuario);
        IniciarPantallas.generos(generos, PrincipalActivity.this, usuario);

        desplegarMenu();

        usuarioDAO = new UsuarioDAO(this);

        if (usuario != null) {
            cargarDatosUsuario();
        }

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la galería de imágenes para seleccionar una foto de perfil
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        usuarioDAO = new UsuarioDAO(this);

        if (usuario != null) {
            cargarDatosUsuario();
        }
    }

    private void cargarDatosUsuario() {
        byte[] imagenPerfilBytes = usuarioDAO.obtenerImagenPerfil(usuario.getUsername());
        if (imagenPerfilBytes != null) {
            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenPerfilBytes, 0, imagenPerfilBytes.length);
            imagenPerfil.setImageBitmap(imagenBitmap);
        } else {
            imagenPerfil.setImageResource(R.drawable.foto_perfil_por_defecto);
        }
    }


    private void guardarImagenPerfil(Uri uri, byte[] imagenPerfilBytes) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imagenPerfil.setImageBitmap(bitmap);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imagenBytes = outputStream.toByteArray();

            usuarioDAO.guardarImagenPerfil(usuario.getUsername(), imagenBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imagenBytes = outputStream.toByteArray();

                guardarImagenPerfil(selectedImageUri, imagenBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void desplegarMenu(){
        imagenLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
