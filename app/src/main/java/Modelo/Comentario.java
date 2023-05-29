package Modelo;

import java.io.Serializable;

public class Comentario implements Serializable {

    String comentario;


    public Comentario() {

    }


    public Comentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "comentario='" + comentario + '\'' +
                '}';
    }
}
