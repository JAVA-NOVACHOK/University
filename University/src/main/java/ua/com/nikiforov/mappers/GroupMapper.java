package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Group;

@Component
public class GroupMapper implements RowMapper<Group> {
    
    private static final int GROUP_ID_INDEX = 1;
    private static final int GROUP_NAME_INDEX = 2;

    @Override
    public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Group group = new Group();
        group.setGroupId(resultSet.getLong(GROUP_ID_INDEX));
        group.setGroupName(resultSet.getString(GROUP_NAME_INDEX));
        return group;
    }

}
