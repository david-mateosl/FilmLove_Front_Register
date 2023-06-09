package com.example.filmlove_front_register;

import static com.example.filmlove_front_register.IniciarPantallas.REQUEST_CODE_IMAGE_PICK;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorGeneros;
import com.example.filmlove_front_register.Controlador.ControladorProducion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import Callback.GenderCallback;
import Callback.ProductionCallback;
import Modelo.Genero;
import Modelo.Production;
import Modelo.Usuario;

public class GeneroActivity extends Activity implements GenderCallback, SearchView.OnQueryTextListener {

    private ListView listaGeneros;
    private ArrayAdapter<Genero> generoAdapter;
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView seriesBoton;
    private TextView generos;

    private Usuario usuario;
    private ImageView imagenLogo;
    private SearchView barraBusqueda;
    private UsuarioDAO usuarioDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        listaGeneros = findViewById(R.id.listaGener);
        imagenLogo = findViewById(R.id.imagenLogo);

        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        imagenPerfil = findViewById(R.id.imagenPerfilG);
        iniciarSesion = findViewById(R.id.menuitemInicioSesionGeneros);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        seriesBoton = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, GeneroActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, GeneroActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, GeneroActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, GeneroActivity.this, usuario);
        IniciarPantallas.series(seriesBoton, GeneroActivity.this, usuario);
        IniciarPantallas.generos(generos, GeneroActivity.this, usuario);
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

        usuarioDAO = new UsuarioDAO(this); // Crear una instancia de UsuarioDAO

        if (usuario != null) {
            cargarDatosUsuario();
        }

        ControladorGeneros controladorGeneros = new ControladorGeneros();
        controladorGeneros.showGenders(this);
        desplegarMenu();
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
        ControladorProducion controladorProducion = new ControladorProducion();
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(GeneroActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = GeneroActivity.this.getCurrentFocus();
                iniciarProduccion(view,production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(GeneroActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void iniciarProduccion(View view, Production production) {
        System.out.println(production);
        if (production != null) {
            Intent intent = new Intent(GeneroActivity.this, ProduccionActivity.class);
            intent.putExtra("production", production);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        }
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
