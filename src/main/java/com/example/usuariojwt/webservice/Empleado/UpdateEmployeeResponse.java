package com.example.usuariojwt.webservice.Empleado;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado",
    "mensaje",
    "codigo"
})
@XmlRootElement(name = "actualizarEmpleadoResponse")
public class UpdateEmployeeResponse {

    @XmlElement(required = true)
    protected EmployeeType empleado;
    
    @XmlElement(required = true)
    protected String mensaje;
    
    @XmlElement(required = true)
    protected String codigo;
}
