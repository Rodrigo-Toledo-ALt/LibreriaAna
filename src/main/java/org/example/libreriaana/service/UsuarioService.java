package org.example.libreriaana.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.UsuarioDTO;
import org.example.libreriaana.dto.UsuarioRegistroDTO;
import org.example.libreriaana.exception.DuplicateResourceException;
import org.example.libreriaana.exception.ResourceNotFoundException;
import org.example.libreriaana.exception.UnauthorizedException;
import org.example.libreriaana.mapper.UsuarioMapper;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        return usuarioMapper.toDTO(getUsuarioById(id));
    }

    @Transactional
    public UsuarioDTO registrar(UsuarioRegistroDTO registroDTO) {
        // Verificar si ya existe un usuario con el email proporcionado
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", registroDTO.getEmail());
        }

        // Convertir DTO a entidad y guardar
        Usuario usuario = usuarioMapper.toEntity(registroDTO);
        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(usuario);
    }


    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        // Obtener usuario existente
        Usuario usuario = getUsuarioById(id);
        
        // Verificar posible duplicación de email
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuario.getEmail()) 
                && usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", usuarioDTO.getEmail());
        }

        // Actualizar usuario y guardar
        usuarioMapper.updateUsuarioFromDTO(usuario, usuarioDTO);
        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }

        usuarioRepository.deleteById(id);
    }

    @Transactional
    public UsuarioDTO actualizarRol(Long id, Usuario.Rol rol) {
        Usuario usuario = getUsuarioById(id);
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioActual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        } else {
            throw new UnauthorizedException("No hay usuario autenticado");
        }
    }

    @Transactional
    public void cambiarPassword(Long id, String oldPassword, String newPassword) {
        Usuario usuario = getUsuarioById(id);
        
        // Verificar que la contraseña anterior es correcta
        if (!passwordEncoder.matches(oldPassword, usuario.getPassword())) {
            throw new UnauthorizedException("Contraseña actual incorrecta");
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }
}