package com.kyc.postalcodes.configuration;

import com.kyc.core.config.LoadSimpleSqlConfig;
import com.kyc.core.exception.handlers.KycGenericSoapExceptionHandler;
import com.kyc.core.properties.KycMessages;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import static com.kyc.postalcodes.constants.AppConstants.ERROR_CODE_001;

@Configuration
@Import(value = {LoadSimpleSqlConfig.class, KycMessages.class})
@EnableCaching
public class GeneralConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext ctx){

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(ctx);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet,"/kyc/*");
    }

    @Bean
    public KycGenericSoapExceptionHandler kycGenericSoapExceptionHandler(KycMessages kycMessages){

        return new KycGenericSoapExceptionHandler(kycMessages.getMessage(ERROR_CODE_001));
    }
}
