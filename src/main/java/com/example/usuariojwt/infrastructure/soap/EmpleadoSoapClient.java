package com.example.usuariojwt.infrastructure.soap;

import com.example.usuariojwt.domain.model.Empleado;
import com.example.usuariojwt.webservice.Empleado.*;
import jakarta.xml.bind.*;
import jakarta.xml.soap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@Component
public class EmpleadoSoapClient {

    private final WebServiceTemplate webServiceTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoSoapClient.class);

    private static final String NAMESPACE_URI = "http://example.com/empleado-ws";
    private final String soapServiceUrl;

    public EmpleadoSoapClient(WebServiceTemplate webServiceTemplate,
                              @Value("${soap.service.url}") String soapServiceUrl) {
        this.webServiceTemplate = webServiceTemplate;
        this.soapServiceUrl = soapServiceUrl;
    }

    public CreateEmployeeResponse createEmployee(Empleado employee) {
        try {
            validateEmployee(employee);

            // Crear objeto empleado para el request
            com.example.usuariojwt.webservice.Empleado.Empleado empleado =
                    new com.example.usuariojwt.webservice.Empleado.Empleado();
            empleado.setNombres(employee.getNombres());
            empleado.setApellidos(employee.getApellidos());
            empleado.setTipoDocumento(employee.getTipoDocumento());
            empleado.setNumeroDocumento(employee.getNumeroDocumento());
            empleado.setCargo(employee.getCargo());
            empleado.setSalario(BigDecimal.valueOf(employee.getSalario()));
            empleado.setFechaNacimiento(localDateToXmlGregorian(employee.getFechaNacimiento()));
            empleado.setFechaVinculacionCompania(localDateToXmlGregorian(employee.getFechaVinculacion()));

            EmployeeRequest request = new EmployeeRequest();
            request.setEmpleado(empleado);

            JAXBElement<EmployeeRequest> requestElement =
                    new ObjectFactory().createCreateEmployeeRequest(request);

            // Callback para construir y loguear el SOAP
            WebServiceMessageCallback callback = message -> {
                try {
                    SoapMessage soapMessage = (SoapMessage) message;
                    SOAPMessage soapMsg = ((SaajSoapMessage) soapMessage).getSaajMessage();
                    SOAPEnvelope envelope = soapMsg.getSOAPPart().getEnvelope();
                    SOAPBody body = envelope.getBody();

                    body.removeContents();

                    SOAPBodyElement requestElem = body.addBodyElement(
                            envelope.createName("createEmployeeRequest", "emp", NAMESPACE_URI));

                    addChildElement(requestElem, "nombres", empleado.getNombres());
                    addChildElement(requestElem, "apellidos", empleado.getApellidos());
                    addChildElement(requestElem, "tipoDocumento", empleado.getTipoDocumento());
                    addChildElement(requestElem, "numeroDocumento", empleado.getNumeroDocumento());
                    addChildElement(requestElem, "fechaNacimiento", employee.getFechaNacimiento().toString());
                    addChildElement(requestElem, "fechaVinculacionCompania", employee.getFechaVinculacion().toString());
                    addChildElement(requestElem, "cargo", empleado.getCargo());
                    addChildElement(requestElem, "salario", empleado.getSalario().toString());

                    soapMsg.saveChanges();

                    logger.info("SOAP Request: {}", soapMessageToString(soapMsg));

                } catch (Exception e) {
                    logger.error("Error creating SOAP message", e);
                    throw new RuntimeException("Error creating SOAP message", e);
                }
            };

            logger.info("Sending SOAP request to: {}", soapServiceUrl);
            Object response = webServiceTemplate.marshalSendAndReceive(soapServiceUrl, requestElement, callback);

            if (response instanceof JAXBElement) {
                JAXBElement<?> jaxbElement = (JAXBElement<?>) response;
                if (jaxbElement.getValue() instanceof CreateEmployeeResponse) {
                    return (CreateEmployeeResponse) jaxbElement.getValue();
                }
                throw new RuntimeException("Unexpected response type: " +
                        jaxbElement.getValue().getClass().getName());
            } else if (response instanceof CreateEmployeeResponse) {
                return (CreateEmployeeResponse) response;
            }

            throw new RuntimeException("Unexpected response type from SOAP service");

        } catch (Exception e) {
            logger.error("Error calling SOAP service", e);
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    private void addChildElement(SOAPElement parent, String name, String value) throws SOAPException {
        SOAPElement child = parent.addChildElement(name, "emp", NAMESPACE_URI);
        child.addTextNode(value);
    }

    private void validateEmployee(Empleado employee) {
        if (employee.getNombres() == null || employee.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'nombres' es obligatorio");
        }
        if (employee.getApellidos() == null || employee.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'apellidos' es obligatorio");
        }
        if (employee.getTipoDocumento() == null || employee.getTipoDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'tipoDocumento' es obligatorio");
        }
        if (employee.getNumeroDocumento() == null || employee.getNumeroDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'numeroDocumento' es obligatorio");
        }
        if (employee.getCargo() == null || employee.getCargo().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'cargo' es obligatorio");
        }
        if (employee.getSalario() == null) {
            throw new IllegalArgumentException("El campo 'salario' es obligatorio");
        }
        if (employee.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("El campo 'fechaNacimiento' es obligatorio");
        }
        if (employee.getFechaVinculacion() == null) {
            throw new IllegalArgumentException("El campo 'fechaVinculacion' es obligatorio");
        }
    }

    public XMLGregorianCalendar localDateToXmlGregorian(LocalDate date) {
        if (date == null) return null;
        GregorianCalendar gc = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            throw new RuntimeException("Error converting LocalDate to XMLGregorianCalendar", e);
        }
    }

    // Utilitario para convertir SOAPMessage a String
    private String soapMessageToString(SOAPMessage soapMessage) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessage.writeTo(out);
            return out.toString(java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error converting SOAPMessage to String", e);
            return "Error converting SOAPMessage to String: " + e.getMessage();
        }
    }
}