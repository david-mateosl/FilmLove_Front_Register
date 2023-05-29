package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import Modelo.Usuario;

// OnClickListenerHelper.java
public class IniciarPantallas {
    public static final int REQUEST_CODE_IMAGE_PICK = 1;
    private static OnActivityResultListener onActivityResultListener;

    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public static void setOnActivityResultListener(OnActivityResultListener listener) {
        onActivityResultListener = listener;
    }

    private void seleccionarImagenDeGaleria(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }

    public static void menuFotoPerfil(ImageView imagen, final Activity activity) {
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });
    }

    public static void volverAInicio(View view, final Context context, Usuario usuario) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public static void cerrarSesion(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(context, LoginActivity.class);
                context.startActivity(iLogin);
            }
        });
    }

    public static void favoritos(View view, final Context context,Usuario usuario) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iFavoritos = new Intent(context, FavoritoActivity.class);
                iFavoritos.putExtra("usuario",usuario);
                context.startActivity(iFavoritos);
            }
        });
    }

    public static void peliculas(View view, final Context context,Usuario usuario) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPeliculas = new Intent(context, PeliculasActivity.class);
                iPeliculas.putExtra("usuario",usuario);
                context.startActivity(iPeliculas);
            }
        });
    }

    public static void series(View view, final Context context,Usuario usuario) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSeries = new Intent(context, SeriesActivity.class);
                iSeries.putExtra("usuario",usuario);
                context.startActivity(iSeries);
                ((Activity) context).finish();
            }
        });
    }

    public static void generos(View view, final Context context, Usuario usuario) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGeneros = new Intent(context, GeneroActivity.class);
                iGeneros.putExtra("usuario",usuario);
                context.startActivity(iGeneros);
            }
        });
    }


}
