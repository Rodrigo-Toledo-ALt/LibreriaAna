package org.example.libreriaana.mapper;

import org.example.libreriaana.dto.UsuarioDTO;
import org.example.libreriaana.dto.UsuarioRegistroDTO;
import org.example.libreriaana.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        
        return dto;
    }
    
    public Usuario toEntity(UsuarioRegistroDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(Usuario.Rol.USER); // Por defecto, los nuevos usuarios tienen rol USER
        
        return usuario;
    }
    
    public void updateUsuarioFromDTO(Usuario usuario, UsuarioDTO dto) {
        if (dto == null || usuario == null) {
            return;
        }
        
        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        
        if (dto.getApellidos() != null) {
            usuario.setApellidos(dto.getApellidos());
        }
        
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        
        // No actualizamos la contraseña ni el rol desde el DTO básico por seguridad
    }
}