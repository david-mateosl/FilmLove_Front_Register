package com.example.filmlove_front_register.Controlador;

import Modelo.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<Usuario> login(
            @Field("username") String username,
            @Field("password") String password
    );
}

