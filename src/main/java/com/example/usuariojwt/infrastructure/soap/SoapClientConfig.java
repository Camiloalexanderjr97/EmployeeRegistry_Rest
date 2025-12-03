package com.example.usuariojwt.infrastructure.soap;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.HttpComponents5MessageSender;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SoapClientConfig {

    @Value("${soap.service.url}")
    private String soapServiceUrl;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Set the context path to the package containing JAXB classes
        marshaller.setContextPath("com.example.usuariojwt.webservice.Empleado");
        
        // Configure the marshaller
        Map<String, Object> props = new HashMap<>();
        props.put(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        props.put(jakarta.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setMarshallerProperties(props);
        
        marshaller.setMtomEnabled(false);
        
        try {
            // This will validate the JAXB context
            marshaller.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JAXB marshaller. Please check your JAXB annotations.", e);
        }
        
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        // Create and configure HTTP client
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(20);
        connectionManager.setDefaultMaxPerRoute(10);

        // Configure timeouts in the HttpClient itself

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(5000))   // connect timeout
                .setResponseTimeout(Timeout.ofMilliseconds(10000)) // response/socket timeout
                .build();

        // Create HTTP client with request interceptor to remove duplicate Content-Length header
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .addRequestInterceptorFirst((request, entity, context) -> {
                    // Remove Content-Length header to prevent duplicate
                    request.removeHeaders(HttpHeaders.CONTENT_LENGTH);
                })
                .build();

        // Create message sender with the configured client
        HttpComponents5MessageSender messageSender = new HttpComponents5MessageSender();
        messageSender.setHttpClient(httpClient);

        // Configure WebServiceTemplate
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
        webServiceTemplate.setDefaultUri(soapServiceUrl);
        webServiceTemplate.setMessageSender(messageSender);
        webServiceTemplate.setCheckConnectionForFault(true);
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{ validatingInterceptor() });

        return webServiceTemplate;
    }

    @Bean
    public PayloadValidatingInterceptor validatingInterceptor() {
        PayloadValidatingInterceptor interceptor = new PayloadValidatingInterceptor();
        interceptor.setValidateRequest(false);
        interceptor.setValidateResponse(false);

        String schema = "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'>\n" +
                "  <xs:element name='dummy' type='xs:string'/>\n" +
                "</xs:schema>";
        ByteArrayResource schemaResource = new ByteArrayResource(schema.getBytes(StandardCharsets.UTF_8));
        interceptor.setSchema(schemaResource);
        return interceptor;
    }
}
