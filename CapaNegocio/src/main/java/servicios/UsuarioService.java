/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.UsuarioDAO;
import Entidades.Usuario;
import dtos.UsuarioDTO;
import interfaces.IUsuarioService;

/**
 *
 * @author golea
 */
public class UsuarioService implements IUsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void registrar(UsuarioDTO dto) throws Exception {
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new Exception("Email inválido.");
        }
        if (usuarioDAO.buscarPorEmail(dto.getEmail()) != null) {
            throw new Exception("El usuario ya existe.");
        }

        Usuario entidad = new Usuario();
        entidad.setNombre(dto.getNombre());
        entidad.setEmail(dto.getEmail());
        entidad.setPassword(dto.getPassword());

        usuarioDAO.guardarUsuario(entidad);
    }

    @Override
    public UsuarioDTO iniciarSesion(String email, String password) {
        Usuario u = usuarioDAO.buscarPorEmail(email);

        if (u != null && u.getPassword().equals(password)) {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(u.getId());
            dto.setNombre(u.getNombre());
            dto.setEmail(u.getEmail());
            return dto;
        }
        return null;
    }
}
