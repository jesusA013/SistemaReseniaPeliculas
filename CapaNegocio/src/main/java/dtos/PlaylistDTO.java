/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.util.List;

/**
 *
 * @author golea
 */
public class PlaylistDTO {

    private int id;
    private String nombre;
    private boolean esPublica;
    private UsuarioDTO usuario;
    private List<PeliculasDTO> peliculas;

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEsPublica() {
        return esPublica;
    }

    public void setEsPublica(boolean esPublica) {
        this.esPublica = esPublica;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PeliculasDTO> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<PeliculasDTO> peliculas) {
        this.peliculas = peliculas;
    }
}

