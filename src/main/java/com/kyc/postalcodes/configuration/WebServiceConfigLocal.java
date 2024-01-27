package com.kyc.postalcodes.configuration;

import org.apache.ws.commons.schema.resolver.DefaultURIResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

import static com.kyc.postalcodes.constants.AppConstants.NAME_SPACE_POSTAL_CODES_URI;

@Configuration
@Profile("dev")
public class WebServiceConfigLocal {

    @Value("${service.url}")
    private String urlService;


    //http://localhost:9000/ws/paymentService/KYCPayments.wsdl
    @Bean(name="KYCPostalCodes")
    public DefaultWsdl11Definition postalCodesWsdl(){

        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setServiceName("PostalCodes");
        definition.setPortTypeName("PostalCodesPortType");
        definition.setLocationUri(urlService);
        definition.setTargetNamespace(NAME_SPACE_POSTAL_CODES_URI);
        definition.setSchemaCollection(schemasCollection());
        return definition;
    }

    @Bean
    public XsdSchemaCollection schemasCollection(){

        CommonsXsdSchemaCollection collection = new CommonsXsdSchemaCollection();
        ClassPathResource paymentTypes = new ClassPathResource("ws/PostalCodesTypes.xsd");

        collection.setXsds(paymentTypes);
        collection.setInline(false);
        collection.setUriResolver(new DefaultURIResolver());
        return collection;
    }

    //Exposes the xsd schema in the url
    @Bean(name="CommonTypes")
    public XsdSchema commonTypes(){

        ClassPathResource commonTypes = new ClassPathResource("ws/CommonTypes.xsd");
        return new SimpleXsdSchema(commonTypes);
    }
}
