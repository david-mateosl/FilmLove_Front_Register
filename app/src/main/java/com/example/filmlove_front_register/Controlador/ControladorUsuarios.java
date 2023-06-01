package com.example.filmlove_front_register.Controlador;

import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import Callback.LoginCallback;
import Callback.RegistroCallback;
import Callback.ResetPasswordCallback;
import Modelo.Usuario;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ControladorUsuarios {

    private final String dbHost = Configuracion.DBHOST;
    private final String dbLogin = Configuracion.DBLOGIN;
    private final String dbforgotpwd = Configuracion.DBFORGOTPWD;
    private final String dbRegistro = Configuracion.DBREGISTRO;

    public void login(String usuario, String contrasena, LoginCallback callback) {
        class InsertarAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    return logearse(strings[0], strings[1]);
                } catch (Exception e) {
                    return null;
                }
            }

            private String logearse(String usuario, String contrasena) throws Exception {
                String parametros = "name=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&password=" + URLEncoder.encode(contrasena, "UTF-8");

                String apiUrl = dbHost + dbLogin;
                URL url = new URL(apiUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(parametros);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String lineaEntrada;
                StringBuilder response = new StringBuilder();
                while ((lineaEntrada = in.readLine()) != null) {
                    response.append(lineaEntrada);
                }
                in.close();

                return response.toString();
            }

            @Override
            protected void onPostExecute(String respuesta) {
                if (respuesta != null) {
                    Gson gson = new Gson();
                    Usuario usuario = gson.fromJson(respuesta, Usuario.class);
                    if (usuario != null) {
                        usuario.getUsername();
                        if (callback != null) {
                            callback.onLoginSuccess(usuario);
                        }
                    } else {
                        if (callback != null) {
                            callback.onLoginFailure();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onLoginFailure();
                    }
                }
            }
        }

        InsertarAsyncTask task = new InsertarAsyncTask();
        task.execute(usuario, contrasena);
    }

    public void solicitarRestablecimientoContrasena(View view, String email, final ResetPasswordCallback callback) {
        class SolicitarRestablecimientoAsyncTask extends AsyncTask<String, Void, Pair<String, String>> {
            @Override
            protected Pair<String, String> doInBackground(String... strings) {
                try {
                    return solicitarRestablecimiento(strings[0]);
                } catch (Exception e) {
                    return null;
                }
            }

            private Pair<String, String> solicitarRestablecimiento(String email) throws Exception {
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("email", email)
                        .build();

                Request request = new Request.Builder()
                        .url(dbHost + dbforgotpwd)
                        .post(requestBody)
                        .build();


                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String password = response.body().string();
                    return Pair.create("success", password);
                } else {
                    return Pair.create("error", null);
                }
            }

            @Override
            protected void onPostExecute(Pair<String, String> result) {
                super.onPostExecute(result);

                if (result != null) {
                    if (result.first.equals("success")) {
                        callback.onResetPasswordSuccess(result.second);
                    } else {
                        callback.onResetPasswordFailure();
                    }
                } else {
                    callback.onResetPasswordFailure();
                }
            }
        }

        SolicitarRestablecimientoAsyncTask task = new SolicitarRestablecimientoAsyncTask();
        task.execute(email);
    }

    public void registrarUsuario(String username, String email, String password, final RegistroCallback callback) {
        class RegistroAsyncTask extends AsyncTask<Void, Void, Pair<String, String>> {
            @Override
            protected Pair<String, String> doInBackground(Void... voids) {
                try {
                    return registrarUsuario(username, email, password);
                } catch (Exception e) {
                    return null;
                }
            }

            private Pair<String, String> registrarUsuario(String username, String email, String password) throws Exception {
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("name",   URLEncoder.encode(username, "UTF-8"))
                        .add("email", URLDecoder.decode(email, "UTF-8"))
                        .add("password", URLEncoder.encode(password, "UTF-8"))
                        .build();

                Request request = new Request.Builder()
                        .url(dbHost + dbRegistro)  // Reemplaza "api/registrar-usuario" con la ruta correcta de tu API
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Aquí puedes procesar la respuesta de la API si es necesario
                    return Pair.create("success", responseBody);
                } else {
                    return Pair.create("error", null);
                }
            }

            @Override
            protected void onPostExecute(Pair<String, String> result) {
                super.onPostExecute(result);

                if (result != null) {
                    if (result.first.equals("success")) {
                        callback.onRegistroSuccess(result.second);
                    } else {
                        callback.onRegistroFailure();
                    }
                } else {
                    callback.onRegistroFailure();
                }
            }
        }

        RegistroAsyncTask task = new RegistroAsyncTask();
        task.execute();
    }
}
