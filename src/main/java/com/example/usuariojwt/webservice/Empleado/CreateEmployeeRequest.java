package com.example.usuariojwt.webservice.Empleado;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado"
})
@XmlRootElement(name = "createEmployeeRequest", namespace = "http://example.com/empleado-ws")
public class CreateEmployeeRequest {

    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected EmployeeType empleado;
}
