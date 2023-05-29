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

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.example.filmlove_front_register.Controlador.ControladorSeries;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Callback.ProductionCallback;
import Callback.SeriesCallback;
import Modelo.Production;
import Modelo.Serie;
import Modelo.Usuario;

public class SeriesActivity extends Activity {

    private ListView listView;
    private List<Serie> series;
    private Usuario usuario;
    private ControladorProducion controladorProducion;
    ImageView imagenLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        controladorProducion = new ControladorProducion();
        imagenLogo = findViewById(R.id.imagenLogo);

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
                // No es necesario implementar este método para la actividad de series
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

            Picasso.get().load(serie.getRutaImagen()).into(imageView);
            txtTituloProducion.setText(serie.getTitulo());
            txtValorancionMediaProducion.setText(String.valueOf(serie.getMedia_votos()));

            return view;
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
}
