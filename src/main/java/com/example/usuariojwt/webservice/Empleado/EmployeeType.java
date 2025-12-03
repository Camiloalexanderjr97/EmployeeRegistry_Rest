package com.example.usuariojwt.webservice.Empleado;

import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleadoType", propOrder = {
    "id",
    "nombres",
    "apellidos",
    "tipoDocumento",
    "numeroDocumento",
    "fechaNacimiento",
    "fechaVinculacionCompania",
    "cargo",
    "salario"
}, namespace = "http://example.com/empleado-ws")
@Getter
@Setter
public class EmployeeType {
    @XmlElement(namespace = "http://example.com/empleado-ws")
    protected Long id;
    
    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected String nombres;
    
    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected String apellidos;
    
    @XmlElement(name = "tipoDocumento", required = true, namespace = "http://example.com/empleado-ws")
    protected String tipoDocumento;
    
    @XmlElement(name = "numeroDocumento", required = true, namespace = "http://example.com/empleado-ws")
    protected String numeroDocumento;
    
    @XmlElement(name = "fechaNacimiento", required = true, namespace = "http://example.com/empleado-ws")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaNacimiento;
    
    @XmlElement(name = "fechaVinculacionCompania", required = true, namespace = "http://example.com/empleado-ws")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaVinculacionCompania;
    
    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected String cargo;
    
    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected BigDecimal salario;
}
