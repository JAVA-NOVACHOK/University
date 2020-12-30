package ua.com.nikiforov.dao.group;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.GroupMapperDTO;
import ua.com.nikiforov.models.Group;

@Repository
public class GroupDAOImpl implements GroupDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAOImpl.class);

    private static final String FIND_GROUP_BY_NAME = "SELECT g FROM Group g  WHERE g.groupName =  ?1 ";
    private static final String GET_ALL_GROUPS = "SELECT g FROM Group g ORDER BY g.groupName";
    private static final String DELETE_GROUP_BY_ID = "DELETE  FROM Group  WHERE groupId =  ?1 ";

    private static final int FIRST_PARAMETER_INDEX = 1;

    @PersistenceContext
    private EntityManager entityManager;

    private GroupMapperDTO groupMapper;

    @Autowired
    public GroupDAOImpl(GroupMapperDTO groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    public Group getGroupById(Long id) {
        LOGGER.debug("Getting group by id '{}'", id);
        Group group = entityManager.find(Group.class, id);
        if(group == null ){
            String failMessage = String.format("Fail to get group by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved group '{}'", group);
        return group;
    }

    @Override
    public Group getGroupByName(String groupName) {
        LOGGER.debug("Getting group by name '{}'", groupName);
        Group group;
        try {
            group = (Group) entityManager.createQuery(FIND_GROUP_BY_NAME)
                    .setParameter(FIRST_PARAMETER_INDEX, groupName)
                    .getSingleResult();
        }catch (NoResultException e){
            String failMessage = String.format("Fail to get group by Id %s from DB", groupName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage,e);
        }
        LOGGER.info("Successfully retrieved group '{}'", group);
        return group;
    }

    @Override
    @Transactional
    public void addGroup(String groupName) {
        String message = String.format("Group with name %s",groupName);
        LOGGER.debug("Adding {}", message);
        entityManager.persist(new Group(groupName));
        LOGGER.info("Successful adding {}", message);
    }

    @Override
    @Transactional
    public void deleteGroupById(Long id) {
        LOGGER.debug("Deleting group by id '{}'", id);
        try {
            entityManager.createQuery(DELETE_GROUP_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, id)
                    .executeUpdate();
            LOGGER.info("Successful deleting group with id '{}'.", id);
        } catch (PersistenceException e) {
            String failDeleteMessage = String.format("Couldn't delete group by ID %d", id);
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }

    @Override
    public List<Group> getAllGroups() {
        LOGGER.debug("Retrieving all groups.");
        List<Group> allGroups = new ArrayList<>();
        try {
            allGroups.addAll(entityManager.createQuery(GET_ALL_GROUPS, Group.class).getResultList());
            LOGGER.info("Successfully query for all groups");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all groups from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allGroups;
    }

    @Override
    @Transactional
    public void updateGroup(GroupDTO groupDTO) {
        Group newGroup = groupMapper.groupDTOToGroup(groupDTO);
        LOGGER.debug("Updating group '{}'", newGroup.getGroupName());
        try {
            Group group = getGroupById(newGroup.getGroupId());
            newGroup.setGroupStudents(group.getGroupStudents());
            entityManager.merge(newGroup);
            LOGGER.info("Successful adding group with id '{}' name '{}'", newGroup.getGroupId(), newGroup.getGroupName());
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Group with id %d name %s", newGroup.getGroupId(), newGroup.getGroupName());
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

}
