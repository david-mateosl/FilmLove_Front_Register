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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filmlove_front_register.Controlador.ControladorUsuarios;
import Callback.LoginCallback;
import Callback.ResetPasswordCallback;

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
        recordarCheckBox.setChecked(remember);

        if (remember) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            usuariotxt.setText(savedUsername);
            contraseniatxt.setText(savedPassword);
            recordarCheckBox.setChecked(true);
        }

        recordarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    String username = usuariotxt.getText().toString();
                    String password = contraseniatxt.getText().toString();
                    editor.putString(KEY_USERNAME, username);
                    editor.putString(KEY_PASSWORD, password);
                    editor.putBoolean(KEY_REMEMBER, true);
                } else {
                    editor.remove(KEY_USERNAME);
                    editor.remove(KEY_PASSWORD);
                    editor.remove(KEY_REMEMBER);
                }
                editor.apply();
            }
        });
    }

    public void login(View view) {
        String username = usuariotxt.getText().toString();
        String password = contraseniatxt.getText().toString();
        boolean remember = recordarCheckBox.isChecked();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (remember) {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(KEY_REMEMBER, true);
        } else {
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_REMEMBER);
        }
        editor.apply();

        controladorUsuarios.login(username, password, this);

        boolean rememberPref = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (rememberPref) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            usuariotxt.setText(savedUsername);
            contraseniatxt.setText(savedPassword);
            recordarCheckBox.setChecked(true);
        } else {
            usuariotxt.setText("");
            contraseniatxt.setText("");
            recordarCheckBox.setChecked(false);
        }
    }


    public void mostrarDialogoOlvidarContrasena(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restablecer contraseña");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        EditText emailEditText = dialogView.findViewById(R.id.editTextEmail);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEditText.getText().toString();

                // Validar el formato del email (puedes agregar una validación adicional si lo deseas)
                if (isValidEmail(email)) {
                    enviarEmailRestablecimientoContrasena(view,email);
                } else {
                    Toast.makeText(LoginActivity.this, "Ingresa un email válido", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void enviarEmailRestablecimientoContrasena(View view, String email) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                controladorUsuarios.solicitarRestablecimientoContrasena(view, email, new ResetPasswordCallback() {
                    @Override
                    public void onResetPasswordSuccess(String nuevaContrasena) {
                        enviarCorreoRestablecimientoContrasena(email, nuevaContrasena);
                    }

                    @Override
                    public void onResetPasswordFailure() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error al solicitar el restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        thread.start();
    }


    private void enviarCorreoRestablecimientoContrasena(String email, String nuevaContrasena) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com"); // dirección del servidor de correo saliente (SMTP)
                props.put("mail.smtp.port", "587"); // puerto del servidor de correo saliente (SMTP)

                Authenticator auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("filmlove.4u@gmail.com", "izxclmzxynbjzbkh");
                    }
                };

                Session session = Session.getInstance(props, auth);

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("filmlove.4u@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("Restablecimiento de contraseña"); // Asunto del correo electrónico
                    message.setText("Hola,\n\nHas solicitado restablecer tu contraseña. Aquí está la contraseña que se te ha asignado [" + nuevaContrasena + "]");

                    Transport.send(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Correo enviado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
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
