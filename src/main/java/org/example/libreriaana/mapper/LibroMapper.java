package org.example.libreriaana.mapper;

import org.example.libreriaana.dto.LibroDTO;
import org.example.libreriaana.model.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {
    
    public LibroDTO toDTO(Libro libro) {
        if (libro == null) {
            return null;
        }
        
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        dto.setIsbn(libro.getIsbn());
        dto.setCategoria(libro.getCategoria());
        dto.setAnioPublicacion(libro.getAnioPublicacion());
        dto.setEditorial(libro.getEditorial());
        dto.setDisponible(libro.getDisponible());
        dto.setImagenPortada(libro.getImagenPortada());
        dto.setDescripcion(libro.getDescripcion());
        
        return dto;
    }
    
    public Libro toEntity(LibroDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Libro libro = new Libro();
        updateLibroFromDTO(libro, dto);
        
        return libro;
    }
    
    public void updateLibroFromDTO(Libro libro, LibroDTO dto) {
        if (dto == null || libro == null) {
            return;
        }
        
        if (dto.getTitulo() != null) {
            libro.setTitulo(dto.getTitulo());
        }
        
        if (dto.getAutor() != null) {
            libro.setAutor(dto.getAutor());
        }
        
        if (dto.getIsbn() != null) {
            libro.setIsbn(dto.getIsbn());
        }
        
        if (dto.getCategoria() != null) {
            libro.setCategoria(dto.getCategoria());
        }
        
        if (dto.getAnioPublicacion() != null) {
            libro.setAnioPublicacion(dto.getAnioPublicacion());
        }
        
        if (dto.getEditorial() != null) {
            libro.setEditorial(dto.getEditorial());
        }
        
        if (dto.getDisponible() != null) {
            libro.setDisponible(dto.getDisponible());
        }
        
        if (dto.getImagenPortada() != null) {
            libro.setImagenPortada(dto.getImagenPortada());
        }
        
        if (dto.getDescripcion() != null) {
            libro.setDescripcion(dto.getDescripcion());
        }
    }
}