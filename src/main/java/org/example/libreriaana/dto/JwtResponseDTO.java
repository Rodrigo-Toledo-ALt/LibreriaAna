package org.example.libreriaana.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {
    
    private String token;
    private String refreshToken;
    private String tipo = "Bearer";
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}