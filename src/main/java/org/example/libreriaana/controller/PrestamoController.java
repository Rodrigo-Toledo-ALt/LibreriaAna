package org.example.libreriaana.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.PrestamoDTO;
import org.example.libreriaana.model.Prestamo;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.service.PrestamoService;
import org.example.libreriaana.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@AllArgsConstructor
public class PrestamoController {

    private  PrestamoService prestamoService;
    private  UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<PrestamoDTO>> obtenerTodos() {
        Usuario usuarioActual = usuarioService.getUsuarioActual();
        
        List<PrestamoDTO> prestamos;
        if (usuarioActual.getRol() == Usuario.Rol.ADMIN) {
            prestamos = prestamoService.obtenerTodos();
        } else {
            prestamos = prestamoService.obtenerPorUsuario(usuarioActual.getId());
        }
        
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @prestamoService.getPrestamoById(#id).getUsuario().getId() == @usuarioService.getUsuarioActual().getId()")
    public ResponseEntity<PrestamoDTO> obtenerPorId(@PathVariable Long id) {
        PrestamoDTO prestamo = prestamoService.obtenerPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PrestamoDTO> crear(@Valid @RequestBody PrestamoDTO prestamoDTO) {
        // Si el usuario no es admin, forzar el ID del usuario actual
        Usuario usuarioActual = usuarioService.getUsuarioActual();
        if (usuarioActual.getRol() != Usuario.Rol.ADMIN) {
            prestamoDTO.setUsuarioId(usuarioActual.getId());
        }
        
        PrestamoDTO nuevoPrestamo = prestamoService.crear(prestamoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrestamo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @prestamoService.getPrestamoById(#id).getUsuario().getId() == @usuarioService.getUsuarioActual().getId()")
    public ResponseEntity<PrestamoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO prestamoDTO) {
        PrestamoDTO prestamoActualizado = prestamoService.actualizar(id, prestamoDTO);
        return ResponseEntity.ok(prestamoActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @usuarioService.getUsuarioActual().getId()")
    public ResponseEntity<List<PrestamoDTO>> obtenerPorUsuario(@PathVariable Long id) {
        List<PrestamoDTO> prestamos = prestamoService.obtenerPorUsuario(id);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestamoDTO>> obtenerPorEstado(@PathVariable Prestamo.Estado estado) {
        List<PrestamoDTO> prestamos = prestamoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(prestamos);
    }

    @PostMapping("/{id}/devolver")
    @PreAuthorize("hasRole('ADMIN') or @prestamoService.getPrestamoById(#id).getUsuario().getId() == @usuarioService.getUsuarioActual().getId()")
    public ResponseEntity<PrestamoDTO> devolverLibro(@PathVariable Long id) {
        PrestamoDTO prestamo = prestamoService.devolverLibro(id);
        return ResponseEntity.ok(prestamo);
    }
}