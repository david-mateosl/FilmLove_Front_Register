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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.example.filmlove_front_register.Controlador.ControladorSeries;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Callback.ProductionCallback;
import Callback.SeriesCallback;
import Modelo.Pelicula;
import Modelo.Production;
import Modelo.Serie;
import Modelo.Usuario;

public class SeriesActivity extends Activity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private List<Serie> series;
    private Usuario usuario;
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView seriesBoton;
    private TextView generos;
    private ControladorProducion controladorProducion;
    ImageView imagenLogo;
    SearchView barraBusqueda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        controladorProducion = new ControladorProducion();
        imagenLogo = findViewById(R.id.imagenLogo);

        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        imagenPerfil = findViewById(R.id.imagenPerfilS);
        iniciarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        seriesBoton = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, SeriesActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, SeriesActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, SeriesActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, SeriesActivity.this, usuario);
        IniciarPantallas.series(seriesBoton, SeriesActivity.this, usuario);
        IniciarPantallas.generos(generos, SeriesActivity.this, usuario);

        listView = findViewById(R.id.listaSeries);
        series = new ArrayList<>();

        obtenerSeriesDesdeBaseDeDatos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Serie serie = series.get(position);
                buscarProduccionPorNombre(serie.getTitulo());
            }
        });

        desplegarMenu();
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

    @Override
    protected void onResume() {
        super.onResume();
        obtenerSeriesDesdeBaseDeDatos();
    }

    private void obtenerSeriesDesdeBaseDeDatos() {
        ControladorSeries controladorSeries = new ControladorSeries();
        controladorSeries.mostrarTodasLasSeries(new SeriesCallback() {
            @Override
            public void onSerieLoaded(Serie serie) {

            }

            @Override
            public void onSeriesLoaded(List<Serie> series) {
                if (!series.isEmpty()) {
                    SeriesActivity.this.series = series;
                    System.out.println(series);
                    SeriesAdapter adapter = new SeriesAdapter(series);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onSeriesLoadError() {
                Toast.makeText(SeriesActivity.this, "Error al cargar las series", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(SeriesActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                iniciarProduccion(production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(SeriesActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void iniciarProduccion(Production production) {
        Intent intent = new Intent(SeriesActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private class SeriesAdapter extends ArrayAdapter<Serie> {

        public SeriesAdapter(List<Serie> series) {
            super(SeriesActivity.this, R.layout.formato_listview, series);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.formato_listview, parent, false);
            }

            Serie serie = getItem(position);

            ImageView imageView = view.findViewById(R.id.imagenDeLaProducion);
            TextView txtTituloProducion = view.findViewById(R.id.tituloDeProducion);
            TextView txtValorancionMediaProducion = view.findViewById(R.id.valorancionMediaDeProducion);
            TextView txtsinopsis = view.findViewById(R.id.txtSinopsis);

            Picasso.get().load(serie.getRutaImagen()).into(imageView);
            txtTituloProducion.setText(serie.getTitulo());

            float rating = serie.getMedia_votos();
            updateVotosMedios(rating, txtValorancionMediaProducion);

            txtsinopsis.setText(String.valueOf(serie.getSinopsis()));

            return view;
        }
    }

    private void updateVotosMedios(float ratingMedio, TextView txtValorancionMediaProducion) {
        int roundedRating = Math.round(ratingMedio);
        txtValorancionMediaProducion.setText(String.valueOf(roundedRating));

        int colorId;
        switch (roundedRating) {
            case 0:
                colorId = R.drawable.fondo_votos_medios_1;
                break;
            case 1:
                colorId = R.drawable.fondo_votos_medios_1;
                break;
            case 2:
                colorId = R.drawable.fondo_votos_medios_2;
                break;
            case 3:
                colorId = R.drawable.fondo_votos_medios_3;
                break;
            case 4:
                colorId = R.drawable.fondo_votos_medios_4;
                break;
            default:
                colorId = R.drawable.fondo_votos_medios_5;
                break;
        }

        txtValorancionMediaProducion.setBackgroundResource(colorId);
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
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivity(i);
        i.putExtra("usuario",usuario);
        finish();
    }
}
