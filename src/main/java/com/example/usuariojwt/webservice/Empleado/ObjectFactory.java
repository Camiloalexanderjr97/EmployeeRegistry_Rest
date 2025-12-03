//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.7 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.02 a las 11:02:46 PM COT 
//


package com.example.usuariojwt.webservice.Empleado;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.usuariojwt.webservice.empleado package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CrearEmpleadoRequest_QNAME = new QName("http://www.example.com/empleado-ws", "crearEmpleadoRequest");
    private final static QName _CrearEmpleadoResponse_QNAME = new QName("http://www.example.com/empleado-ws", "crearEmpleadoResponse");
    private final static QName _ActualizarEmpleadoRequest_QNAME = new QName("http://www.example.com/empleado-ws", "actualizarEmpleadoRequest");
    private final static QName _ActualizarEmpleadoResponse_QNAME = new QName("http://www.example.com/empleado-ws", "actualizarEmpleadoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.usuariojwt.webservice.empleado
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EmployeeRequest }
     * 
     */
    public EmployeeRequest createEmpleadoRequest() {
        return new EmployeeRequest();
    }

    /**
     * Create an instance of {@link EmployeeResponse }
     * 
     */
    public EmployeeResponse createEmpleadoResponse() {
        return new EmployeeResponse();
    }

    /**
     * Create an instance of {@link DeleteEmployeeRequest }
     * 
     */
    public DeleteEmployeeRequest createEliminarEmpleadoRequest() {
        return new DeleteEmployeeRequest();
    }

    /**
     * Create an instance of {@link DeleteEmployeeResponse }
     * 
     */
    public DeleteEmployeeResponse createEliminarEmpleadoResponse() {
        return new DeleteEmployeeResponse();
    }

    /**
     * Create an instance of {@link Empleado }
     * 
     */
    public Empleado createEmpleado() {
        return new Empleado();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeRequest }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmployeeRequest }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.com/empleado-ws", name = "crearEmpleadoRequest")
    public JAXBElement<EmployeeRequest> createCrearEmpleadoRequest(EmployeeRequest value) {
        return new JAXBElement<EmployeeRequest>(_CrearEmpleadoRequest_QNAME, EmployeeRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmployeeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.com/empleado-ws", name = "crearEmpleadoResponse")
    public JAXBElement<EmployeeResponse> createCrearEmpleadoResponse(EmployeeResponse value) {
        return new JAXBElement<EmployeeResponse>(_CrearEmpleadoResponse_QNAME, EmployeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeRequest }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmployeeRequest }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.com/empleado-ws", name = "actualizarEmpleadoRequest")
    public JAXBElement<EmployeeRequest> createActualizarEmpleadoRequest(EmployeeRequest value) {
        return new JAXBElement<EmployeeRequest>(_ActualizarEmpleadoRequest_QNAME, EmployeeRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmployeeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.com/empleado-ws", name = "actualizarEmpleadoResponse")
    public JAXBElement<EmployeeResponse> createActualizarEmpleadoResponse(EmployeeResponse value) {
        return new JAXBElement<EmployeeResponse>(_ActualizarEmpleadoResponse_QNAME, EmployeeResponse.class, null, value);
    }

}
