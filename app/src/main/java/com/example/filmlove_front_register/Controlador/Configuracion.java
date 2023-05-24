package com.example.filmlove_front_register.Controlador;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.widget.Toast;

import com.example.filmlove_front_register.LoginActivity;
import com.example.filmlove_front_register.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configuracion {

    static final String DBHOST = "https://6829-81-43-16-96.eu.ngrok.io/api";
    static final String DBLOGIN = "login";
    static final String DBFORGOTPWD = "forgotten-password";

}
