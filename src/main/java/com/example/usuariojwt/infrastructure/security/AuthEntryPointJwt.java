package com.example.usuariojwt.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Authentication error: {}", authException.getMessage());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> body = new HashMap<>();
        var message = "Error de autenticación";
        var status = HttpServletResponse.SC_UNAUTHORIZED;
        var error = "Unauthorized";

        // Handle specific authentication exceptions
        if (authException.getCause() != null) {
            var cause = authException.getCause();
            
            if (cause instanceof ExpiredJwtException) {
                message = "El token JWT ha expirado";
                status = HttpServletResponse.SC_UNAUTHORIZED;
                error = "Token Expired";
            } else if (cause instanceof UnsupportedJwtException) {
                message = "Token JWT no soportado";
                status = HttpServletResponse.SC_BAD_REQUEST;
                error = "Unsupported JWT Token";
            } else if (cause instanceof MalformedJwtException) {
                message = "Token JWT inválido";
                status = HttpServletResponse.SC_BAD_REQUEST;
                error = "Invalid JWT Token";
            } else if (cause instanceof SignatureException) {
                message = "Firma de token JWT inválida";
                status = HttpServletResponse.SC_UNAUTHORIZED;
                error = "Invalid JWT Signature";
            } else if (cause instanceof IllegalArgumentException) {
                message = "Token JWT está vacío o es inválido";
                status = HttpServletResponse.SC_BAD_REQUEST;
                error = "Invalid JWT Token";
            }
        } else if (authException instanceof BadCredentialsException) {
            message = "Credenciales inválidas";
            status = HttpServletResponse.SC_UNAUTHORIZED;
            error = "Invalid Credentials";
        } else if (authException instanceof InternalAuthenticationServiceException) {
            message = "Error interno del servidor de autenticación";
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            error = "Authentication Service Error";
        }

        // Check for missing or invalid Authorization header
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            message = "Se requiere un token de autenticación";
            status = HttpServletResponse.SC_UNAUTHORIZED;
            error = "Missing or invalid Authorization header";
        }

        response.setStatus(status);
        
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getRequestURI());
        body.put("timestamp", System.currentTimeMillis());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
