package com.example.usuariojwt.infrastructure.webservice;

import com.example.usuariojwt.domain.model.Empleado;
import com.example.usuariojwt.domain.repository.EmployeeRepository;
import com.example.usuariojwt.infrastructure.exception.ResourceNotFoundException;
import com.example.usuariojwt.webservice.Empleado.*;
import lombok.AllArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@AllArgsConstructor
public class EmpleadoEndpoint {
    
    private static final String NAMESPACE_URI = "http://www.example.com/empleado-ws";
    
    private final EmployeeRepository employeeRepository;

    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "crearEmpleadoRequest")
    @ResponsePayload
    public CreateEmployeeResponse crearEmpleado(@RequestPayload CreateEmployeeRequest request) {
        var empleado = mapToEntity(request.getEmpleado());
        
        if (!empleado.esMayorDeEdad()) {
            return createErrorResponse("El empleado debe ser mayor de edad", "ERROR_VALIDACION");
        }
        
        if (employeeRepository.existsByNumeroDocumentoAndDeletedFalse(empleado.getNumeroDocumento())) {
            return createErrorResponse("Ya existe un empleado con el número de documento: " + empleado.getNumeroDocumento(), 
                                     "ERROR_DUPLICADO");
        }
        
        var empleadoGuardado = employeeRepository.save(empleado);
        
        return createSuccessResponse(empleado, "Empleado creado exitosamente");
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actualizarEmpleadoRequest")
    @ResponsePayload
    public UpdateEmployeeResponse actualizarEmpleado(@RequestPayload UpdateEmployeeRequest request) {
        var empleadoExistente = employeeRepository.findByIdAndDeletedFalse(request.getEmpleado().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + request.getEmpleado().getId()));
        
        var empleadoActualizado = mapToEntity(request.getEmpleado());
        empleadoActualizado.setId(empleadoExistente.getId());
        empleadoActualizado.setCreadoPor(empleadoExistente.getCreadoPor());
        
        if (!empleadoActualizado.esMayorDeEdad()) {
            UpdateEmployeeResponse response = new UpdateEmployeeResponse();
            response.setMensaje("El empleado debe ser mayor de edad");
            response.setCodigo("ERROR_VALIDACION");
            return response;
        }
        
        if (employeeRepository.existsByNumeroDocumentoAndIdNotAndDeletedFalse(
                empleadoActualizado.getNumeroDocumento(), empleadoActualizado.getId())) {
            UpdateEmployeeResponse response = new UpdateEmployeeResponse();
            response.setMensaje("Ya existe otro empleado con el número de documento: " + empleadoActualizado.getNumeroDocumento());
            response.setCodigo("ERROR_DUPLICADO");
            return response;
        }
        
        empleadoActualizado = employeeRepository.save(empleadoActualizado);
        
        UpdateEmployeeResponse response = new UpdateEmployeeResponse();
        response.setEmpleado(mapToEmpleadoType(empleadoActualizado));
        response.setMensaje("Empleado actualizado exitosamente");
        response.setCodigo("EXITO");
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "eliminarEmpleadoRequest")
    @ResponsePayload
    public DeleteEmployeeResponse eliminarEmpleado(@RequestPayload DeleteEmployeeRequest request) {
        var empleado = employeeRepository.findByIdAndDeletedFalse(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + request.getId()));
        
        empleado.setDeleted(true);
        employeeRepository.save(empleado);
        
        var response = new DeleteEmployeeResponse();
        response.setMensaje("Empleado eliminado exitosamente");
        response.setCodigo("EXITO");
        return response;
    }
    
    private Empleado mapToEntity(EmployeeType employeeType) {
        var empleado = new Empleado();
        empleado.setId(employeeType.getId());
        empleado.setNombres(employeeType.getNombres());
        empleado.setApellidos(employeeType.getApellidos());
        empleado.setTipoDocumento(employeeType.getTipoDocumento());
        empleado.setNumeroDocumento(employeeType.getNumeroDocumento());
        
        if (employeeType.getFechaNacimiento() != null) {
            empleado.setFechaNacimiento(employeeType.getFechaNacimiento().toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDate());
        }
        
        if (employeeType.getFechaVinculacion() != null) {
            empleado.setFechaVinculacion(employeeType.getFechaVinculacion().toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDate());
        }
        
        empleado.setCargo(employeeType.getCargo());
        empleado.setSalario(employeeType.getSalario());
        return empleado;
    }
    
    private EmployeeType mapToEmpleadoType(Empleado empleado) {
        var empleadoType = new EmployeeType();
        empleadoType.setId(empleado.getId());
        empleadoType.setNombres(empleado.getNombres());
        empleadoType.setApellidos(empleado.getApellidos());
        empleadoType.setTipoDocumento(empleado.getTipoDocumento());
        empleadoType.setNumeroDocumento(empleado.getNumeroDocumento());
        empleadoType.setFechaNacimiento(javax.xml.datatype.DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendar(empleado.getFechaNacimiento().toString()));
        empleadoType.setFechaVinculacion(javax.xml.datatype.DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendar(empleado.getFechaVinculacion().toString()));
        empleadoType.setCargo(empleado.getCargo());
        empleadoType.setSalario(empleado.getSalario());
        return empleadoType;
    }
    
    private CreateEmployeeResponse createSuccessResponse(Empleado empleado, String mensaje) {
        var response = new CreateEmployeeResponse();
        response.setEmpleado(mapToEmpleadoType(empleado));
        response.setMensaje(mensaje);
        response.setCodigo("EXITO");
        return response;
    }
    
    private CreateEmployeeResponse createErrorResponse(String mensaje, String codigo) {
        var response = new CreateEmployeeResponse();
        response.setMensaje(mensaje);
        response.setCodigo(codigo);
        return response;
    }
}
