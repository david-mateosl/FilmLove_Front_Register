package com.example.filmlove_front_register.Controlador;

import static com.example.filmlove_front_register.Controlador.Configuracion.DBHOST;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBMOSTRARSERIES;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBSERIEALEATORIA;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Callback.SeriesCallback;
import Modelo.Serie;

public class ControladorSeries {

    public void obtenerSerieAleatoria(SeriesCallback callback) {
        class SerieRandomAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return fetchRandomSerie();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            private String fetchRandomSerie() throws Exception {
                String apiUrl = DBHOST + DBSERIEALEATORIA; // Reemplaza con tu URL de la API
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
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String title = jsonObject.getString("title");
                            String path = jsonObject.getString("path");
                            String sinopsis = jsonObject.getString("synopsis");
                            Serie serie = new Serie(title, path, sinopsis);
                            if (callback != null) {
                                callback.onSerieLoaded(serie);
                            }
                        } else {
                            if (callback != null) {
                                callback.onSeriesLoadError();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onSeriesLoadError();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onSeriesLoadError();
                    }
                }
            }
        }

        SerieRandomAsyncTask task = new SerieRandomAsyncTask();
        task.execute();
    }

    public void mostrarTodasLasSeries(final SeriesCallback callback) {
        class SeriesRandomAsyncTask extends AsyncTask<Void, Void, List<Serie>> {
            @Override
            protected List<Serie> doInBackground(Void... voids) {
                try {
                    URL url = new URL(DBHOST + DBMOSTRARSERIES);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    List<Serie> series = new ArrayList<>();
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

                        Serie serie = new Serie(titulo, synopsis, media_votos, rutaImagen);
                        series.add(serie);
                    }

                    return series;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Serie> series) {
                if (series != null) {
                    callback.onSeriesLoaded(series);
                } else {
                    callback.onSeriesLoadError();
                }
            }
        }

        SeriesRandomAsyncTask asyncTask = new SeriesRandomAsyncTask();
        asyncTask.execute();
    }


}
