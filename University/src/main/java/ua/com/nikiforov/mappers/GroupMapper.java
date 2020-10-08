package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.Group;
import static ua.com.nikiforov.dao.SqlConstants.GroupsTable.*;


public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getLong(ID));
        group.setGroupName(resultSet.getString(NAME));
        return group;
    }

}
