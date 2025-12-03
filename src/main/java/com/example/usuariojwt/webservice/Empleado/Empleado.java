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

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Clase Java para empleado complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="empleado"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="nombres" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="apellidos" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="numeroDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="fechaVinculacionCompania" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="cargo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="salario" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleado", namespace = "http://example.com/empleado-ws", propOrder = {
    "id",
    "nombres",
    "apellidos",
    "tipoDocumento",
    "numeroDocumento",
    "fechaNacimiento",
    "fechaVinculacionCompania",
    "cargo",
    "salario"
})
@XmlRootElement(name = "empleado", namespace = "http://example.com/empleado-ws")
@Getter
@Setter
public class Empleado {

    @XmlElement(namespace = "http://example.com/empleado-ws")
    protected Long id;
    
    @XmlElement(namespace = "http://example.com/empleado-ws", required = true)
    protected String nombres;
    
    @XmlElement(namespace = "http://example.com/empleado-ws", required = true)
    protected String apellidos;
    
    @XmlElement(name = "tipoDocumento", namespace = "http://example.com/empleado-ws", required = true)
    protected String tipoDocumento;
    
    @XmlElement(name = "numeroDocumento", namespace = "http://example.com/empleado-ws", required = true)
    protected String numeroDocumento;
    
    @XmlElement(name = "fechaNacimiento", namespace = "http://example.com/empleado-ws", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaNacimiento;
    
    @XmlElement(name = "fechaVinculacionCompania", namespace = "http://example.com/empleado-ws", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaVinculacionCompania;
    
    @XmlElement(namespace = "http://example.com/empleado-ws", required = true)
    protected String cargo;
    
    @XmlElement(namespace = "http://example.com/empleado-ws", required = true)
    protected BigDecimal salario;

}
