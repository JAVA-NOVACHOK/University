package ua.com.nikiforov.services.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.exceptions.StudentsInGroupException;
import ua.com.nikiforov.mappers_dto.GroupMapperDTO;
import ua.com.nikiforov.models.Group;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private static final String GETTING_MSG = "Getting {}";

    private GroupDAO groupDAO;
    private GroupMapperDTO groupMapper;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, GroupMapperDTO groupMapper) {
        this.groupDAO = groupDAO;
        this.groupMapper = groupMapper;
    }

    @Override
    public void addGroup(String groupName) {
        String addMessage = String.format("Group with name %s", groupName);
        LOGGER.debug("Adding {}", addMessage);
        try {
            groupDAO.save(new Group(groupName));
            LOGGER.info("Successfully added {}", addMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + addMessage, e);
        }
    }

    @Override
    public GroupDTO getGroupById(long groupId) {
        String getMessage = String.format("Group by id %d", groupId);
        LOGGER.debug(GETTING_MSG, getMessage);
        GroupDTO group = groupMapper.groupToGroupDTO(groupDAO.getGroupByGroupId(groupId));
        if (group == null) {
            String failMessage = String.format("Fail to get group by Id %d from DB", groupId);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved group '{}'", group);
        return group;
    }

    @Override
    public GroupDTO getGroupByName(String groupName) {
        String getMessage = String.format("Group by name %s", groupName);
        LOGGER.debug(GETTING_MSG, getMessage);
        GroupDTO group = groupMapper.groupToGroupDTO(groupDAO.getGroupByGroupName(groupName));
        if (group == null) {
            String failMessage = String.format("Fail to get group by Id %s from DB", groupName);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved group '{}'", group);
        return group;
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        String getAllMessage = "all groups";
        LOGGER.debug(GETTING_MSG, getAllMessage);
        List<GroupDTO> allGroups = new ArrayList<>();
        try {
            allGroups.addAll(groupMapper.getGroupDTOList(groupDAO.getAllGroups()));
            LOGGER.info("Successfully query for all groups");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all groups from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allGroups;
    }

    @Override
    public void updateGroup(GroupDTO groupDTO) {
        LOGGER.debug("Updating group {}", groupDTO);
        Group newGroup = groupMapper.groupDTOToGroup(groupDTO);
        Group groupWithStudents = groupDAO.getGroupByGroupId(newGroup.getGroupId());
        try {
            newGroup.setGroupStudents(groupWithStudents.getGroupStudents());
            groupDAO.save(newGroup);
            LOGGER.info("Successful adding group {}", newGroup);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Group with id %d name %s", newGroup.getGroupId(), newGroup.getGroupName());
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    public void deleteGroup(long id) {
        String deleteMessage = String.format("group with id %d.", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        if (!groupDAO.getGroupByGroupId(id).getGroupStudents().isEmpty()) {
            String message = String.format("There are students in group with id %d", id);
            throw new StudentsInGroupException(message);
        }
        try {
            groupDAO.deleteGroupByGroupId(id);
            LOGGER.info("Successful deleting {}.", deleteMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }

    }

}
