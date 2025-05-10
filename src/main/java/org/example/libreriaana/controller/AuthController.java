package org.example.libreriaana.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.JwtResponseDTO;
import org.example.libreriaana.dto.LoginRequestDTO;
import org.example.libreriaana.dto.RefreshTokenRequestDTO;
import org.example.libreriaana.dto.UsuarioRegistroDTO;
import org.example.libreriaana.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponseDTO jwtResponse = authService.login(loginRequestDTO);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/registro")
    public ResponseEntity<JwtResponseDTO> registro(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        JwtResponseDTO jwtResponse = authService.registro(registroDTO);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequest) {
        JwtResponseDTO jwtResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}