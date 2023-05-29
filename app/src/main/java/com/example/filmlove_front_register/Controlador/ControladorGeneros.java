package com.example.filmlove_front_register.Controlador;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.filmlove_front_register.Controlador.Configuracion.DBGENEROS;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBHOST;

import Callback.GenderCallback;
import Modelo.Genero;

public class ControladorGeneros {

    public void showGenders(final GenderCallback callback) {
        class ShowGendersAsyncTask extends AsyncTask<Void, Void, List<Genero>> {
            @Override
            protected List<Genero> doInBackground(Void... voids) {
                try {
                    URL url = new URL(DBHOST + DBGENEROS);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONArray jsonGenders = new JSONArray(response.toString());
                    List<Genero> genders = new ArrayList<>();
                    for (int i = 0; i < jsonGenders.length(); i++) {
                        JSONObject jsonGender = jsonGenders.getJSONObject(i);
                        String name = jsonGender.optString("name");
                        Genero gender = new Genero(name);
                        genders.add(gender);
                    }
                    return genders;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Genero> genders) {
                if (genders != null) {
                    callback.onGendersLoaded(genders);
                } else {
                    callback.onGenderLoadError();
                }
            }
        }

        ShowGendersAsyncTask asyncTask = new ShowGendersAsyncTask();
        asyncTask.execute();
    }
}
