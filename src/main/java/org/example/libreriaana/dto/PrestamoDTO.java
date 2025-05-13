package org.example.libreriaana.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libreriaana.model.Prestamo;
import org.example.libreriaana.validation.ValidationGroups.AdminValidation;
import org.example.libreriaana.validation.ValidationGroups.CreateValidation;
import org.example.libreriaana.validation.ValidationGroups.UpdateValidation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoDTO {

    private Long id;

    @NotNull(groups = {AdminValidation.class}, message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(groups = {CreateValidation.class}, message = "El ID del libro es obligatorio")
    private Long libroId;

    private LocalDateTime fechaPrestamo;

    @FutureOrPresent(groups = {CreateValidation.class, UpdateValidation.class},
            message = "La fecha de devolución prevista debe ser actual o futura")
    private LocalDateTime fechaDevolucionPrevista;

    private LocalDateTime fechaDevolucionReal;

    private Prestamo.Estado estado;

    // Datos adicionales para mostrar en respuestas
    private String tituloLibro;
    private String autorLibro;
    private String nombreUsuario;
}