package org.example.libreriaana.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.PrestamoDTO;
import org.example.libreriaana.exception.BadRequestException;
import org.example.libreriaana.exception.BookNotAvailableException;
import org.example.libreriaana.exception.ResourceNotFoundException;
import org.example.libreriaana.exception.UnauthorizedException;
import org.example.libreriaana.mapper.PrestamoMapper;
import org.example.libreriaana.model.Libro;
import org.example.libreriaana.model.Prestamo;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrestamoService {

    @Autowired
    private final PrestamoRepository prestamoRepository;
    @Autowired
    private final UsuarioService usuarioService;
    @Autowired
    private final LibroService libroService;
    @Autowired
    private final PrestamoMapper prestamoMapper;

    @Transactional(readOnly = true)
    public List<PrestamoDTO> obtenerTodos() {
        return prestamoRepository.findAll().stream()
                .map(prestamoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrestamoDTO obtenerPorId(Long id) {
        Prestamo prestamo = getPrestamoById(id);
        return prestamoMapper.toDTO(prestamo);
    }

    @Transactional(readOnly = true)
    public List<PrestamoDTO> obtenerPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        return prestamoRepository.findByUsuario(usuario).stream()
                .map(prestamoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrestamoDTO> obtenerPorEstado(Prestamo.Estado estado) {
        return prestamoRepository.findByEstado(estado).stream()
                .map(prestamoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PrestamoDTO crear(PrestamoDTO prestamoDTO) {
        // Obtener usuario y libro
        Usuario usuario = usuarioService.getUsuarioById(prestamoDTO.getUsuarioId());
        Libro libro = libroService.getLibroById(prestamoDTO.getLibroId());

        // Verificar disponibilidad del libro
        if (!libro.getDisponible()) {
            throw new BookNotAvailableException(libro.getId());
        }

        // Verificar si ya hay un préstamo activo para este libro
        if (prestamoRepository.existsByLibroIdAndEstado(libro.getId(), Prestamo.Estado.ACTIVO)) {
            throw new BadRequestException("El libro ya está prestado");
        }

        // Crear el préstamo
        Prestamo prestamo = prestamoMapper.toEntity(prestamoDTO, usuario, libro);
        
        // Actualizar disponibilidad del libro
        libro.setDisponible(false);
        
        // Guardar el préstamo
        prestamo = prestamoRepository.save(prestamo);

        return prestamoMapper.toDTO(prestamo);
    }

    @Transactional
    public PrestamoDTO actualizar(Long id, PrestamoDTO prestamoDTO) {
        // Obtener préstamo existente
        Prestamo prestamo = getPrestamoById(id);
        
        // Actualizar préstamo
        prestamoMapper.updatePrestamoFromDTO(prestamo, prestamoDTO);
        
        // Si se está devolviendo el libro, actualizar su disponibilidad
        if (prestamoDTO.getEstado() == Prestamo.Estado.DEVUELTO) {
            prestamo.setFechaDevolucionReal(LocalDateTime.now());
            prestamo.getLibro().setDisponible(true);
        }
        
        // Guardar cambios
        prestamo = prestamoRepository.save(prestamo);

        return prestamoMapper.toDTO(prestamo);
    }

    @Transactional
    public void eliminar(Long id) {
        // Verificar que el préstamo existe
        Prestamo prestamo = getPrestamoById(id);
        
        // Si el préstamo está activo, volver a hacer disponible el libro
        if (prestamo.getEstado() == Prestamo.Estado.ACTIVO) {
            prestamo.getLibro().setDisponible(true);
        }
        
        prestamoRepository.deleteById(id);
    }

    @Transactional
    public PrestamoDTO devolverLibro(Long id) {
        // Obtener préstamo
        Prestamo prestamo = getPrestamoById(id);
        
        // Verificar que el préstamo está activo
        if (prestamo.getEstado() != Prestamo.Estado.ACTIVO) {
            throw new BadRequestException("El préstamo no está activo");
        }
        
        // Actualizar préstamo
        prestamo.setEstado(Prestamo.Estado.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDateTime.now());
        
        // Actualizar disponibilidad del libro
        prestamo.getLibro().setDisponible(true);
        
        // Guardar cambios
        prestamo = prestamoRepository.save(prestamo);
        
        return prestamoMapper.toDTO(prestamo);
    }

    @Transactional
    public void verificarPrestamosVencidos() {
        // Obtener préstamos activos
        List<Prestamo> prestamosActivos = prestamoRepository.findByEstado(Prestamo.Estado.ACTIVO);
        
        // Verificar cuáles están vencidos
        LocalDateTime hoy = LocalDateTime.now();
        prestamosActivos.stream()
            .filter(p -> p.getFechaDevolucionPrevista().isBefore(hoy))
            .forEach(p -> {
                p.setEstado(Prestamo.Estado.RETRASADO);
                prestamoRepository.save(p);
            });
    }

    @Transactional(readOnly = true)
    public Prestamo getPrestamoById(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo", "id", id));
    }

    @Transactional(readOnly = true)
    public void verificarPropietario(Long prestamoId, Long usuarioId) {
        Prestamo prestamo = getPrestamoById(prestamoId);
        
        // Verificar si el usuario es el propietario del préstamo
        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            throw new UnauthorizedException("No tiene permiso para acceder a este préstamo");
        }
    }
}