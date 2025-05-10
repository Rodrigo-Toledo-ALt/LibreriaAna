package org.example.libreriaana.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.libreriaana.dto.JwtResponseDTO;
import org.example.libreriaana.dto.LoginRequestDTO;
import org.example.libreriaana.dto.RefreshTokenRequestDTO;
import org.example.libreriaana.dto.UsuarioRegistroDTO;
import org.example.libreriaana.exception.UnauthorizedException;
import org.example.libreriaana.model.Usuario;
import org.example.libreriaana.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtTokenProvider tokenProvider;
    @Autowired
    private final UsuarioService usuarioService;

    @Transactional
    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Obtener el usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        // Generar tokens
        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(usuario);
        
        // Construir respuesta
        return JwtResponseDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .tipo("Bearer")
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }

    @Transactional
    public JwtResponseDTO registro(UsuarioRegistroDTO registroDTO) {
        // Registrar al usuario
        usuarioService.registrar(registroDTO);
        
        // Iniciar sesión automáticamente
        LoginRequestDTO loginRequest = new LoginRequestDTO(registroDTO.getEmail(), registroDTO.getPassword());
        return login(loginRequest);
    }

    @Transactional
    public JwtResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) {
        // Validar el refresh token
        if (!tokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token inválido o expirado");
        }
        
        // Obtener el ID del usuario a partir del token
        Long userId = tokenProvider.getUserIdFromJWT(refreshTokenRequest.getRefreshToken());
        
        // Obtener el usuario
        Usuario usuario = usuarioService.getUsuarioById(userId);
        
        // Generar nuevos tokens
        String jwt = tokenProvider.generateToken(usuario);
        String refreshToken = tokenProvider.generateRefreshToken(usuario);
        
        // Construir respuesta
        return JwtResponseDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .tipo("Bearer")
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}