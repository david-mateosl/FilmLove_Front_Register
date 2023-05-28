package Callback;

import java.util.List;

import Modelo.Serie;

public interface SeriesCallback {
    void onSerieLoaded(Serie serie);

    void onSeriesLoaded(List<Serie> series);

    void onSeriesLoadError();
}
