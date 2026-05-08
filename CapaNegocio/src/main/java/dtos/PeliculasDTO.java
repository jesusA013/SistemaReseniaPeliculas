/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.util.Date;
import java.util.List;

/**
 *
 * @author golea
 */
public class PeliculasDTO {

    private int idPelicula;
    private int tmdbId;
    private String titulo;
    private String sinopsis;
    private Date fechaEstreno;
    private float ratingPromedio;
    private List<ActorDTO> listaActores;

    public PeliculasDTO() {
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Date getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(Date fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public List<ActorDTO> getListaActores() {
        return listaActores;
    }

    public void setListaActores(List<ActorDTO> listaActores) {
        this.listaActores = listaActores;
    }
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public float getRatingPromedio() {
        return ratingPromedio;
    }

    public void setRatingPromedio(float ratingPromedio) {
        this.ratingPromedio = ratingPromedio;
    }
}
