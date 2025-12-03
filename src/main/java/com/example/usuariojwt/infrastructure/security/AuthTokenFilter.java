package com.example.usuariojwt.infrastructure.security;

import com.example.usuariojwt.infrastructure.exception.TokenValidationException;
import com.example.usuariojwt.infrastructure.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                // Verificar si el token está en la lista negra
                if (jwtUtils.isTokenBlacklisted(jwt)) {
                    logger.warn("Attempted to use blacklisted token");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated");
                    return;
                }
                
                // Validar el token
                if (!jwtUtils.validateJwtToken(jwt)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }
                
                // Obtener el nombre de usuario del token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                
                // Cargar los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Crear la autenticación
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            logger.warn("JWT token is expired: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        } catch (UsernameNotFoundException ex) {
            logger.error("User not found: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        } catch (BadCredentialsException ex) {
            logger.error("Authentication failed: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        } catch (InternalAuthenticationServiceException ex) {
            logger.error("Authentication service error: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication service error");
            return;
        } catch (Exception ex) {
            logger.error("Authentication error: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTH_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void validateJwtToken(String authToken) {
        try {
            jwtUtils.validateJwtToken(authToken);
        } catch (ExpiredJwtException ex) {
            throw new TokenValidationException("Token expirado", ex);
        } catch (UnsupportedJwtException ex) {
            throw new TokenValidationException("Token no soportado", ex);
        } catch (MalformedJwtException ex) {
            throw new TokenValidationException("Token con formato inválido", ex);
        } catch (SignatureException ex) {
            throw new TokenValidationException("Firma del token inválida", ex);
        } catch (IllegalArgumentException ex) {
            throw new TokenValidationException("Token vacío o inválido", ex);
        } catch (Exception ex) {
            throw new TokenValidationException("Error al validar el token", ex);
        }
    }
}
