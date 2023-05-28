package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmlove_front_register.Controlador.ControladorPeliculas;
import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.example.filmlove_front_register.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Callback.FilmsCallback;
import Callback.ProductionCallback;
import Modelo.Pelicula;
import Modelo.Production;
import Modelo.Usuario;

public class PeliculasActivity extends Activity {

    private ListView listView;
    private List<Pelicula> peliculas;
    private ControladorProducion controladorProducion = new ControladorProducion();
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView series;
    private TextView generos;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(this, usuario.getUsername(), Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.listaPeliculas);
        peliculas = new ArrayList<>();

        imagenPerfil = findViewById(R.id.imagenPerfil);
        iniciarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, PeliculasActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, PeliculasActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, PeliculasActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, PeliculasActivity.this, usuario);
        IniciarPantallas.series(series, PeliculasActivity.this, usuario);
        IniciarPantallas.generos(generos, PeliculasActivity.this, usuario);

        obtenerPeliculasDesdeBaseDeDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerPeliculasDesdeBaseDeDatos();
    }

    private void obtenerPeliculasDesdeBaseDeDatos() {
        ControladorPeliculas controladorPeliculas = new ControladorPeliculas();
        controladorPeliculas.mostrarTodasLasPelis(new FilmsCallback() {
            @Override
            public void onFilmsLoaded(List<Pelicula> peliculas) {
                if (!peliculas.isEmpty()) {
                    Pelicula primeraPelicula = peliculas.get(0);

                }

                PeliculasActivity.this.peliculas = peliculas;

                PeliculasAdapter adapter = new PeliculasAdapter(peliculas);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Pelicula pelicula = peliculas.get(position);
                        buscarProduccionPorNombre(pelicula.getTitulo());
                    }
                });
            }

            @Override
            public void onFilmsLoadError() {
            }
        });


    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(PeliculasActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = PeliculasActivity.this.getCurrentFocus();
                iniciarProduccion(view,production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(PeliculasActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void iniciarProduccion(View view,Production production) {
        Intent intent = new Intent(PeliculasActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }




    private class PeliculasAdapter extends ArrayAdapter<Pelicula> {

        public PeliculasAdapter(List<Pelicula> peliculas) {
            super(PeliculasActivity.this, R.layout.formato_listview, peliculas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.formato_listview, parent, false);
            }

            Pelicula pelicula = getItem(position);

            ImageView imageView = view.findViewById(R.id.imagenDeLaProducion);
            TextView txtTituloProducion = view.findViewById(R.id.tituloDeProducion);
            TextView txtValorancionMediaProducion = view.findViewById(R.id.valorancionMediaDeProducion);

            Picasso.get().load(pelicula.getRutaImagen()).into(imageView);
            txtTituloProducion.setText(pelicula.getTitulo());
            txtValorancionMediaProducion.setText(String.valueOf(pelicula.getMedia_votos()));

            return view;
        }
    }
}
