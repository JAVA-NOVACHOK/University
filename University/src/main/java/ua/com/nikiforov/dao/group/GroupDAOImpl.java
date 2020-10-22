package ua.com.nikiforov.dao.group;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.GroupsTable.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
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
            LOGGER.info("Successfully retrived group '{}'", group);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get group by Id %d", id);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return group;
    }

    @Override
    public Group getGroupByName(String groupName) {
        LOGGER.debug("Getting group by name '{}'", groupName);
        Group group;
        try {
            group = jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME, new Object[] { groupName }, groupMapper);
            LOGGER.info("Successfully retrived group '{}'", group);
        } catch (EmptyResultDataAccessException e) {
            String failGetByNameMessage = String.format("Couldn't get group by name %s", groupName);
            LOGGER.error(failGetByNameMessage);
            throw new EntityNotFoundException(failGetByNameMessage, e);
        }
        return group;
    }

    @Override
    public boolean deleteGroupById(Long id) {
        LOGGER.debug("Deleting group by id '{}'", id);
        boolean actionResult = false;
        String failDeleteMessage = String.format("Couldn't delete group by ID %d", id);
        try {
            actionResult = jdbcTemplate.update(DELETE_GROUP_BY_ID, id) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting group with id '{}'", id);
            } else {
                throw new DataOperationException(failDeleteMessage);
            }
        } catch (DataAccessException e) {
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }

    @Override
    public List<Group> getAllGroups() {
        LOGGER.debug("Retrieving all groups.");
        List<Group> allGroups = new ArrayList<>();
        try {
            allGroups.addAll(jdbcTemplate.query(GET_ALL_GROUPS, groupMapper));
            LOGGER.info("Successfully query for all groups");
        }catch (DataAccessException e) {
            String failMessage = "Fail to get all groups from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage,e);
        }
        return allGroups;
    }

    @Override
    public boolean addGroup(String groupName) {
        LOGGER.debug("Adding group '{}'", groupName);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_GROUP, groupName) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding group '{}'", groupName);
            } else {
                throw new DataOperationException(NO_AFFECTED_ROWS_MSG);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't add Group with name %s", groupName);
            LOGGER.error(failMessage,e);
            throw new DataOperationException(failMessage, e);
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
                String failMessage = String.format("Couldn't update Group with id %d name %s",id, groupName);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't update Group with id %d name %s",id, groupName);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

}
