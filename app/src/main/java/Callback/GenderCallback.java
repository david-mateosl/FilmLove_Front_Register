package Callback;

import java.util.List;

import Modelo.Genero;

public interface GenderCallback {
    void onGendersLoaded(List<Genero> generoList);
    void onGenderLoadError();
}
