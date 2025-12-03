//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.7 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.02 a las 11:02:46 PM COT 
//


package com.example.usuariojwt.webservice.Empleado;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>Clase Java para empleadoRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="empleadoRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="empleado" type="{http://example.com/empleado-ws}empleado"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "employeeRequest", namespace = "http://example.com/empleado-ws", propOrder = {
    "empleado"
})
@XmlRootElement(name = "createEmployeeRequest", namespace = "http://example.com/empleado-ws")
public class EmployeeRequest {

    @XmlElement(required = true, namespace = "http://example.com/empleado-ws")
    protected Empleado empleado;

    public Empleado getEmpleado() {
        return empleado;
    }
    
    public void setEmpleado(Empleado value) {
        this.empleado = value;
    }

}
