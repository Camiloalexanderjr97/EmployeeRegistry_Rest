package com.example.usuariojwt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(
    info = @Info(
        title = "User Management API",
        version = "1.0",
        description = "User Management System with JWT Authentication"
    )
)
public class UsuarioJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsuarioJwtApplication.class, args);
    }
}
