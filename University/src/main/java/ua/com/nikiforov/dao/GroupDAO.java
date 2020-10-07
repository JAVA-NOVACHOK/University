package ua.com.nikiforov.dao;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.mappers.GroupMapper;
import ua.com.nikiforov.models.Group;

@Component
public class GroupDAO {

    private static final String ADD_GROUP = INSERT + TABLE_GROUPS + L_BRACKET + COLUMN_GROUP_NAME + VALUES_1_QMARK;
    private static final String FIND_GROUP_BY_ID = SELECT + ASTERISK + FROM + TABLE_GROUPS + WHERE + COLUMN_GROUP_ID
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_GROUPS = SELECT + ASTERISK + FROM + TABLE_GROUPS;
    private static final String UPDATE_GROUP = UPDATE + TABLE_GROUPS + SET + COLUMN_GROUP_NAME + EQUALS_M + Q_MARK
            + WHERE + COLUMN_GROUP_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_GROUP_BY_ID = DELETE + FROM + TABLE_GROUPS + WHERE + COLUMN_GROUP_ID + EQUALS_M
            + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Group getGroupById(Long id) {
        return jdbcTemplate.queryForObject(FIND_GROUP_BY_ID, new Object[] { id }, new GroupMapper());
    }

    public boolean deleteGroupById(Long id) {
        return jdbcTemplate.update(DELETE_GROUP_BY_ID, id) > 0;
    }

    public List<Group> getAllGroups() {
        return jdbcTemplate.query(GET_ALL_GROUPS, new GroupMapper());
    }

    public boolean addGroup(Long groupNumber) {
        return jdbcTemplate.update(ADD_GROUP, groupNumber) > 0;
    }

    public boolean updateGroup(Long groupNumber, Long id) {
        return jdbcTemplate.update(UPDATE_GROUP, groupNumber, id) > 0;
    }

}
