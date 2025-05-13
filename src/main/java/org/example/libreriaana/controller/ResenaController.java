package org.example.libreriaana.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.ResenaDTO;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.service.ResenaService;
import org.example.libreriaana.service.UsuarioService;
import org.example.libreriaana.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
@AllArgsConstructor
public class ResenaController {

    private  ResenaService resenaService;
    private  UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<ResenaDTO>> obtenerTodas() {
        List<ResenaDTO> resenas = resenaService.obtenerTodas();
        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaDTO> obtenerPorId(@PathVariable Long id) {
        ResenaDTO resena = resenaService.obtenerPorId(id);
        return ResponseEntity.ok(resena);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResenaDTO> crear(@Validated(ValidationGroups.CreateValidation.class) @RequestBody ResenaDTO resenaDTO) {
        // Si el usuario no es admin, forzar el ID del usuario actual
        Usuario usuarioActual = usuarioService.getUsuarioActual();
        if (usuarioActual.getRol() != Usuario.Rol.ADMIN || resenaDTO.getUsuarioId() == null) {
            resenaDTO.setUsuarioId(usuarioActual.getId());
        }
        
        ResenaDTO nuevaResena = resenaService.crear(resenaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaResena);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @resenaService.esAutorDeResena(#id, @usuarioService.getUsuarioActual().getId())")
    public ResponseEntity<ResenaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaDTO resenaDTO) {
        ResenaDTO resenaActualizada = resenaService.actualizar(id, resenaDTO);
        return ResponseEntity.ok(resenaActualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @resenaService.esAutorDeResena(#id, @usuarioService.getUsuarioActual().getId())")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/libro/{id}")
    public ResponseEntity<List<ResenaDTO>> obtenerPorLibro(@PathVariable Long id) {
        List<ResenaDTO> resenas = resenaService.obtenerPorLibro(id);
        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/libro/{id}/puntuacion")
    public ResponseEntity<List<ResenaDTO>> obtenerPorLibroYPuntuacion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer puntuacion) {
        List<ResenaDTO> resenas = resenaService.obtenerPorLibroYPuntuacionMinima(id, puntuacion);
        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @usuarioService.getUsuarioActual().getId()")
    public ResponseEntity<List<ResenaDTO>> obtenerPorUsuario(@PathVariable Long id) {
        List<ResenaDTO> resenas = resenaService.obtenerPorUsuario(id);
        return ResponseEntity.ok(resenas);
    }
}