package org.example.libreriaana.mapper;

import org.example.libreriaana.dto.PrestamoDTO;
import org.example.libreriaana.model.Libro;
import org.example.libreriaana.model.Prestamo;
import org.example.libreriaana.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {
    
    public PrestamoDTO toDTO(Prestamo prestamo) {
        if (prestamo == null) {
            return null;
        }
        
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(prestamo.getId());
        
        if (prestamo.getUsuario() != null) {
            dto.setUsuarioId(prestamo.getUsuario().getId());
            dto.setNombreUsuario(prestamo.getUsuario().getNombre() + " " + prestamo.getUsuario().getApellidos());
        }
        
        if (prestamo.getLibro() != null) {
            dto.setLibroId(prestamo.getLibro().getId());
            dto.setTituloLibro(prestamo.getLibro().getTitulo());
            dto.setAutorLibro(prestamo.getLibro().getAutor());
        }
        
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucionPrevista(prestamo.getFechaDevolucionPrevista());
        dto.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());
        dto.setEstado(prestamo.getEstado());
        
        return dto;
    }
    
    public Prestamo toEntity(PrestamoDTO dto, Usuario usuario, Libro libro) {
        if (dto == null) {
            return null;
        }
        
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        
        if (dto.getFechaDevolucionPrevista() != null) {
            prestamo.setFechaDevolucionPrevista(dto.getFechaDevolucionPrevista());
        }
        
        if (dto.getEstado() != null) {
            prestamo.setEstado(dto.getEstado());
        } else {
            prestamo.setEstado(Prestamo.Estado.ACTIVO);
        }
        
        return prestamo;
    }
    
    public void updatePrestamoFromDTO(Prestamo prestamo, PrestamoDTO dto) {
        if (dto == null || prestamo == null) {
            return;
        }
        
        if (dto.getFechaDevolucionPrevista() != null) {
            prestamo.setFechaDevolucionPrevista(dto.getFechaDevolucionPrevista());
        }
        
        if (dto.getFechaDevolucionReal() != null) {
            prestamo.setFechaDevolucionReal(dto.getFechaDevolucionReal());
        }
        
        if (dto.getEstado() != null) {
            prestamo.setEstado(dto.getEstado());
        }
    }
}