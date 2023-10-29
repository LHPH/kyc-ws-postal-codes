package com.kyc.postalcodes.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
@Profile("prod")
public class WebServiceConfig {

    @Value("${service.url}")
    private String urlService;

    @Bean(name="KYCPostalCodes")
    public SimpleWsdl11Definition postalCodesWsdl(){

        SimpleWsdl11Definition definition = new SimpleWsdl11Definition();
        definition.setWsdl(new ClassPathResource("ws/KYCPostalCodes.wsdl"));

        return definition;
    }

    @Bean(name="PostalCodesTypes")
    public XsdSchema postalCodesTypes(){

        ClassPathResource commonTypes = new ClassPathResource("ws/PostalCodesTypes.xsd");
        return new SimpleXsdSchema(commonTypes);
    }

    @Bean(name="CommonTypes")
    public XsdSchema commonTypes(){

        ClassPathResource commonTypes = new ClassPathResource("ws/CommonTypes.xsd");
        return new SimpleXsdSchema(commonTypes);
    }
}
