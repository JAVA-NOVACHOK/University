package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.Group;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_GROUP_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_GROUP_NAME;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getLong(COLUMN_GROUP_ID));
        group.setGroupName(resultSet.getString(COLUMN_GROUP_NAME));
        return group;
    }

}
