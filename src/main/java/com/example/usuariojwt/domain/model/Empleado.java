package com.example.usuariojwt.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden tener más de 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20, message = "El tipo de documento no puede tener más de 20 caracteres")
    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no puede tener más de 20 caracteres")
    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de vinculación es obligatoria")
    @PastOrPresent(message = "La fecha de vinculación no puede ser futura")
    @Column(name = "fecha_vinculacion")
    private LocalDate fechaVinculacion;

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 100, message = "El cargo no puede tener más de 100 caracteres")
    private String cargo;

    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor que 0")
    @Column
    private Double salario;

    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private User creadoPor;

    public boolean esMayorDeEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    public String calcularEdadActual() {
        return calcularPeriodo(fechaNacimiento, LocalDate.now());
    }

    public String calcularTiempoVinculacion() {
        return calcularPeriodo(fechaVinculacion, LocalDate.now());
    }

    private String calcularPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        Period periodo = Period.between(fechaInicio, fechaFin);
        return String.format("%d años, %d meses, %d días", 
            periodo.getYears(), periodo.getMonths(), periodo.getDays());
    }
}
