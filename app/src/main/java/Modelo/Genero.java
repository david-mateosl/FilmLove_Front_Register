package Modelo;

import android.os.Parcelable;

import java.io.Serializable;

public class Genero implements Serializable {

    private String name;

    public Genero() {
    }
    public Genero(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genero [name=" + name + "]";
    }
}
