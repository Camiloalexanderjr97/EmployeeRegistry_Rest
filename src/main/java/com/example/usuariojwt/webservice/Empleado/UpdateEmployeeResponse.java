package com.example.usuariojwt.webservice.Empleado;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado",
    "mensaje",
    "codigo"
})
public class UpdateEmployeeResponse {

    @XmlElement(required = true)
    protected EmployeeType empleado;
    
    @XmlElement(required = true)
    protected String mensaje;
    
    @XmlElement(required = true)
    protected String codigo;
}
