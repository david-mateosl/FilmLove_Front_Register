package com.example.filmlove_front_register.Controlador;

import static com.example.filmlove_front_register.Controlador.Configuracion.DBBUSCARPRODUCION;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBCOMENTARPRODUCIONES;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBELIMINARFAV;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBHOST;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBPRODUCIONESVOTADAS;
import static com.example.filmlove_front_register.Controlador.Configuracion.DBVOTAR;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Callback.CommentCallback;
import Callback.DeleteCallback;
import Callback.ProductionCallback;
import Callback.ProductionVoteCallback;
import Callback.VotedProductionsCallback;
import Modelo.Comentario;
import Modelo.Genero;
import Modelo.Multimedia;
import Modelo.Production;
import Modelo.Voto;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControladorProducion {

    public void searchProduction(String title, ProductionCallback callback) {
        class SearchProductionAsyncTask extends AsyncTask<String, Void, Pair<String, String>> {
            @Override
            protected Pair<String, String> doInBackground(String... params) {
                try {
                    return searchProduction(params[0]);
                } catch (IOException e) {
                    return null;
                }
            }

            private Pair<String, String> searchProduction(String title) throws IOException {
                String apiUrl = DBHOST + DBBUSCARPRODUCION;
                String encodedTitle = URLEncoder.encode(title, "UTF-8");
                String urlString = apiUrl + "?title=" + encodedTitle;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    return Pair.create("success", response.toString());
                } else {
                    return Pair.create("error", null);
                }
            }

            @Override
            protected void onPostExecute(Pair<String, String> result) {
                if (result != null) {
                    if (result.first.equals("success")) {
                        try {
                            JSONArray jsonArray = new JSONArray(result.second);
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                String title = jsonObject.getString("title");
                                String director = jsonObject.getString("director");
                                String premiere = jsonObject.getString("premiere");
                                String casting = jsonObject.getString("casting");
                                String synopsis = jsonObject.getString("synopsis");
                                int ratingAverage = jsonObject.getInt("rating_average");

                                JSONArray multimediaArray = jsonObject.getJSONArray("multimedia");
                                List<Multimedia> multimediaList = new ArrayList<>();

                                for (int i = 0; i < multimediaArray.length(); i++) {
                                    JSONObject multimediaObject = multimediaArray.getJSONObject(i);
                                    String path = multimediaObject.getString("path");
                                    String type = multimediaObject.getString("type");

                                    Multimedia multimedia = new Multimedia();
                                    multimedia.setPath(path);
                                    multimedia.setTipo(type);

                                    multimediaList.add(multimedia);
                                }

                                JSONArray generosArray = jsonObject.getJSONArray("genders");
                                List<Genero> generosList = new ArrayList<>();

                                for (int i = 0; i < generosArray.length(); i++) {
                                    JSONObject multimediaObject = generosArray.getJSONObject(i);
                                    String name = multimediaObject.getString("name");

                                    Genero generos = new Genero();
                                    generos.setName(name);

                                    generosList.add(generos);
                                }

                                Production production = new Production();
                                production.setTitulo(title);
                                production.setDirector(director);
                                production.setPremiere(premiere);
                                production.setCasting(casting);
                                production.setSinopsis(synopsis);
                                production.setRating_medio(ratingAverage);
                                production.setMultimediaList(multimediaList);
                                production.setGeneroList(generosList);

                                callback.onProductionSuccess(production);
                            } else {
                                callback.onProductionFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onProductionFailure();
                        }
                    } else {
                        callback.onProductionFailure();
                    }
                } else {
                    callback.onProductionFailure();
                }
            }
        }

        SearchProductionAsyncTask task = new SearchProductionAsyncTask();
        task.execute(title);
    }

    public static void voteProduction(String name, String title, int vote, ProductionVoteCallback callback) {
        String finalName = name;
        String finalTitle = title;
        int finalVote = vote;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("name", finalName);
            formBuilder.add("title", finalTitle);
            formBuilder.add("vote", String.valueOf(finalVote));

            RequestBody requestBody = formBuilder.build();

            Request request = new Request.Builder()
                    .url(DBHOST + DBVOTAR)  // Reemplaza "api/vote-production" con la ruta correcta de tu API
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    callback.onVoteProductionSuccess("Votación exitosa");
                } else {
                    callback.onVoteProductionFailure();
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onVoteProductionFailure();
            }
        });
    }

    public void getVotedProductions(String name, VotedProductionsCallback callback) {
        class GetVotedProductionsAsyncTask extends AsyncTask<String, Void, Pair<String, String>> {
            @Override
            protected Pair<String, String> doInBackground(String... params) {
                try {
                    return getVotedProductions(params[0]);
                } catch (IOException e) {
                    return null;
                }
            }

            private Pair<String, String> getVotedProductions(String name) throws IOException {
                String apiUrl = DBHOST + DBPRODUCIONESVOTADAS;

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("name", name)
                        .build();
                Request request = new Request.Builder()
                        .url(apiUrl)
                        .post(requestBody)
                        .build();

                // Ejecutar la solicitud y obtener la respuesta
                try (Response response = client.newCall(request).execute()) {
                    int responseCode = response.code();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        return Pair.create("success", responseBody);
                    } else {
                        return Pair.create("error", null);
                    }
                }
            }

            @Override
            protected void onPostExecute(Pair<String, String> result) {
                if (result != null) {
                    if (result.first.equals("success")) {
                        try {
                            JSONArray jsonArray = new JSONArray(result.second);
                            List<Production> votedProductions = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String productionName = jsonObject.getString("title");

                                String multimediaPath = jsonObject.getString("path");
                                Multimedia multimedia = new Multimedia();
                                multimedia.setPath(multimediaPath);

                                int userVote = jsonObject.getInt("votes");
                                Voto voto = new Voto(userVote);

                                String userComment = jsonObject.getString("description");
                                Comentario comentario = new Comentario(userComment);

                                Production production = new Production();
                                production.setTitulo(productionName);
                                production.setMultimedia(multimedia);
                                production.setVoto(voto);
                                production.setComentario(comentario);

                                votedProductions.add(production);
                            }
                            callback.onVotedProductionsSuccess(votedProductions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onVotedProductionsFailure();
                        }
                    } else {
                        callback.onVotedProductionsFailure();
                    }
                } else {
                    callback.onVotedProductionsFailure();
                }
            }
        }

        GetVotedProductionsAsyncTask task = new GetVotedProductionsAsyncTask();
        task.execute(name);
    }

    public static class CommentProductionTask extends AsyncTask<String, Void, String> {

        private CommentCallback callback;

        public CommentProductionTask(CommentCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String productionTitle = params[1];
            String comment = params[2];

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            // Construir los parámetros en el formato adecuado
            String requestBodyString = null;
            try {
                requestBodyString = "name=" + URLEncoder.encode(userName, "UTF-8") +
                        "&title=" + URLEncoder.encode(productionTitle, "UTF-8") +
                        "&comment=" + URLEncoder.encode(comment, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);

            Request request = new Request.Builder()
                    .url(DBHOST + DBCOMENTARPRODUCIONES)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            callback.onCommentSuccess(result);
        }
    }

    public static class DeleteCommentAndVoteTask extends AsyncTask<String, Void, String> {

        private DeleteCallback callback;

        public DeleteCommentAndVoteTask(DeleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String productionTitle = params[1];

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            String requestBodyString = null;
            try {
                requestBodyString = "name=" + URLEncoder.encode(userName, "UTF-8") +
                        "&title=" + URLEncoder.encode(productionTitle, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);

            Request request = new Request.Builder()
                    .url(DBHOST + DBELIMINARFAV)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            callback.onDeleteSuccess(result);
        }
    }

}
