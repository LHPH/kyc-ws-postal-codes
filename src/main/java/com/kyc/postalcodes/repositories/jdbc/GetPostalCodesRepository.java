package com.kyc.postalcodes.repositories.jdbc;

import com.kyc.postalcodes.repositories.mappers.NeighborhoodRowMapper;
import com.kyc.postalcodes.repositories.mappers.PostalCodeDataRowMapper;
import com.kyc.postalcodes.ws.coretypes.Neighborhood;
import com.kyc.postalcodes.ws.coretypes.PostalCodeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;

import static com.kyc.postalcodes.constants.AppConstants.CACHE_VERSION;
import static com.kyc.postalcodes.constants.AppConstants.GET_MAX_VERSION;
import static com.kyc.postalcodes.constants.AppConstants.GET_POSTAL_CODE_MAIN;
import static com.kyc.postalcodes.constants.AppConstants.GET_POSTAL_CODE_NEIGHBORHOOD;

@Repository
public class GetPostalCodesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Autowired
    private PostalCodeDataRowMapper postalCodeDataRowMapper;

    @Autowired
    private NeighborhoodRowMapper neighborhoodRowMapper;

    @Cacheable(value = CACHE_VERSION)
    public Integer getMaxVersion(){

        String sql = queriesProps.getProperty(GET_MAX_VERSION);
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public PostalCodeData getPostalCodeMain(String postalCode, Integer version){

        String sql = queriesProps.getProperty(GET_POSTAL_CODE_MAIN);
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql,postalCodeDataRowMapper,version,postalCode));
    }

    public List<Neighborhood> getNeighborhoods(String postalCode, Integer version){

        String sql = queriesProps.getProperty(GET_POSTAL_CODE_NEIGHBORHOOD);
        return jdbcTemplate.query(sql,neighborhoodRowMapper,version,postalCode);
    }

}
