package com.example.filmlove_front_register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import Modelo.Pelicula;

public class PeliculasAdapter extends ArrayAdapter<Pelicula> {

    public PeliculasAdapter(Context context, List<Pelicula> peliculas) {
        super(context, 0, peliculas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.formato_listview, parent, false);
        }

        // Obtener la película actual
        Pelicula pelicula = getItem(position);

        // Obtener las referencias de los elementos de la vista
        TextView txtTitulo = convertView.findViewById(R.id.txtTituloProducion);
        TextView txtSinopsis = convertView.findViewById(R.id.txtSinopsis);
        TextView txtValoracion = convertView.findViewById(R.id.txtValorancionMediaProducion);

        // Configurar los datos de la película en los elementos de la vista
        txtTitulo.setText(pelicula.getTitulo());
        txtSinopsis.setText(pelicula.getSinopsis());
        txtValoracion.setText(String.valueOf(pelicula.getMedia_votos()));

        return convertView;
    }
}