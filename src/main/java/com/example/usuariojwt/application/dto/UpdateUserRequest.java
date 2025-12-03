package com.example.usuariojwt.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    @Size(max = 50, message = "El correo electrónico no puede tener más de 50 caracteres")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres")
    private String password;
    
    private Set<String> roles;
}
