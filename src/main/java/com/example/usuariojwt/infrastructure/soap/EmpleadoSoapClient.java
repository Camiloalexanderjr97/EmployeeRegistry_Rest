package com.example.usuariojwt.infrastructure.soap;

import com.example.usuariojwt.domain.model.Empleado;
import com.example.usuariojwt.webservice.Empleado.CreateEmployeeRequest;
import com.example.usuariojwt.webservice.Empleado.CreateEmployeeResponse;
import com.example.usuariojwt.webservice.Empleado.EmployeeType;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.datatype.DatatypeFactory;


@Component
public class EmpleadoSoapClient extends WebServiceGatewaySupport {

    private static final String SOAP_ACTION = "http://www.example.com/empleado-ws/crearEmpleadoRequest";

    public CreateEmployeeResponse createEmployee(Empleado employee) {
        try {
            var request = new CreateEmployeeRequest();
            var employeeType = new EmployeeType();
            
            // Map Employee to EmployeeType
            employeeType.setId(employee.getId());
            employeeType.setNombres(employee.getNombres());
            employeeType.setApellidos(employee.getApellidos());
            employeeType.setTipoDocumento(employee.getTipoDocumento());
            employeeType.setNumeroDocumento(employee.getNumeroDocumento());
            employeeType.setCargo(employee.getCargo());
            employeeType.setSalario(employee.getSalario());
            
            // Convert LocalDate to XMLGregorianCalendar
            if (employee.getFechaNacimiento() != null) {
                var fechaNacimiento = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(employee.getFechaNacimiento().toString());
                employeeType.setFechaNacimiento(fechaNacimiento);
            }
            
            if (employee.getFechaVinculacion() != null) {
                var fechaVinculacion = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(employee.getFechaVinculacion().toString());
                employeeType.setFechaVinculacion(fechaVinculacion);
            }
            
            request.setEmpleado(employeeType);
            
            return (CreateEmployeeResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                    getDefaultUri(),
                    request,
                    new SoapActionCallback(SOAP_ACTION)
                );
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service", e);
        }
    }
}
