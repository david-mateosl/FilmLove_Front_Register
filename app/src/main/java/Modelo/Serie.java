package Modelo;

import java.util.Date;

public class Serie {

    String titulo;
    String director;
    Date premiere;
    String casting;
    String sinopsis;
    int media_votos;
    int total_votos;

    public Serie(String titulo, String director, Date premiere, String casting,String sinopsis, int media_votos, int total_votos) {
        this.titulo = titulo;
        this.director = director;
        this.premiere = premiere;
        this.casting = casting;
        this.sinopsis = sinopsis;
        this.media_votos = media_votos;
        this.total_votos = total_votos;
    }

    public Serie(String titulo,String sinopsis, Date premiere) {
        this.titulo = titulo;
        this.premiere = premiere;
        this.sinopsis = sinopsis;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getPremiere() {
        return premiere;
    }

    public void setPremiere(Date premiere) {
        this.premiere = premiere;
    }

    public String getCasting() {
        return casting;
    }

    public void setCasting(String casting) {
        this.casting = casting;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getMedia_votos() {
        return media_votos;
    }

    public void setMedia_votos(int media_votos) {
        this.media_votos = media_votos;
    }

    public int getTotal_votos() {
        return total_votos;
    }

    public void setTotal_votos(int total_votos) {
        this.total_votos = total_votos;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", premiere=" + premiere +
                ", casting='" + casting + '\'' +
                ", media_votos=" + media_votos +
                ", total_votos=" + total_votos +
                '}';
    }

}
