package com.example.filmlove_front_register.Controlador;

import Modelo.Usuario;

public interface LoginCallback {
    void onLoginSuccess(Usuario usuario);
    void onLoginFailure();
}
