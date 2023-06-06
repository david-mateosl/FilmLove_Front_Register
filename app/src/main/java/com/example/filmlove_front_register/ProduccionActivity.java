package com.example.filmlove_front_register;

import static com.example.filmlove_front_register.IniciarPantallas.REQUEST_CODE_IMAGE_PICK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmlove_front_register.Controlador.ControladorProducion;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import Callback.CommentCallback;
import Callback.ProductionCallback;
import Callback.ProductionVoteCallback;
import Modelo.Genero;
import Modelo.Multimedia;
import Modelo.Production;
import Modelo.Usuario;

public class ProduccionActivity extends Activity implements SearchView.OnQueryTextListener {

    private ViewFlipper v_fliper;
    private RatingBar ratingBar;
    private Production production;
    private Usuario usuario;
    private TextView tituloProducion;
    private TextView generosProducion;
    private TextView directorProducion;
    private TextView premiereProducion;
    private TextView sinopsisProducion;
    private TextView repartoProducion;
    private ImageView imagenCaratula;
    private TextView votosMedios;
    private String generosProducionConcatenados;
    private String rutaCaratula = null;
    private String rutaImagen1 = null;
    private String rutaImagen2 = null;
    private String rutaImagen3 = null;
    ImageView imagenLogo;
    private ImageView imagenPerfil;
    private TextView iniciarSesion;
    private TextView favoritos;
    private TextView pelicula;
    private TextView seriesBoton;
    private TextView generos;
    private SearchView barraBusqueda;

    private UsuarioDAO usuarioDAO;


