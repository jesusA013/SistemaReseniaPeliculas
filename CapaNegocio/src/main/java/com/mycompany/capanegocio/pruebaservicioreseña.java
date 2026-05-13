/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capanegocio;

import dtos.PeliculasDTO;
import dtos.ResenaDTO;
import dtos.UsuarioDTO;
import servicios.ResenaService;


/**
 *
 * @author golea
 */
public class pruebaservicioreseña {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
// 1. Instanciar el servicio
        ResenaService resenaService = new ResenaService();

        // 2. Crear un UsuarioDTO simulado (Debe existir en la BD un ID coincidente)
        UsuarioDTO usuarioDto = new UsuarioDTO();
        usuarioDto.setId(1); // Cambia por un ID que exista en tu tabla 'usuarios'
        usuarioDto.setNombre("Usuario de Prueba");

        // 3. Crear un PeliculasDTO simulado (Debe existir en la BD un ID coincidente)
        PeliculasDTO peliculaDto = new PeliculasDTO();
        peliculaDto.setIdPelicula(4); // Cambia por un ID que exista en tu tabla 'peliculas'
        peliculaDto.setTitulo("Pelicula de Prueba");

        // 4. Crear el ResenaDTO con los datos de la reseña
        ResenaDTO resenaDto = new ResenaDTO();
        resenaDto.setUsuario(usuarioDto);
        resenaDto.setPelicula(peliculaDto);
        resenaDto.setCalificacion(9); // Calificación válida entre 1 y 10
        resenaDto.setComentario("¡Excelente película! La actuación fue impecable.");

        try {
            System.out.println("Intentando publicar reseña...");
            
            // 5. Ejecutar el método de negocio
            resenaService.publicarResena(resenaDto);
            
            System.out.println("✅ Reseña publicada o actualizada con éxito.");
            
        } catch (Exception e) {
            System.err.println("❌ Error al publicar la reseña: " + e.getMessage());
            e.printStackTrace();
        }
    }
}