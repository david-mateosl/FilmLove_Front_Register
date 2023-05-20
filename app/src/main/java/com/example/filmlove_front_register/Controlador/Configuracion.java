package com.example.filmlove_front_register.Controlador;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.widget.Toast;

import com.example.filmlove_front_register.LoginActivity;
import com.example.filmlove_front_register.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configuracion {

    private static final String DBHOST = "";
    private static final String USUARIOS = "login/";
    private static final String DB_DATABASE = "postgresºº";
    private static final String DB_USERNAME = "root";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context){
        if (retrofit == null){
            retrofit = new  Retrofit.Builder()
                                    .baseUrl(DBHOST)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
        }
        return retrofit;
    }

    public static void mensaje(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

}
