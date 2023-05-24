package com.example.filmlove_front_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filmlove_front_register.Controlador.ControladorUsuarios;
import com.example.filmlove_front_register.Controlador.LoginCallback;
import com.example.filmlove_front_register.Controlador.ResetPasswordCallback;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Modelo.Usuario;

public class LoginActivity extends Activity implements LoginCallback {

    private EditText usuariotxt;
    private EditText contraseniatxt;
    private CheckBox recordarCheckBox;
    private ControladorUsuarios controladorUsuarios;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";
    private String nuevaContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuariotxt = findViewById(R.id.editTextUsername);
        contraseniatxt = findViewById(R.id.editTextContrasenia);
        recordarCheckBox = findViewById(R.id.checkRecordar);
        controladorUsuarios = new ControladorUsuarios();

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean remember = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (remember) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            usuariotxt.setText(savedUsername);
            contraseniatxt.setText(savedPassword);
            recordarCheckBox.setChecked(true);
        }
    }

    public void login(View view) {
        String username = usuariotxt.getText().toString();
        String password = contraseniatxt.getText().toString();
        boolean remember = recordarCheckBox.isChecked();

        if (remember) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_REMEMBER);
            editor.apply();
        }

        controladorUsuarios.login(username, password, this);
    }

    public void mostrarDialogoOlvidarContrasena(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restablecer contraseña");

        // Inflar el diseño personalizado para el diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        // Obtener la referencia del elemento de la interfaz del diálogo
        EditText emailEditText = dialogView.findViewById(R.id.editTextEmail);

        // Configurar el botón "Enviar"
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEditText.getText().toString();

                // Validar el formato del email (puedes agregar una validación adicional si lo deseas)
                if (isValidEmail(email)) {
                    // Aquí puedes llamar al método para enviar el email de restablecimiento de contraseña
                    enviarEmailRestablecimientoContrasena(view,email);
                    Toast.makeText(LoginActivity.this, "Correo enviado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Ingresa un email válido", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        // Configurar el botón "Cancelar"
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el diálogo sin realizar ninguna acción adicional
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Método para verificar el formato del email
    private boolean isValidEmail(String email) {
        // Aquí puedes implementar tu lógica de validación del formato del email
        // Puedes utilizar una expresión regular o alguna librería de validación de email

        // Ejemplo de validación básica
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para enviar el email de restablecimiento de contraseña
    private void enviarEmailRestablecimientoContrasena(View view,final String email) {
        // Crear un nuevo hilo para la operación de red
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Llamar al método de la clase ControladorUsuarios para solicitar el restablecimiento de contraseña
                controladorUsuarios.solicitarRestablecimientoContrasena(view,email, new ResetPasswordCallback() {
                    @Override
                    public void onResetPasswordSuccess(String nuevaContrasena) {
                        // Enviar el correo electrónico con la nueva contraseña
                        enviarCorreoRestablecimientoContrasena(email, nuevaContrasena);
                    }

                    @Override
                    public void onResetPasswordFailure() {
                        // Mostrar un Toast desde el hilo principal
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(LoginActivity.this, "Error al solicitar el restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        // Iniciar el hilo
        thread.start();
    }

    private void enviarCorreoRestablecimientoContrasena(final String email, final String nuevaContrasena) {
        // Crear un nuevo hilo para la operación de red
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Configurar las propiedades para la sesión de correo electrónico
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com"); // Reemplaza con la dirección del servidor de correo saliente (SMTP)
                props.put("mail.smtp.port", "587"); // Reemplaza con el puerto del servidor de correo saliente (SMTP)

                // Crear una instancia de autenticación para el servidor de correo electrónico
                Authenticator auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("filmlove.4u@gmail.com", "izxclmzxynbjzbkh"); // Reemplaza con tus credenciales de correo electrónico
                    }
                };

                // Crear una sesión de correo electrónico con las propiedades y autenticación configuradas
                Session session = Session.getInstance(props, auth);

                try {
                    // Crear un objeto de mensaje de correo electrónico
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("filmlove.4u@gmail.com")); // Reemplaza con tu dirección de correo electrónico
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Reemplaza con la dirección de correo electrónico del destinatario
                    message.setSubject("Restablecimiento de contraseña"); // Asunto del correo electrónico
                    message.setText("Hola,\n\nHas solicitado restablecer tu contraseña. Aquí está la contraseña que se te ha asignado [" + nuevaContrasena + "]"); // Contenido del correo electrónico

                    // Enviar el correo electrónico
                    Transport.send(message);

                    // Mostrar un Toast desde el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Correo enviado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    // Mostrar un Toast desde el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Iniciar el hilo
        thread.start();
    }

    public void olvidada(View view) {
        mostrarDialogoOlvidarContrasena(view);
    }

    @Override
    public void onLoginSuccess(Usuario usuario) {
        Intent iPrincipal = new Intent(this, PrincipalActivity.class);
        iPrincipal.putExtra("usuario", usuario); // Pasar el objeto usuario a través del Intent
        startActivity(iPrincipal);
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
    }

    public void registro(View view) {
        Intent iRegistro = new Intent(this, RegisterActivity.class);
        startActivity(iRegistro);
        finish();
    }
}