    private ControladorProducion controladorProducion = new ControladorProducion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produccion);

        Intent intent = getIntent();
        production = (Production) intent.getSerializableExtra("production");
        System.out.println(production);
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        generosProducionConcatenados = obtenerGenerosConcatenados(production.getGeneroList());
        barraBusqueda = findViewById(R.id.barraDeBusqueda);
        barraBusqueda.setOnQueryTextListener(this);

        List<Multimedia> multimediaList = production.getMultimediaList();
        for (Multimedia multimedia : multimediaList) {
            String tipo = multimedia.getTipo();
            String ruta = multimedia.getPath();

            if (tipo.equals("caratula")) {
                rutaCaratula = ruta;
            } else if (tipo.equals("imagen 1")) {
                rutaImagen1 = ruta;
            } else if (tipo.equals("imagen 2")) {
                rutaImagen2 = ruta;
            } else if (tipo.equals("imagen 3")) {
                rutaImagen3 = ruta;
            }
        }

        ratingBar = findViewById(R.id.barraDeBotacion);
        v_fliper = findViewById(R.id.galeriaProduccion);
        tituloProducion = findViewById(R.id.tituloProducion);
        generosProducion = findViewById(R.id.generosProducion);
        directorProducion = findViewById(R.id.driectorProducion);
        premiereProducion = findViewById(R.id.premiereProducion);
        sinopsisProducion = findViewById(R.id.sinopsisProducion);
        repartoProducion = findViewById(R.id.repartoProducion);
        imagenCaratula = findViewById(R.id.imagenProducion);
        votosMedios = findViewById(R.id.votosMediosProducion);
        imagenLogo = findViewById(R.id.imagenLogo);
        imagenPerfil = findViewById(R.id.imagenPerfilP);
        iniciarSesion = findViewById(R.id.menuitemcerrarsesion);
        favoritos = findViewById(R.id.menuitemfavoritos);
        pelicula = findViewById(R.id.menuitempeliculas);
        seriesBoton = findViewById(R.id.menuitemseries);
        generos = findViewById(R.id.menuitemGeneros);
        imagenLogo = findViewById(R.id.imagenLogo);

        IniciarPantallas.menuFotoPerfil(imagenPerfil, ProduccionActivity.this);
        IniciarPantallas.volverAInicio(iniciarSesion, ProduccionActivity.this, usuario);
        IniciarPantallas.favoritos(favoritos, ProduccionActivity.this, usuario);
        IniciarPantallas.peliculas(pelicula, ProduccionActivity.this, usuario);
        IniciarPantallas.series(seriesBoton, ProduccionActivity.this, usuario);
        IniciarPantallas.generos(generos, ProduccionActivity.this, usuario);

        tituloProducion.setText(production.getTitulo());
        directorProducion.setText(production.getDirector());
        String premiere = production.getPremiere();
        String premiereSubstring = premiere.substring(0, 4);
        premiereProducion.setText(premiereSubstring);
        sinopsisProducion.setText(production.getSinopsis());
        repartoProducion.setText(production.getCasting());
        generosProducion.setText(generosProducionConcatenados);
        Picasso.get().load(rutaCaratula).into(imagenCaratula);

        actualizarVotosMedios();

        String[] imagenes = {rutaImagen1, rutaImagen2, rutaImagen3};

        for (String imagen : imagenes) {
            cargarImagenesProduccion(imagen);
        }

        usuarioDAO = new UsuarioDAO(this); // Crear una instancia de UsuarioDAO

        if (usuario != null) {
            cargarDatosUsuario();
        }

        votar();
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

    public String obtenerGenerosConcatenados(List<Genero> generosList) {
        StringBuilder generosString = new StringBuilder();

        for (int i = 0; i < generosList.size(); i++) {
            Genero genero = generosList.get(i);
            String nombreGenero = genero.getName();

            generosString.append(nombreGenero);

            if (i < generosList.size() - 1) {
                generosString.append("\n");
            }
        }

        return generosString.toString();
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

    public void votar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProduccionActivity.this);
                dialogBuilder.setTitle("Comentario");

                final EditText editText = new EditText(ProduccionActivity.this);
                dialogBuilder.setView(editText);

                dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String comment = editText.getText().toString();
                        int vote = (int) rating;

                        CommentCallback commentCallback = new CommentCallback() {
                            @Override
                            public void onCommentSuccess(String message) {
                                String mensaje = production.getTitulo() + " fue votada exitosamente";
                                Toast.makeText(ProduccionActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                                actualizarVotosMedios(); // Actualizar la votación media después de votar y comentar
                            }

                            @Override
                            public void onCommentFailure(String error) {
                                // Manejar el fallo del comentario, por ejemplo, mostrar un mensaje de error
                                Toast.makeText(ProduccionActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        };

                        ProductionVoteCallback voteCallback = new ProductionVoteCallback() {
                            @Override
                            public void onVoteProductionSuccess(String response) {
                                String mensaje = production.getTitulo() + " fue votada exitosamente";
                                Toast.makeText(ProduccionActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                                actualizarVotosMedios(); // Actualizar la votación media después de votar y comentar
                            }

                            @Override
                            public void onVoteProductionFailure() {
                                // Votación fallida, puedes manejar el error o mostrar un mensaje de error
                            }
                        };

                        String userName = usuario.getUsername();
                        String productionTitle = production.getTitulo();

                        ControladorProducion.CommentProductionTask commentTask = new ControladorProducion.CommentProductionTask(commentCallback);
                        commentTask.execute(userName, productionTitle, comment);

                        ControladorProducion.voteProduction(userName, productionTitle, vote, voteCallback);

                        dialog.dismiss();
                    }
                });

                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    private void buscarProduccionPorNombre(String nombre) {
        controladorProducion.searchProduction(nombre, new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                // Actualizar la votación media y el color en el campo correspondiente
                updateVotosMedios(production.getRating_medio());
                Toast.makeText(ProduccionActivity.this, production.getTitulo(), Toast.LENGTH_SHORT).show();
                View view = ProduccionActivity.this.getCurrentFocus();
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                Toast.makeText(ProduccionActivity.this, "No se encontró ninguna producción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVotosMedios(float ratingMedio) {
        int roundedRating = Math.round(ratingMedio);
        votosMedios.setText(roundedRating + "/5");

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

        votosMedios.setBackgroundResource(colorId);
    }

    private void cargarImagenesProduccion(String imagen) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(imagen).into(imageView);
        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(5000);
        v_fliper.setAutoStart(true);
    }

    private void actualizarVotosMedios() {
        controladorProducion.searchProduction(production.getTitulo(), new ProductionCallback() {
            @Override
            public void onProductionSuccess(Production production) {
                updateVotosMedios(production.getRating_medio());
            }

            @Override
            public void onProductionListLoaded(List<Production> productions) {

            }

            @Override
            public void onProductionFailure() {
                // Manejar la falla de búsqueda de producción
            }
        });
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
