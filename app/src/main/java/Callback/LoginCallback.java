package Callback;

import Modelo.Usuario;

public interface LoginCallback {
    void onLoginSuccess(Usuario usuario);
    void onLoginFailure();
}
