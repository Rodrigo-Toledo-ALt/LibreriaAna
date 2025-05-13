package org.example.libreriaana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libreriaana.validation.ValidationGroups.CreateValidation;



// Modifica tu LibroDTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {

    private Long id;

    @NotBlank(groups = CreateValidation.class, message = "El título es obligatorio")
    private String titulo;

    @NotBlank(groups = CreateValidation.class, message = "El autor es obligatorio")
    private String autor;

    @NotBlank(groups = CreateValidation.class, message = "El ISBN es obligatorio")
    @Pattern(regexp = "...", message = "Formato de ISBN inválido")
    private String isbn;

    @NotBlank(groups = CreateValidation.class, message = "La categoría es obligatoria")
    private String categoria;

    @Positive(message = "El año de publicación debe ser positivo")
    private Integer anioPublicacion;

    private String editorial;

    @NotNull(groups = CreateValidation.class, message = "La disponibilidad es obligatoria")
    private Boolean disponible;

    private String imagenPortada;

    private String descripcion;
}