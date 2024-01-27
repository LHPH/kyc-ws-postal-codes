package com.kyc.postalcodes.services;

import com.kyc.core.exception.KycSoapException;
import com.kyc.core.model.MessageData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.postalcodes.repositories.jdbc.GetPostalCodesRepository;
import com.kyc.postalcodes.ws.coretypes.Neighborhood;
import com.kyc.postalcodes.ws.coretypes.Neighborhoods;
import com.kyc.postalcodes.ws.coretypes.PostalCodeData;
import com.kyc.postalcodes.ws.types.GetPostalCodeRequest;
import com.kyc.postalcodes.ws.types.GetPostalCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;

import java.util.List;
import java.util.regex.Pattern;

import static com.kyc.postalcodes.constants.AppConstants.ERROR_CODE_002;
import static com.kyc.postalcodes.constants.AppConstants.ERROR_CODE_003;

@Service
public class GetPostalCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPostalCodeService.class);

    @Autowired
    private GetPostalCodesRepository repository;

    @Autowired
    private KycMessages kycMessages;

    private static final Pattern POSTAL_CODE = Pattern.compile("\\d{5}");

    public ResponseData<GetPostalCodeResponse> getPostalCode(RequestData<GetPostalCodeRequest> requestData){

        GetPostalCodeRequest req = requestData.getBody();
        String postalCode = req.getPostalCode();
        try{

            LOGGER.info("Checking if the postal code {} is a valid number",postalCode);
            if(POSTAL_CODE.matcher(postalCode).matches()){

                LOGGER.info("Retrieving the latest version of postal codes");
                Integer maxVersion = repository.getMaxVersion();
                LOGGER.info("The latest version is {}",maxVersion);

                LOGGER.info("Retrieving the postal code of database {}",postalCode);
                PostalCodeData postalCodeData = repository.getPostalCodeMain(postalCode,maxVersion);
                if(postalCodeData!=null){

                    LOGGER.info("Retrieving neighborhoods of the postal code {}",postalCode);
                    List<Neighborhood> neighborhoods = repository.getNeighborhoods(postalCode,maxVersion);

                    postalCodeData.setNeighborhoods(new Neighborhoods());
                    postalCodeData.getNeighborhoods().getNeighborhood().addAll(neighborhoods);

                    GetPostalCodeResponse response = new GetPostalCodeResponse();
                    response.setData(postalCodeData);
                    LOGGER.info("Returning info about postal code {}",postalCode);
                    return ResponseData.of(response);
                }
            }
            LOGGER.warn("The postal code does not valid or doest not exists {}",postalCode);
            MessageData messageData = kycMessages.getMessage(ERROR_CODE_002);
            throw KycSoapException.builderSoapException()
                    .faultCode(SoapFaultDefinition.CLIENT)
                    .errorData(messageData)
                    .inputData(postalCode)
                    .build();
        }
        catch(DataAccessException ex){

            MessageData messageData = kycMessages.getMessage(ERROR_CODE_003);
            throw KycSoapException.builderSoapException()
                    .faultCode(SoapFaultDefinition.SERVER)
                    .errorData(messageData)
                    .inputData(requestData)
                    .build();
        }
    }
}
