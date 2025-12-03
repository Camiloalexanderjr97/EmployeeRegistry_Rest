package com.example.usuariojwt.webservice.Empleado;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleadoType", propOrder = {
    "id",
    "nombres",
    "apellidos",
    "tipoDocumento",
    "numeroDocumento",
    "fechaNacimiento",
    "fechaVinculacion",
    "cargo",
    "salario"
})
@Getter
@Setter
public class EmployeeType {
    protected Long id;
    
    @XmlElement(required = true)
    protected String nombres;
    
    @XmlElement(required = true)
    protected String apellidos;
    
    @XmlElement(required = true)
    protected String tipoDocumento;
    
    @XmlElement(required = true)
    protected String numeroDocumento;
    
    @XmlElement(required = true)
    protected XMLGregorianCalendar fechaNacimiento;
    
    @XmlElement(required = true)
    protected XMLGregorianCalendar  fechaVinculacion;
    
    protected String cargo;
    protected Double salario;
}
