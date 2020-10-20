package ua.com.nikiforov.dao.group;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.GroupsTable.*;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.ChangesNotMadeException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.GroupMapper;
import ua.com.nikiforov.models.Group;

@Repository
public class GroupDAOImpl implements GroupDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAOImpl.class);
    
    private static final String NO_AFFECTED_ROWS_MSG = "No affected rows";

    private static final String ADD_GROUP = INSERT + TABLE_GROUPS + L_BRACKET + NAME + VALUES_1_QMARK;
    private static final String FIND_GROUP_BY_ID = SELECT + ASTERISK + FROM + TABLE_GROUPS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_GROUP_BY_NAME = SELECT + ASTERISK + FROM + TABLE_GROUPS + WHERE + NAME + EQUALS_M
            + Q_MARK;
    private static final String GET_ALL_GROUPS = SELECT + ASTERISK + FROM + TABLE_GROUPS;
    private static final String UPDATE_GROUP = UPDATE + TABLE_GROUPS + SET + NAME + EQUALS_M + Q_MARK + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String DELETE_GROUP_BY_ID = DELETE + FROM + TABLE_GROUPS + WHERE + ID + EQUALS_M + Q_MARK;

    private GroupMapper groupMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAOImpl(DataSource dataSource, GroupMapper groupMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.groupMapper = groupMapper;
    }

    @Override
    public Group getGroupById(Long id) {
        LOGGER.debug("Getting group by id '{}'", id);
        Group group;
        try {
            group = jdbcTemplate.queryForObject(FIND_GROUP_BY_ID, new Object[] { id }, groupMapper);
            LOGGER.info("Retrived group '{}'", group);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Couldn't find group by id '{}'", id);
            throw new EntityNotFoundException("Couldn't find group by id " + id, e);
        }
        return group;
    }

    @Override
    public Group getGroupByName(String groupName) {
        LOGGER.debug("Getting group by name '{}'", groupName);
        Group group;
        try {
            group = jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME, new Object[] { groupName }, groupMapper);
            LOGGER.info("Retrived group '{}'", group);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Couldn't find group by name '{}'", groupName);
            throw new EntityNotFoundException("Couldn't find group by id " + groupName, e);
        }
        return group;
    }

    @Override
    public boolean deleteGroupById(Long id) {
        LOGGER.debug("Deleting group by id '{}'", id);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_GROUP_BY_ID, id) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting group");
            } else {
                throw new ChangesNotMadeException(NO_AFFECTED_ROWS_MSG);
            }
        } catch (DataAccessException e) {
            LOGGER.warn("Couldn't find group by ID '{}'", id);
            throw new ChangesNotMadeException("Couldn't delete group by id " + id, e);
        }
        return actionResult;
    }

    @Override
    public List<Group> getAllGroups() {
        LOGGER.debug("Retrieving all groups.");
        return jdbcTemplate.query(GET_ALL_GROUPS, groupMapper);
    }

    @Override
    public boolean addGroup(String groupName) {
        LOGGER.debug("Adding group '{}'", groupName);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_GROUP, groupName) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding group {}", groupName);
            } else {
                throw new ChangesNotMadeException(NO_AFFECTED_ROWS_MSG);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't add Group with name '{}'", groupName);
            throw new ChangesNotMadeException("Couldn't add group " + groupName, e);
        }
        return actionResult;
    }

    @Override
    public boolean updateGroup(String groupName, Long id) {
        LOGGER.debug("Updating group '{}'", groupName);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_GROUP, groupName,id) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding group with id '{}' name '{}'", id, groupName);
            } else {
                throw new ChangesNotMadeException(NO_AFFECTED_ROWS_MSG);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't update Group with id '{}' name '{}'", id, groupName);
            throw new ChangesNotMadeException("Couldn't update group " + groupName, e);
        }
        return actionResult;
    }

}
