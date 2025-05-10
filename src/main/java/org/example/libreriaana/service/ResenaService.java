package org.example.libreriaana.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.ResenaDTO;
import org.example.libreriaana.exception.BadRequestException;
import org.example.libreriaana.exception.ResourceNotFoundException;
import org.example.libreriaana.exception.UnauthorizedException;
import org.example.libreriaana.mapper.ResenaMapper;
import org.example.libreriaana.model.Libro;
import org.example.libreriaana.model.Resena;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResenaService {

    @Autowired
    private final ResenaRepository resenaRepository;
    @Autowired
    private final UsuarioService usuarioService;
    @Autowired
    private final LibroService libroService;
    @Autowired
    private final ResenaMapper resenaMapper;

    @Transactional(readOnly = true)
    public List<ResenaDTO> obtenerTodas() {
        return resenaRepository.findAll().stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResenaDTO obtenerPorId(Long id) {
        Resena resena = getResenaById(id);
        return resenaMapper.toDTO(resena);
    }

    @Transactional(readOnly = true)
    public List<ResenaDTO> obtenerPorLibro(Long libroId) {
        return resenaRepository.findByLibroId(libroId).stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResenaDTO> obtenerPorUsuario(Long usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId).stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResenaDTO> obtenerPorLibroYPuntuacionMinima(Long libroId, Integer puntuacionMinima) {
        return resenaRepository.findByLibroIdAndPuntuacionMinima(libroId, puntuacionMinima).stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResenaDTO crear(ResenaDTO resenaDTO) {
        // Obtener usuario y libro
        Usuario usuario = usuarioService.getUsuarioById(resenaDTO.getUsuarioId());
        Libro libro = libroService.getLibroById(resenaDTO.getLibroId());

        // Verificar si el usuario ya ha hecho una reseña para este libro
        if (resenaRepository.findByUsuarioIdAndLibroId(usuario.getId(), libro.getId()).isPresent()) {
            throw new BadRequestException("Ya has realizado una reseña para este libro");
        }

        // Crear la reseña
        Resena resena = resenaMapper.toEntity(resenaDTO, usuario, libro);
        
        // Guardar la reseña
        resena = resenaRepository.save(resena);

        return resenaMapper.toDTO(resena);
    }

    @Transactional
    public ResenaDTO actualizar(Long id, ResenaDTO resenaDTO) {
        // Obtener reseña existente
        Resena resena = getResenaById(id);
        
        // Actualizar reseña
        resenaMapper.updateResenaFromDTO(resena, resenaDTO);
        
        // Guardar cambios
        resena = resenaRepository.save(resena);

        return resenaMapper.toDTO(resena);
    }

    @Transactional
    public void eliminar(Long id) {
        // Verificar que la reseña existe
        if (!resenaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reseña", "id", id);
        }

        resenaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Resena getResenaById(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña", "id", id));
    }

    @Transactional(readOnly = true)
    public boolean esAutorDeResena(Long resenaId, Long usuarioId) {
        Resena resena = getResenaById(resenaId);
        return resena.getUsuario().getId().equals(usuarioId);
    }

    @Transactional(readOnly = true)
    public void verificarAutor(Long resenaId, Long usuarioId) {
        if (!esAutorDeResena(resenaId, usuarioId)) {
            throw new UnauthorizedException("No tiene permiso para modificar esta reseña");
        }
    }
}