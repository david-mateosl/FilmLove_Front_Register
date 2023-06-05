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
import com.squareup.picasso.Picasso;

import java.util.List;

import Callback.ProductionCallback;
import Modelo.Production;
import Modelo.Usuario;

public class ProducionesPorGeneroActivity extends Activity implements ProductionCallback, SearchView.OnQueryTextListener {

    private ListView listViewProductions;
    private ControladorProducion controladorProducion;
    private Usuario usuario;
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView series;
    private TextView generos;
    ImageView imagenLogo;
    SearchView barraBusqueda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produciones_por_genero);
        controladorProducion = new ControladorProducion();

        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        listViewProductions = findViewById(R.id.listaProducionesGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);
        imagenPerfil = findViewById(R.id.imagenPefilGeneros);
        iniciarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        series = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(this, usuario.getUsername(), Toast.LENGTH_SHORT).show();

        String nombreGenero = getIntent().getStringExtra("genero");
        System.out.println(nombreGenero);

        getProductionsByGenre(nombreGenero);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, ProducionesPorGeneroActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, ProducionesPorGeneroActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, ProducionesPorGeneroActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, ProducionesPorGeneroActivity.this, usuario);
        IniciarPantallas.series(series, ProducionesPorGeneroActivity.this, usuario);
        IniciarPantallas.generos(generos, ProducionesPorGeneroActivity.this, usuario);

        listViewProductions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Production production = (Production) parent.getItemAtPosition(position);
                System.out.println(production);
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
            TextView txtsinopsis = view.findViewById(R.id.txtSinopsis);

            Picasso.get().load(production.getMultimedia().getPath()).into(imageView);
            txtTituloProducion.setText(production.getTitulo());

            float rating = production.getRating_medio();
            updateVotosMedios(rating, txtValorancionMediaProducion);

            txtsinopsis.setText(production.getSinopsis());

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
}
