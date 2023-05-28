package Modelo;

import java.io.Serializable;

public class Genero implements Serializable {

    private String name;

    public Genero() {
    }

    public Genero(int id, String name) {
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
