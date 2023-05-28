package Callback;

import java.util.List;

import Modelo.Pelicula;

public interface FilmsCallback {
    void onFilmsLoaded(List<Pelicula> films);
    void onFilmsLoadError();
}
