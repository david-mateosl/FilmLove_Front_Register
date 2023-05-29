package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorGeneros;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import Callback.GenderCallback;
import Modelo.Genero;
import Modelo.Usuario;

public class GeneroActivity extends Activity implements GenderCallback {

    private ListView listaGeneros;
    private ArrayAdapter<Genero> generoAdapter;

    private Usuario usuario;
    ImageView imagenLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        listaGeneros = findViewById(R.id.listaGener);
        imagenLogo = findViewById(R.id.imagenLogo);
        generoAdapter = new ArrayAdapter<Genero>(this, R.layout.formato_listview_generos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.formato_listview_generos, parent, false);
                }

                TextView txtGenero = convertView.findViewById(R.id.txtGeneroFormato);
                ImageView imagenGenero = convertView.findViewById(R.id.imagenGenero);
                Genero genero = getItem(position);
                if (genero != null) {
                    txtGenero.setText(genero.getName());

                    String nombreGenero = removeAccents(genero.getName().toLowerCase().replace(" ", "_"));

                    int idImagenGenero = getResources().getIdentifier(nombreGenero, "drawable", getPackageName());

                    if (idImagenGenero != 0) {
                        Drawable drawable = getResources().getDrawable(idImagenGenero);
                        drawable.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                        imagenGenero.setImageDrawable(drawable);
                    } else {
                        imagenGenero.setImageResource(R.drawable.el_bueno_feo_malo);
                    }
                }

                return convertView;
            }
        };
        listaGeneros.setAdapter(generoAdapter);
        listaGeneros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Genero generoSeleccionado = generoAdapter.getItem(position);
                System.out.println(generoSeleccionado);
                if (generoSeleccionado != null) {
                    String nombreGenero = generoSeleccionado.getName();
                    Intent intent = new Intent(GeneroActivity.this, ProducionesPorGeneroActivity.class);
                    intent.putExtra("genero", nombreGenero);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                }
            }
        });

        ControladorGeneros controladorGeneros = new ControladorGeneros();
        controladorGeneros.showGenders(this);
        desplegarMenu();
    }

    @Override
    public void onGendersLoaded(List<Genero> genders) {
        if (genders != null) {
            generoAdapter.addAll(genders);
            generoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGenderLoadError() {
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
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
