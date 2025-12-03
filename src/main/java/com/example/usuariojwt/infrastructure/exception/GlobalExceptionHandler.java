package com.example.usuariojwt.infrastructure.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        response.put("path", request.getDescription(false).replace("uri=", ""));
        return response;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.NOT_FOUND, 
            "Not Found", 
            ex.getMessage(), 
            request
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            "Nombre de usuario o contraseña incorrectos",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            "Usuario no encontrado",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            "La cuenta del usuario está deshabilitada",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(LockedException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            "La cuenta del usuario está bloqueada",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "Error en el servidor de autenticación",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler({ExpiredJwtException.class, UnsupportedJwtException.class, 
                      MalformedJwtException.class, SignatureException.class, 
                      IllegalArgumentException.class, TokenValidationException.class})
    public ResponseEntity<?> handleJwtException(Exception ex, WebRequest request) {
        String errorMessage = "Token de autenticación inválido";
        
        if (ex instanceof ExpiredJwtException) {
            errorMessage = "El token ha expirado";
        } else if (ex instanceof UnsupportedJwtException) {
            errorMessage = "Token no soportado";
        } else if (ex instanceof MalformedJwtException) {
            errorMessage = "Token con formato inválido";
        } else if (ex instanceof SignatureException) {
            errorMessage = "Firma del token inválida";
        } else if (ex instanceof IllegalArgumentException) {
            errorMessage = "Token vacío o inválido";
        } else if (ex instanceof TokenValidationException) {
            errorMessage = ex.getMessage();
        }
        
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            errorMessage,
            request
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            "Error de autenticación: " + ex.getMessage(),
            request
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String message = ex.getReason() != null ? 
            ex.getReason() : 
            ex.getBody().getDetail();
            
        // Convert HttpStatusCode to HttpStatus
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
            
        Map<String, Object> response = createErrorResponse(
            httpStatus,
            ex.getStatusCode().toString(),
            message,
            request
        );
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Bad Request",
            "Error de validación",
            request
        );
        response.put("errors", errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            "Acceso denegado: No tiene permisos para acceder a este recurso",
            request
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error("Error interno del servidor", ex);
        
        Map<String, Object> response = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo más tarde.",
            request
        );

        if (Boolean.TRUE.equals(environment.getProperty("app.debug", Boolean.class, false))) {
            response.put("debugMessage", ex.getMessage());
        }
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Add these fields at the class level

    @Autowired
    private org.springframework.core.env.Environment environment;
}
