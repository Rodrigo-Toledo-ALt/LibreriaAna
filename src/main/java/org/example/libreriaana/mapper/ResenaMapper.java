package org.example.libreriaana.mapper;

import org.example.libreriaana.dto.ResenaDTO;
import org.example.libreriaana.model.Libro;
import org.example.libreriaana.model.Resena;
import org.example.libreriaana.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ResenaMapper {
    
    public ResenaDTO toDTO(Resena resena) {
        if (resena == null) {
            return null;
        }
        
        ResenaDTO dto = new ResenaDTO();
        dto.setId(resena.getId());
        
        if (resena.getUsuario() != null) {
            dto.setUsuarioId(resena.getUsuario().getId());
            dto.setNombreUsuario(resena.getUsuario().getNombre() + " " + resena.getUsuario().getApellidos());
        }
        
        if (resena.getLibro() != null) {
            dto.setLibroId(resena.getLibro().getId());
            dto.setTituloLibro(resena.getLibro().getTitulo());
        }
        
        dto.setPuntuacion(resena.getPuntuacion());
        dto.setComentario(resena.getComentario());
        dto.setFechaPublicacion(resena.getFechaPublicacion());
        
        return dto;
    }
    
    public Resena toEntity(ResenaDTO dto, Usuario usuario, Libro libro) {
        if (dto == null) {
            return null;
        }
        
        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setLibro(libro);
        resena.setPuntuacion(dto.getPuntuacion());
        resena.setComentario(dto.getComentario());
        
        return resena;
    }
    
    public void updateResenaFromDTO(Resena resena, ResenaDTO dto) {
        if (dto == null || resena == null) {
            return;
        }
        
        if (dto.getPuntuacion() != null) {
            resena.setPuntuacion(dto.getPuntuacion());
        }
        
        if (dto.getComentario() != null) {
            resena.setComentario(dto.getComentario());
        }
    }
}