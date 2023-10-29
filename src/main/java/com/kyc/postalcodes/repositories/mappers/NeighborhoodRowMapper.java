package com.kyc.postalcodes.repositories.mappers;

import com.kyc.postalcodes.ws.coretypes.Neighborhood;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NeighborhoodRowMapper implements RowMapper<Neighborhood> {
    @Override
    public Neighborhood mapRow(ResultSet rs, int rowNum) throws SQLException {

        Neighborhood neighborhood = new Neighborhood();
        neighborhood.setId(rs.getInt("ID_NEIGHBORHOOD"));
        neighborhood.setName(rs.getString("NEIGHBORHOOD"));

        return neighborhood;
    }
}
