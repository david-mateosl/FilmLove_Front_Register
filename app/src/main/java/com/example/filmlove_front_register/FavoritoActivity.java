package com.example.filmlove_front_register;

import static com.example.filmlove_front_register.IniciarPantallas.REQUEST_CODE_IMAGE_PICK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
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

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Callback.DeleteCallback;
import Callback.ProductionCallback;
import Callback.VotedProductionsCallback;
import Modelo.Comentario;
import Modelo.Multimedia;
import Modelo.Production;
import Modelo.Usuario;

public class FavoritoActivity extends Activity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private List<Production> producciones;
    private Usuario usuario;
    ControladorProducion controladorProducion = new ControladorProducion();

    ImageView imagenLogo;
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView seriesBoton;
    private TextView generos;
    private SearchView barraBusqueda;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        System.out.println(usuario);

        listView = findViewById(R.id.listaFavoritos);
        imagenLogo = findViewById(R.id.imagenLogo);

        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        imagenPerfil = findViewById(R.id.imagenPerfilF);
        iniciarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        seriesBoton = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, FavoritoActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, FavoritoActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, FavoritoActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, FavoritoActivity.this, usuario);
        IniciarPantallas.series(seriesBoton, FavoritoActivity.this, usuario);
        IniciarPantallas.generos(generos, FavoritoActivity.this, usuario);

        producciones = new ArrayList<>();

        usuarioDAO = new UsuarioDAO(this); // Crear una instancia de UsuarioDAO

        if (usuario != null) {
            cargarDatosUsuario();
        }

        obtenerProduccionesVotadasDesdeBaseDeDatos();
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

    @Override
    protected void onResume() {
        super.onResume();
        obtenerProduccionesVotadasDesdeBaseDeDatos();
    }

    private void obtenerProduccionesVotadasDesdeBaseDeDatos() {
        controladorProducion.getVotedProductions(usuario.getUsername(), new VotedProductionsCallback() {
            @Override
            public void onVotedProductionsSuccess(List<Production> votedProductions) {
                if (!votedProductions.isEmpty()) {
                    Production primeraProduccion = votedProductions.get(0);
                }

                FavoritoActivity.this.producciones = votedProductions;

                ProduccionAdapter adapter = new ProduccionAdapter(votedProductions);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Production production = votedProductions.get(position);
                        buscarProduccionPorNombre(production.getTitulo());
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Production production = votedProductions.get(position);
                        eliminarDeFavoritos(production);
                        return true;
                    }
                });
            }

            @Override
            public void onVotedProductionsFailure() {
            }
        });
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                Toast.makeText(FavoritoActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = FavoritoActivity.this.getCurrentFocus();
                iniciarProduccion(view,production);
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(FavoritoActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void iniciarProduccion(View view, Production production) {
        System.out.println(production);
        if (production != null) {
            Intent intent = new Intent(FavoritoActivity.this, ProduccionActivity.class);
            intent.putExtra("production", production);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        }
    }

    private class ProduccionAdapter extends ArrayAdapter<Production> {

        public ProduccionAdapter(List<Production> productions) {
            super(FavoritoActivity.this, R.layout.formato_listview, productions);
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
            TextView txtComentario = view.findViewById(R.id.txtSinopsis);

            if (production.getMultimedia() != null) {
                Multimedia multimedia = production.getMultimedia();
                Picasso.get().load(multimedia.getPath()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.el_bueno_feo_malo);
            }

            txtTituloProducion.setText(production.getTitulo());
            txtValorancionMediaProducion.setText(String.valueOf(production.getVoto().getVoto()));
            txtComentario.setText(production.getComentario().getComentario());

            int rating = Integer.parseInt(String.valueOf(production.getVoto().getVoto()));
            updateVotosMedios(rating, txtValorancionMediaProducion);

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

    private void eliminarDeFavoritos(Production production) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoritoActivity.this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que quieres eliminar esta producción de favoritos?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userName = usuario.getUsername(); // Reemplaza "nombre_usuario" con el nombre de usuario correspondiente
                String productionTitle = production.getTitulo();

                ControladorProducion.DeleteCommentAndVoteTask task = new ControladorProducion.DeleteCommentAndVoteTask(new DeleteCallback() {
                    @Override
                    public void onDeleteSuccess(String result) {
                        if (result.trim().equalsIgnoreCase("{\"success\":\"Comentario y voto eliminados exitosamente\"}")) {
                            producciones.remove(production);
                            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
                            Toast.makeText(FavoritoActivity.this, "Producción eliminada de favoritos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FavoritoActivity.this, "Error al eliminar la producción de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDeleteError(String error) {
                        Toast.makeText(FavoritoActivity.this, "Error al eliminar la producción de favoritos: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                task.execute(userName, productionTitle);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
