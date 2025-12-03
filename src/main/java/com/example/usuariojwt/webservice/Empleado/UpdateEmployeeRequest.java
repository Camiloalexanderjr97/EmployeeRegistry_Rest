package com.example.usuariojwt.webservice.Empleado;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "empleado"
})
@XmlRootElement(name = "actualizarEmpleadoRequest", namespace = "http://www.example.com/empleado-ws")
public class UpdateEmployeeRequest {

    @XmlElement(required = true, namespace = "http://www.example.com/empleado-ws")
    protected EmployeeType empleado;

    public EmployeeType getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmployeeType value) {
        this.empleado = value;
    }
}
