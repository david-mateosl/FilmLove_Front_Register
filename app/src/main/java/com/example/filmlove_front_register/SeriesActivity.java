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

import com.example.filmlove_front_register.Controlador.ControladorSeries;
import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Callback.SeriesCallback;
import Callback.ProductionCallback;
import Modelo.Serie;
import Modelo.Production;
import Modelo.Usuario;

public class SeriesActivity extends Activity {

    private ListView listView;
    private List<Serie> series;
    private Usuario usuario;
    ControladorProducion controladorProducion = new ControladorProducion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        listView = findViewById(R.id.listaSeries);
        series = new ArrayList<>();

        obtenerSeriesDesdeBaseDeDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerSeriesDesdeBaseDeDatos();
    }

    private void obtenerSeriesDesdeBaseDeDatos() {
        ControladorSeries controladorSeries = new ControladorSeries();
        controladorSeries.obtenerSerieAleatoria(new SeriesCallback() {
            @Override
            public void onSerieLoaded(Serie serie) {

            }

            @Override
            public void onSeriesLoaded(List<Serie> series) {
                if (!series.isEmpty()) {
                    Serie primeraSerie = series.get(0);
                    System.out.println(series);
                }

                SeriesActivity.this.series = series;

                SeriesAdapter adapter = new SeriesAdapter(series);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Serie serie = series.get(position);
                        buscarProduccionPorNombre(serie.getTitulo());
                    }
                });
            }

            @Override
            public void onSeriesLoadError() {
            }
        });
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(SeriesActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = SeriesActivity.this.getCurrentFocus();
                iniciarProduccion(view, production);
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

    public void iniciarProduccion(View view, Production production) {
        Intent intent = new Intent(SeriesActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario",usuario);
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
}
