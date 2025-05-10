package org.example.libreriaana.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.LibroDTO;
import org.example.libreriaana.dto.ResenaDTO;
import org.example.libreriaana.service.LibroService;
import org.example.libreriaana.service.ResenaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@AllArgsConstructor
public class LibroController {

    private  LibroService libroService;
    private  ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<LibroDTO>> obtenerTodos() {
        List<LibroDTO> libros = libroService.obtenerTodos();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> obtenerPorId(@PathVariable Long id) {
        LibroDTO libro = libroService.obtenerPorId(id);
        return ResponseEntity.ok(libro);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroDTO> crear(@Valid @RequestBody LibroDTO libroDTO) {
        LibroDTO nuevoLibro = libroService.crear(libroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroDTO libroDTO) {
        LibroDTO libroActualizado = libroService.actualizar(id, libroDTO);
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroDTO>> obtenerLibrosDisponibles() {
        List<LibroDTO> librosDisponibles = libroService.obtenerLibrosDisponibles();
        return ResponseEntity.ok(librosDisponibles);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroDTO>> buscarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria) {
        List<LibroDTO> libros = libroService.buscarPorCriterios(titulo, autor, categoria);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}/resenas")
    public ResponseEntity<List<ResenaDTO>> obtenerResenasPorLibro(@PathVariable Long id) {
        List<ResenaDTO> resenas = resenaService.obtenerPorLibro(id);
        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/{id}/resenas/puntuacion")
    public ResponseEntity<List<ResenaDTO>> obtenerResenasPorLibroYPuntuacion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer puntuacion) {
        List<ResenaDTO> resenas = resenaService.obtenerPorLibroYPuntuacionMinima(id, puntuacion);
        return ResponseEntity.ok(resenas);
    }
}