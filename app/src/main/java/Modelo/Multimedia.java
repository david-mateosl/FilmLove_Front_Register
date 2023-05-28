package Modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Multimedia implements Serializable {
    @SerializedName("path")
    private String path;
    @SerializedName("type")
    private String tipo;

    public Multimedia() {

    }

    public Multimedia(String path, String tipo) {
        this.path = path;
        this.tipo = tipo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Multimedia{" +
                "path='" + path + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
