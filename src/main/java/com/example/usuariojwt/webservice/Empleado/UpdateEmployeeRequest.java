package com.example.usuariojwt.webservice.Empleado;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado"
})
public class UpdateEmployeeRequest {

    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected EmployeeType empleado;

    public EmployeeType getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmployeeType value) {
        this.empleado = value;
    }
}
