package com.example.usuariojwt.webservice.Empleado;

import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createEmployeeResponse", namespace = "http://example.com/empleado-ws", propOrder = {
    "empleado",
    "mensaje",
    "codigo"
})
@XmlRootElement(name = "createEmployeeResponse", namespace = "http://example.com/empleado-ws")
public class CreateEmployeeResponse {

    @XmlElement(name = "empleado", namespace = "http://example.com/empleado-ws", required = true)
    protected Empleado empleado;
    
    @XmlElement(name = "mensaje", namespace = "http://example.com/empleado-ws", required = true)
    protected String mensaje;
    
    @XmlElement(name = "codigo", namespace = "http://example.com/empleado-ws", required = true)
    protected String codigo;
}
