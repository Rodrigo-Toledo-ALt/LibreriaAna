package org.example.libreriaana.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.LibroDTO;
import org.example.libreriaana.exception.DuplicateResourceException;
import org.example.libreriaana.exception.ResourceNotFoundException;
import org.example.libreriaana.mapper.LibroMapper;
import org.example.libreriaana.model.Libro;
import org.example.libreriaana.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LibroService {

    @Autowired
    private final LibroRepository libroRepository;
    @Autowired
    private final LibroMapper libroMapper;

    @Transactional(readOnly = true)
    public List<LibroDTO> obtenerTodos() {
        return libroRepository.findAll().stream()
                .map(libroMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LibroDTO obtenerPorId(Long id) {
        Libro libro = getLibroById(id);
        return libroMapper.toDTO(libro);
    }

    @Transactional(readOnly = true)
    public LibroDTO obtenerPorIsbn(String isbn) {
        Libro libro = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", "isbn", isbn));
        return libroMapper.toDTO(libro);
    }

    @Transactional
    public LibroDTO crear(LibroDTO libroDTO) {
        // Verificar si ya existe un libro con el ISBN proporcionado
        if (libroDTO.getIsbn() != null && libroRepository.findByIsbn(libroDTO.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Libro", "isbn", libroDTO.getIsbn());
        }

        // Convertir DTO a entidad y guardar
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);

        return libroMapper.toDTO(libro);
    }

    @Transactional
    public LibroDTO actualizar(Long id, LibroDTO libroDTO) {
        // Obtener libro existente
        Libro libro = getLibroById(id);
        
        // Verificar posible duplicación de ISBN
        if (libroDTO.getIsbn() != null && !libroDTO.getIsbn().equals(libro.getIsbn()) 
                && libroRepository.findByIsbn(libroDTO.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Libro", "isbn", libroDTO.getIsbn());
        }

        // Actualizar libro y guardar
        libroMapper.updateLibroFromDTO(libro, libroDTO);
        libro = libroRepository.save(libro);

        return libroMapper.toDTO(libro);
    }

    @Transactional
    public void eliminar(Long id) {
        // Verificar que el libro existe
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro", "id", id);
        }

        libroRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LibroDTO> buscarPorCriterios(String titulo, String autor, String categoria) {
        return libroRepository.buscarLibros(titulo, autor, categoria).stream()
                .map(libroMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LibroDTO> obtenerLibrosDisponibles() {
        return libroRepository.findByDisponibleTrue().stream()
                .map(libroMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void actualizarDisponibilidad(Long id, boolean disponible) {
        Libro libro = getLibroById(id);
        libro.setDisponible(disponible);
        libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public Libro getLibroById(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", "id", id));
    }
}