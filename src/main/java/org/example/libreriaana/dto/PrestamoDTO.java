package org.example.libreriaana.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libreriaana.model.Prestamo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoDTO {
    
    private Long id;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
    
    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;
    
    private LocalDateTime fechaPrestamo;
    
    @FutureOrPresent(message = "La fecha de devolución prevista debe ser actual o futura")
    private LocalDateTime fechaDevolucionPrevista;

    private LocalDateTime fechaDevolucionReal;
    
    private Prestamo.Estado estado;
    
    // Datos adicionales para mostrar en respuestas
    private String tituloLibro;
    private String autorLibro;
    private String nombreUsuario;
}