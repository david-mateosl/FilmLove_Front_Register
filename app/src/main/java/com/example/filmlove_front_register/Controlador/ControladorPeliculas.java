package com.example.filmlove_front_register.Controlador;

import static com.example.filmlove_front_register.Controlador.Configuracion.DBHOST;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBMOSTRARPELICULAS;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBPELICULASALEATORIAS;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBPRODUCIONESALEATORIAS;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Callback.FilmsCallback;
import Modelo.Pelicula;

public class ControladorPeliculas {

    public void obtenerPeliculasAleatorias(FilmsCallback callback) {
        class PeliculasRandomAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return fetchRandomFilms();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            private String fetchRandomFilms() throws Exception {
                String apiUrl = DBHOST + DBPELICULASALEATORIAS; // Reemplaza con tu URL de la API
                URL url = new URL(apiUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    // Procesa la respuesta JSON y conviértela en objetos Films
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<Pelicula> peliculas = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String path = jsonObject.getString("path");
                            String name = jsonObject.getString("name");
                            Pelicula film = new Pelicula(title, path, name);
                            peliculas.add(film);
                        }
                        if (callback != null) {
                            callback.onFilmsLoaded(peliculas);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFilmsLoadError();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFilmsLoadError();
                    }
                }
            }
        }

        PeliculasRandomAsyncTask task = new PeliculasRandomAsyncTask();
        task.execute();
    }

    public void mostrarTodasLasPelis(final FilmsCallback callback) {
        class PeliculasRandomAsyncTask extends AsyncTask<Void, Void, List<Pelicula>> {
            @Override
            protected List<Pelicula> doInBackground(Void... voids) {
                try {
                    URL url = new URL(DBHOST + DBMOSTRARPELICULAS);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    List<Pelicula> peliculas = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String titulo = jsonObject.optString("title");
                        String synopsis = jsonObject.optString("synopsis");
                        int media_votos = jsonObject.optInt("rating_average");
                        String rutaImagen = "";

                        JSONArray multimediaArray = jsonObject.getJSONArray("multimedia");
                        for (int j = 0; j < multimediaArray.length(); j++) {
                            JSONObject multimediaObject = multimediaArray.getJSONObject(j);
                            String tipo = multimediaObject.optString("type");
                            if (tipo.equals("caratula")) {
                                rutaImagen = multimediaObject.optString("path");
                                break;
                            }
                        }

                        Pelicula pelicula = new Pelicula(titulo, synopsis, media_votos, rutaImagen);
                        peliculas.add(pelicula);
                    }

                    return peliculas;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Pelicula> peliculas) {
                if (peliculas != null) {
                    if (callback != null) {
                        callback.onFilmsLoaded(peliculas);
                    }
                } else {
                    if (callback != null) {
                        callback.onFilmsLoadError();
                    }
                }
            }
        }

        PeliculasRandomAsyncTask task = new PeliculasRandomAsyncTask();
        task.execute();
    }


    public void obtenerProducionesAleatorias(FilmsCallback callback) {
        class ProducionesRandomAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return fetchRandomFilms();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            private String fetchRandomFilms() throws Exception {
                String apiUrl = DBHOST + DBPRODUCIONESALEATORIAS;
                URL url = new URL(apiUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<Pelicula> peliculas = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String path = jsonObject.getString("path");
                            Pelicula film = new Pelicula(title, path);
                            peliculas.add(film);
                        }
                        if (callback != null) {
                            callback.onFilmsLoaded(peliculas);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFilmsLoadError();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFilmsLoadError();
                    }
                }
            }
        }

        ProducionesRandomAsyncTask task = new ProducionesRandomAsyncTask();
        task.execute();
    }
}
