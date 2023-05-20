package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filmlove_front_register.Controlador.ApiService;
import com.example.filmlove_front_register.Controlador.Configuracion;
import com.example.filmlove_front_register.Controlador.ControladorUsuarios;

import Modelo.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    EditText usuario;
    EditText contrasenia;
    ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
    //private final ApiService servicioLogin = Configuracion.getRetrofit(this).create(ApiService.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = findViewById(R.id.editTextUsername);
        contrasenia = findViewById(R.id.editTextContrasenia);
    }

    public void login(View view) {

        Intent iPrincipal = new Intent(this,PrincipalActivity.class);
        startActivity(iPrincipal);
    }

    public void registro(View view) {
        Intent iRegistro = new Intent(this, RegisterActivity.class);
        startActivity(iRegistro);
        finish();
    }
}
