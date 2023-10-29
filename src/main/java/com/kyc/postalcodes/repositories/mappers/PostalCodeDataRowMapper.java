package com.kyc.postalcodes.repositories.mappers;

import com.kyc.postalcodes.ws.coretypes.City;
import com.kyc.postalcodes.ws.coretypes.PostalCodeData;
import com.kyc.postalcodes.ws.coretypes.State;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostalCodeDataRowMapper implements RowMapper<PostalCodeData> {

    @Override
    public PostalCodeData mapRow(ResultSet rs, int rowNum) throws SQLException {

        PostalCodeData result = new PostalCodeData();
        City city = new City();
        State state = new State();

        result.setPostalCode(rs.getString("POSTAL_CODE"));
        city.setId(rs.getInt("ID_CITY"));
        city.setName(rs.getString("CITY"));

        state.setId(rs.getInt("ID_STATE"));
        state.setName(rs.getString("STATE"));

        result.setCity(city);
        result.setState(state);

        return result;
    }
}
