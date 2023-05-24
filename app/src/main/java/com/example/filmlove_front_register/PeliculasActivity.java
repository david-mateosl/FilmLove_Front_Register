package com.example.filmlove_front_register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Modelo.Pelicula;

public class PeliculasActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        listView = findViewById(R.id.listaPeliculas);

        // Obtén la lista de películas desde tu base de datos o cualquier otra fuente de datos
        List<Pelicula> peliculas = obtenerPeliculasDesdeBaseDeDatos();

        // Crea una instancia de un adaptador personalizado y asígnala al ListView
        PeliculasAdapter adapter = new PeliculasAdapter(this, peliculas);
        listView.setAdapter(adapter);
    }

    private List<Pelicula> obtenerPeliculasDesdeBaseDeDatos() {
        // Aquí puedes implementar la lógica para obtener las películas desde tu base de datos
        // o cualquier otra fuente de datos y retornar una lista de objetos Pelicula
        List<Pelicula> peliculas = new ArrayList<>();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date premiere1 = dateFormat.parse("2003-10-11");
            Date premiere2 = dateFormat.parse("2003-10-11");
            Date premiere3 = dateFormat.parse("2003-10-11");

            peliculas.add(new Pelicula("Título 1", "Sinopsis 1", premiere1));
            peliculas.add(new Pelicula("Título 2", "Sinopsis 2", premiere2));
            peliculas.add(new Pelicula("Título 3", "Sinopsis 3", premiere3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return peliculas;
    }
}
