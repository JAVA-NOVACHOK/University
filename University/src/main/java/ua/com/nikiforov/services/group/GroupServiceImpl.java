package ua.com.nikiforov.services.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.StudentsInGroupException;
import ua.com.nikiforov.mappers_dto.GroupMapperDTO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.repositories.group.GroupRepository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private static final Sort SORT_BY_GROUP_NAME = Sort.by(Sort.Direction.ASC, "groupName");

    private static final String GETTING_MSG = "Getting {}";
    private static final String SUCCESS_GET_GROUP = "Successfully retrieved group '{}'";

    private GroupRepository groupRepository;
    private GroupMapperDTO groupMapper;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupMapperDTO groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public GroupDTO addGroup(GroupDTO groupDTO) {
        String addMessage = String.format("Group with name %s", groupDTO.getGroupName());
        LOGGER.debug("Adding {}", addMessage);
        Group group;
        try {
            group = groupRepository.save(groupMapper.groupDTOToGroup(groupDTO));
            LOGGER.info("Successfully added {}", addMessage);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + addMessage, e);
        }
        return groupMapper.groupToGroupDTO(group);
    }

    @Override
    @Transactional
    public GroupDTO getGroupById(long groupId) {
        String getMessage = String.format("Group by id %d", groupId);
        LOGGER.debug(GETTING_MSG, getMessage);
        Group group = findGroupById(groupId,getMessage);
        LOGGER.info(SUCCESS_GET_GROUP, group);
        return groupMapper.groupToGroupDTO(group);
    }

    private Group findGroupById(long groupId,String message){
        Group group;
        try {
            group = groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format("Couldn't get %s", message);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        return group;
    }

    @Override
    @Transactional
    public GroupDTO getGroupByName(String groupName) {
        String getMessage = String.format("Group by name %s", groupName);
        LOGGER.debug(GETTING_MSG, getMessage);
        Group group = groupRepository.getGroupByGroupName(groupName).orElseThrow(EntityNotFoundException::new);
        LOGGER.info(SUCCESS_GET_GROUP, group);
        return groupMapper.groupToGroupDTO(group);
    }

    @Override
    @Transactional
    public List<GroupDTO> getAllGroups() {
        String getAllMessage = "all groups";
        LOGGER.debug(GETTING_MSG, getAllMessage);
        List<GroupDTO> allGroups = new ArrayList<>();
        try {
            allGroups.addAll(groupMapper.getGroupDTOList(groupRepository.findAll(SORT_BY_GROUP_NAME)));
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
    public GroupDTO updateGroup(GroupDTO groupDTO) {
        String message = String.format("Group %s when update",groupDTO);
        LOGGER.debug(message);
        Group newGroup = groupMapper.groupDTOToGroup(groupDTO);
        try {
            Group groupWithStudents = findGroupById(newGroup.getGroupId(),message);
            newGroup.setGroupStudents(groupWithStudents.getGroupStudents());
            groupRepository.save(newGroup);
            LOGGER.info("Successful adding group {}", newGroup);
        } catch (DataIntegrityViolationException e) {
            String duplicateMessage = String.format("Already exists %s", groupDTO);
            LOGGER.error("Error! {}", duplicateMessage);
            throw new DuplicateKeyException(duplicateMessage);
        }
        return groupMapper.groupToGroupDTO(newGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(long id) {
        String deleteMessage = String.format("Group with id %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        if (!findGroupById(id,deleteMessage).getGroupStudents().isEmpty()) {
            String message = String.format("There are students in group with id %d", id);
            throw new StudentsInGroupException(message);
        }
        try {
            groupRepository.deleteById(id);
            LOGGER.info("Successful deleting {}.", deleteMessage);
        } catch (PersistenceException e) {
            String failDeleteMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }

    }

}
