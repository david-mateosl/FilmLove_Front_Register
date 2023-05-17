package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static void cerrarSesion(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(context, LoginActivity.class);
                context.startActivity(iLogin);
            }
        });
    }

    public static void favoritos(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iFavoritos = new Intent(context, FavoritoActivity.class);
                context.startActivity(iFavoritos);
            }
        });
    }

    public static void peliculas(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPeliculas = new Intent(context, PeliculasActivity.class);
                context.startActivity(iPeliculas);
            }
        });
    }

    public static void series(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSeries = new Intent(context, SeriesActivity.class);
                context.startActivity(iSeries);
                ((Activity) context).finish();
            }
        });
    }

    public static void generos(View view, final Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGeneros = new Intent(context, GeneroActivity.class);
                context.startActivity(iGeneros);
            }
        });
    }
}
