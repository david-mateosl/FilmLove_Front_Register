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
import com.squareup.picasso.Picasso;

import java.util.List;

import Callback.ProductionCallback;
import Modelo.Production;
import Modelo.Usuario;

public class ProducionesPorGeneroActivity extends Activity implements ProductionCallback {

    private ListView listViewProductions;
    private ControladorProducion controladorProducion;
    private Usuario usuario;
    ImageView imagenLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produciones_por_genero);
        controladorProducion = new ControladorProducion();

        listViewProductions = findViewById(R.id.listaProducionesGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(this, usuario.getUsername(), Toast.LENGTH_SHORT).show();

        String nombreGenero = getIntent().getStringExtra("genero");
        System.out.println(nombreGenero);

        getProductionsByGenre(nombreGenero);

        listViewProductions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Production production = (Production) parent.getItemAtPosition(position);
                System.out.println(production);
            }
        });
        desplegarMenu();
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(ProducionesPorGeneroActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = ProducionesPorGeneroActivity.this.getCurrentFocus();
                iniciarProduccion(view,production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(ProducionesPorGeneroActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void iniciarProduccion(View view,Production production) {
        Intent intent = new Intent(ProducionesPorGeneroActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private void getProductionsByGenre(String genre) {
        controladorProducion.getProductionsByGenre(genre, this);
    }

    @Override
    public void onProductionSuccess(Production production) {
        Toast.makeText(this, production.getTitulo(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductionListLoaded(List<Production> productions) {
        ProductionAdapter adapter = new ProductionAdapter(productions);
        listViewProductions.setAdapter(adapter);
    }

    @Override
    public void onProductionFailure() {
        Toast.makeText(this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
    }

    public void iniciarProduccion(Production production) {
        Intent intent = new Intent(ProducionesPorGeneroActivity.this, ProduccionActivity.class);
        intent.putExtra("production", production);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private class ProductionAdapter extends ArrayAdapter<Production> {

        public ProductionAdapter(List<Production> productions) {
            super(ProducionesPorGeneroActivity.this, R.layout.formato_listview, productions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.formato_listview, parent, false);
            }

            Production production = getItem(position);

            ImageView imageView = view.findViewById(R.id.imagenDeLaProducion);
            TextView txtTituloProducion = view.findViewById(R.id.tituloDeProducion);
            TextView txtValorancionMediaProducion = view.findViewById(R.id.valorancionMediaDeProducion);

            Picasso.get().load(production.getMultimedia().getPath()).into(imageView);
            txtTituloProducion.setText(production.getTitulo());
            txtValorancionMediaProducion.setText(String.valueOf(production.getRating_medio()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Production production = getItem(position);
                    buscarProduccionPorNombre(production.getTitulo());
                }
            });

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
