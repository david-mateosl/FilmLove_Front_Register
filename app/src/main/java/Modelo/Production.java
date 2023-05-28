package Modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Production implements Serializable {
    @SerializedName("title")
    private String titulo;
    @SerializedName("director")
    private String director;
    @SerializedName("premiere")
    private String premiere;
    @SerializedName("casting")
    private String casting;
    @SerializedName("synopsis")
    private String sinopsis;
    @SerializedName("rating_average")
    private int rating_medio;

    private int total_votos;
    private List<Multimedia> multimediaList;
    private List<Genero> generoList;
    private Multimedia multimedia;
    @SerializedName("votes")

    private Voto voto;
    @SerializedName("comentario")
    private Comentario comentario;

    public Production(){

    }

    public Production(String titulo, String director, String premiere, String casting, String sinopsis, int rating_medio,int total_votos,List<Multimedia> multimediaList){
        this.titulo = titulo;
        this.director = director;
        this.premiere = premiere;
        this.casting = casting;
        this.sinopsis = sinopsis;
        this.rating_medio = rating_medio;
        this.total_votos = total_votos;
        this.multimediaList = multimediaList;
    }

    public List<Genero> getGeneroList() {
        return generoList;
    }

    public void setGeneroList(List<Genero> generoList) {
        this.generoList = generoList;
    }

    public void setTotal_votos(int total_votos) {
        this.total_votos = total_votos;
    }

    public int getTotal_votos() {
        return total_votos;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setPremiere(String premiere) {
        this.premiere = premiere;
    }

    public String getPremiere() {
        return premiere;
    }

    public void setCasting(String casting) {
        this.casting = casting;
    }

    public String getCasting() {
        return casting;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public int getRating_medio() {
        return rating_medio;
    }

    public void setRating_medio(int rating_medio) {
        this.rating_medio = rating_medio;
    }

    public List<Multimedia> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<Multimedia> multimediaList) {
        this.multimediaList = multimediaList;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    public void setVoto(Voto voto) {
        this.voto = voto;
    }

    public Voto getVoto() {
        return voto;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public Comentario getComentario() {
        return comentario;
    }

    @Override
    public String toString() {
        return "Production{" +
                "titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", premiere='" + premiere + '\'' +
                ", casting='" + casting + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", rating_medio=" + rating_medio +
                ", total_votos=" + total_votos +
                ", multimediaList=" + multimediaList +
                ", generoList=" + generoList +
                ", multimedia=" + multimedia +
                ", voto=" + voto +
                ", comentario=" + comentario +
                '}';
    }
}
