package com.example.usuariojwt.application.mapper;

import com.example.usuariojwt.application.dto.EmployeeDTO;
import com.example.usuariojwt.domain.model.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "creadoPor", ignore = true)
    Empleado toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "edadActual", expression = "java(empleado.calcularEdadActual())")
    @Mapping(target = "tiempoVinculacion", expression = "java(empleado.calcularTiempoVinculacion())")
    EmployeeDTO toDto(Empleado empleado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "numeroDocumento", ignore = true)
    void updateEmpleadoFromDto(EmployeeDTO dto, @MappingTarget Empleado entity);
}
