package Modelo;

import java.io.Serializable;

public class Voto implements Serializable {

    int voto;

    public Voto(int voto) {
        this.voto = voto;
    }

    public Voto() {
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "voto=" + voto +
                '}';
    }
}
