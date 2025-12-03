package com.example.usuariojwt.webservice.Empleado;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado"
})
@XmlRootElement(name = "crearEmpleadoRequest", namespace = "http://www.example.com/empleado-ws")
public class CreateEmployeeRequest {

    @XmlElement(required = true, namespace = "http://www.example.com/empleado-ws")
    protected EmployeeType empleado;
}
