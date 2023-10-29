package com.kyc.postalcodes.services;

import com.kyc.core.exception.KycSoapException;
import com.kyc.core.model.web.MessageData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.postalcodes.repositories.jdbc.GetPostalCodesRepository;
import com.kyc.postalcodes.ws.coretypes.Neighborhood;
import com.kyc.postalcodes.ws.coretypes.Neighborhoods;
import com.kyc.postalcodes.ws.coretypes.PostalCodeData;
import com.kyc.postalcodes.ws.types.GetPostalCodeRequest;
import com.kyc.postalcodes.ws.types.GetPostalCodeResponse;
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

    @Autowired
    private GetPostalCodesRepository repository;

    @Autowired
    private KycMessages kycMessages;

    private static final Pattern POSTAL_CODE = Pattern.compile("\\d{5}");

    public ResponseData<GetPostalCodeResponse> getPostalCode(RequestData<GetPostalCodeRequest> requestData){

        try{
            GetPostalCodeRequest req = requestData.getBody();
            String postalCode = req.getPostalCode();

            if(POSTAL_CODE.matcher(postalCode).matches()){

                Integer maxVersion = repository.getMaxVersion();

                PostalCodeData postalCodeData = repository.getPostalCodeMain(postalCode,maxVersion);
                if(postalCodeData!=null){

                    List<Neighborhood> neighborhoods = repository.getNeighborhoods(postalCode,maxVersion);

                    postalCodeData.setNeighborhoods(new Neighborhoods());
                    postalCodeData.getNeighborhoods().getNeighborhood().addAll(neighborhoods);

                    GetPostalCodeResponse response = new GetPostalCodeResponse();
                    response.setData(postalCodeData);
                    return ResponseData.of(response);
                }
            }
            MessageData messageData = kycMessages.getMessage(ERROR_CODE_002);
            throw KycSoapException.builderSoapException()
                    .faultCode(SoapFaultDefinition.CLIENT)
                    .errorData(messageData)
                    .inputData(requestData)
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
