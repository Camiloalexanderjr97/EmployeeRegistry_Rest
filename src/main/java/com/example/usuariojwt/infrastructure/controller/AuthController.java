package com.example.usuariojwt.infrastructure.controller;

import com.example.usuariojwt.application.dto.JwtResponse;
import com.example.usuariojwt.application.dto.LoginRequest;
import com.example.usuariojwt.application.dto.MessageResponse;
import com.example.usuariojwt.infrastructure.security.JwtUtils;
import com.example.usuariojwt.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generar el token JWT
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            // Obtener los detalles del usuario autenticado
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Devolver la respuesta con el token y los detalles del usuario
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
            ));
            
        } catch (BadCredentialsException e) {
            // Manejar credenciales incorrectas
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Error: Usuario o contraseña incorrectos"));
                
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error durante la autenticación: " + e.getMessage()));
        }
    }

    @PostMapping("/signout")
    @Operation(summary = "Sign out user and invalidate the JWT token")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = "Authorization") String authHeader) {
        String jwt = parseJwt(authHeader);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            jwtUtils.invalidateToken(jwt);
            return ResponseEntity.ok(new MessageResponse("You've been signed out successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid or missing JWT token"));
    }
    
    private String parseJwt(String headerAuth) {
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
