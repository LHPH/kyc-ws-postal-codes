package com.kyc.postalcodes.endpoints;

import com.kyc.core.model.web.RequestData;
import com.kyc.postalcodes.services.GetPostalCodeService;
import com.kyc.postalcodes.ws.types.GetPostalCodeRequest;
import com.kyc.postalcodes.ws.types.GetPostalCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.kyc.postalcodes.constants.AppConstants.NAME_SPACE_POSTAL_CODES_URI;

@Endpoint
public class GetPostalCodeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPostalCodeEndpoint.class);

    @Autowired
    private GetPostalCodeService getPostalCodeService;

    @PayloadRoot(localPart = "GetPostalCodeRequest", namespace = NAME_SPACE_POSTAL_CODES_URI)
    @ResponsePayload
    public GetPostalCodeResponse getPostalCode(@RequestPayload GetPostalCodeRequest request)  {

        RequestData<GetPostalCodeRequest> requestData = RequestData.<GetPostalCodeRequest>builder()
                .body(request)
                .build();

        LOGGER.info("Consume service of postal codes");
        return getPostalCodeService.getPostalCode(requestData)
                .getData();
    }
}
