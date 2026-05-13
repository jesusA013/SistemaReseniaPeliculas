/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author golea
 */
public class ActorDTO {

    private int id;
    private String nombre;
    private String biografia;
    private String rutaPerfil;

    public ActorDTO() {
    }

    public ActorDTO(int id, String nombre, String biografia) {
        this.id = id;
        this.nombre = nombre;
        this.biografia = biografia;
    }

    public String getRutaPerfil() {
        return rutaPerfil;
    }

    public void setRutaPerfil(String rutaPerfil) {
        this.rutaPerfil = rutaPerfil;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
}

