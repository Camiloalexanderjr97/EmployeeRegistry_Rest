package com.example.usuariojwt.infrastructure.controller;

import com.example.usuariojwt.application.dto.EmployeeDTO;
import com.example.usuariojwt.application.mapper.EmployeeMapper;
import com.example.usuariojwt.domain.model.User;
import com.example.usuariojwt.domain.repository.EmployeeRepository;
import com.example.usuariojwt.infrastructure.exception.ResourceNotFoundException;
import com.example.usuariojwt.infrastructure.security.UserDetailsImpl;
import com.example.usuariojwt.infrastructure.soap.EmpleadoSoapClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleados", description = "Gestión de empleados")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final EmpleadoSoapClient empleadoSoapClient;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmpleados() {
        var empleados = employeeRepository.findByDeletedFalse();
        var empleadoDTOs = empleados.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(empleadoDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get an employee by ID")
    public ResponseEntity<EmployeeDTO> getEmpleadoById(@PathVariable Long id) {
        var empleado = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return new ResponseEntity<>(employeeMapper.toDto(empleado), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new employee")
    public ResponseEntity<?> createEmpleado(@Valid @RequestBody EmployeeDTO employeeDTO) {
        // Validar que el empleado sea mayor de edad
        if (employeeDTO.getFechaNacimiento() != null) {
            int edad = java.time.Period.between(
                employeeDTO.getFechaNacimiento(),
                java.time.LocalDate.now()
            ).getYears();

            if (edad < 18) {
                return ResponseEntity
                    .badRequest()
                    .body("El empleado debe ser mayor de edad");
            }
        }

        // Validate that there is no employee with the same document number
        if (employeeRepository.existsByNumeroDocumentoAndDeletedFalse(employeeDTO.getNumeroDocumento())) {
            return ResponseEntity
                .badRequest()
                .body("Error: An employee with document number " + employeeDTO.getNumeroDocumento() + " already exists");
        }

        // Get the authenticated user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Map DTO to entity
        var employee = employeeMapper.toEntity(employeeDTO);

        // Assign the user who creates the employee
        var createdBy = new User();
        createdBy.setId(userDetails.getId());
        employee.setCreadoPor(createdBy);

        // Save the employee
        var savedEmployee = employeeRepository.save(employee);

        try {
            // Call SOAP service to save employee in MySQL

            empleadoSoapClient.createEmployee(employee);

            return new ResponseEntity<>(employeeMapper.toDto(savedEmployee), HttpStatus.CREATED);

        } catch (Exception e) {
            // Log the error and return a proper response
            System.err.println("Error calling SOAP service: " + e.getMessage());
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Employee created but there was an error saving to the external system");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing employee")
    public ResponseEntity<?> updateEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        if (employeeRepository.findByIdAndDeletedFalse(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (employeeRepository.existsByNumeroDocumentoAndIdNotAndDeletedFalse(
                employeeDTO.getNumeroDocumento(), id)) {
            return ResponseEntity
                .badRequest()
                .body("Error: Another employee with document number " + employeeDTO.getNumeroDocumento() + " already exists");
        }

        if (employeeDTO.getFechaNacimiento() != null) {
            var edad = java.time.Period.between(
                employeeDTO.getFechaNacimiento(),
                java.time.LocalDate.now()
            ).getYears();

            if (edad < 18) {
                return ResponseEntity
                    .badRequest()
                    .body("El empleado debe ser mayor de edad");
            }
        }

        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeMapper.updateEmpleadoFromDto(employeeDTO, employee);

        var updatedEmployee = employeeRepository.save(employee);


        return new ResponseEntity<>(employeeMapper.toDto(updatedEmployee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an employee (soft delete)")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setDeleted(true);
        employeeRepository.save(employee);

        return ResponseEntity
                .ok()
                .body("El empleado Ha sido eliminado Exitosamente");
    }
}
