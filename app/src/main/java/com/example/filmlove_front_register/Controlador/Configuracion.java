package com.example.filmlove_front_register.Controlador;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.widget.Toast;

import com.example.filmlove_front_register.LoginActivity;
import com.example.filmlove_front_register.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configuracion {

    static final String API = "/api/";
    static final String DBHOST = "https://c15f-79-146-19-118.eu.ngrok.io" + API;
    static final String DBLOGIN = "login";
    static final String DBFORGOTPWD = "forgotten-password";
    static final String DBREGISTRO = "users";
    static final String DBPELICULASALEATORIAS = "get-random-films";
    static final String DBSERIEALEATORIA = "get-random-series";
    static final String DBPRODUCIONESALEATORIAS = "get-random-productions";
    static final String DBBUSCARPRODUCION = "search-production";
    static final String DBMOSTRARPELICULAS = "show-films";
    static final String DBMOSTRARSERIES = "show-series";
    static final String DBVOTAR = "vote-production";
    static final String DBPRODUCIONESVOTADAS = "get-voted-production";
    static final String DBCOMENTARPRODUCIONES = "comment-production";
    static final String DBELIMINARFAV = "delete-fav";
    static final String DBGENEROS = "genders";

    static final String DBBUSCARPRODUCIONESGENEROS = "get-productions-by-gender";

}
