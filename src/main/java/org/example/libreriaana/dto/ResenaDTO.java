package org.example.libreriaana.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libreriaana.validation.ValidationGroups;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    
    private Long id;
    
    @NotNull(groups = {ValidationGroups.AdminValidation.class}, message = "El ID del usuario es obligatorio")
    private Long usuarioId;
    
    @NotNull(groups = {ValidationGroups.CreateValidation.class}, message = "El ID del libro es obligatorio")
    private Long libroId;
    
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;
    
    private String comentario;
    
    private LocalDateTime fechaPublicacion;
    
    // Datos adicionales para mostrar en respuestas
    private String nombreUsuario;
    private String tituloLibro;
}