package ua.com.nikiforov.dao.group;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.GroupsTable.*;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.GroupMapper;
import ua.com.nikiforov.models.Group;

@Repository
public class GroupDAOImpl implements GroupDAO {

    private static final String ADD_GROUP = INSERT + TABLE_GROUPS + L_BRACKET + NAME + VALUES_1_QMARK;
    private static final String FIND_GROUP_BY_ID = SELECT + ASTERISK + FROM + TABLE_GROUPS + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_GROUPS = SELECT + ASTERISK + FROM + TABLE_GROUPS;
    private static final String UPDATE_GROUP = UPDATE + TABLE_GROUPS + SET + NAME + EQUALS_M + Q_MARK
            + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_GROUP_BY_ID = DELETE + FROM + TABLE_GROUPS + WHERE + ID + EQUALS_M
            + Q_MARK;
    
    private String sql = "DROP TABLE IF EXISTS groups; CREATE TABLE groups("+
           " group_id serial PRIMARY KEY,"+
            "group_name varchar(255)"+
        ")";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    

    @Override
    public Group getGroupById(Long id) {
        
        return jdbcTemplate.queryForObject(FIND_GROUP_BY_ID, new Object[] { id }, new GroupMapper());
    }

    @Override
    public boolean deleteGroupById(Long id) {
        return jdbcTemplate.update(DELETE_GROUP_BY_ID, id) > 0;
    }

    @Override
    public List<Group> getAllGroups() {
        return jdbcTemplate.query(GET_ALL_GROUPS, new GroupMapper());
    }

    @Override
    public boolean addGroup(String groupName) {
        return jdbcTemplate.update(ADD_GROUP, groupName) > 0;
    }

    @Override
    public boolean updateGroup(String groupName, Long id) {
        return jdbcTemplate.update(UPDATE_GROUP, groupName, id) > 0;
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute(sql);
        
    }

}
