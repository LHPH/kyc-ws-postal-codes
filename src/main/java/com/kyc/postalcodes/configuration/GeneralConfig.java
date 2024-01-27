package com.kyc.postalcodes.configuration;

import com.kyc.core.config.LoadSimpleSqlConfig;
import com.kyc.core.exception.handlers.KycGenericSoapExceptionHandler;
import com.kyc.core.model.MessageData;
import com.kyc.core.properties.KycMessages;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import javax.xml.bind.JAXBException;

import static com.kyc.postalcodes.constants.AppConstants.ERROR_CODE_001;

@Configuration
@Import(value = {LoadSimpleSqlConfig.class, KycMessages.class})
@EnableCaching
public class GeneralConfig {

    @Bean
    public Jaxb2Marshaller marshaller(){

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(MessageData.class);
        return marshaller;
    }

    @Bean
    public KycGenericSoapExceptionHandler kycGenericSoapExceptionHandler(KycMessages kycMessages) throws JAXBException {

        return new KycGenericSoapExceptionHandler(kycMessages.getMessage(ERROR_CODE_001), marshaller());
    }
}
