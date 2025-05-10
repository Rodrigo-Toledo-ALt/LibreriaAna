package org.example.libreriaana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {
    
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    @NotBlank(message = "El autor es obligatorio")
    private String autor;
    
    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", 
            message = "Formato de ISBN inválido")
    private String isbn;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @Positive(message = "El año de publicación debe ser positivo")
    private Integer anioPublicacion;
    
    private String editorial;
    
    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;
    
    private String imagenPortada;
    
    private String descripcion;
}