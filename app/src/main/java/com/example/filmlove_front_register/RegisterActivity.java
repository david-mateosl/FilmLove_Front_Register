package com.example.filmlove_front_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmlove_front_register.Controlador.ControladorUsuarios;

import org.w3c.dom.Text;

import Callback.RegistroCallback;
import Modelo.Usuario;

public class RegisterActivity extends Activity implements RegistroCallback {

    ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
    TextView usuario;
    TextView email;
    TextView contrasena;
    TextView verificaContrasena;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuario = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        contrasena = findViewById(R.id.editTextContrasenia);
        verificaContrasena = findViewById(R.id.editTextContrasenaVerficada);
    }

    public void registrarse(View view) {
        if (contrasena.getText().toString().equals(verificaContrasena.getText().toString())) {
            controladorUsuarios.registrarUsuario(usuario.getText().toString(), email.getText().toString(), contrasena.getText().toString(), this);
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            contrasena.setText("");
            verificaContrasena.setText("");
        }
    }

    public void volver(View view) {
        Intent intentVolver = new Intent(this, LoginActivity.class);
        startActivity(intentVolver);
        finish();
    }

    @Override
    public void onRegistroSuccess(String message) {
        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistroFailure() {
        Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
    }
}
