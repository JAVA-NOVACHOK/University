package ua.com.nikiforov.services.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.services.persons.StudentsServiceImpl;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDAO groupDAO;


    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public void addGroup(String groupName) {
        try {
            groupDAO.addGroup(groupName);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while adding", e);
        }
    }

    @Override
    public GroupDTO getGroupById(long groupId) {
        return getGroupDTO(groupDAO.getGroupById(groupId));
    }

    @Override
    public GroupDTO getGroupByName(String groupName) {
        return getGroupDTO(groupDAO.getGroupByName(groupName));
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        return getGroupDTOs(groupDAO.getAllGroups());
    }

    @Override
    public void updateGroup(GroupDTO group) {
        try {
            groupDAO.updateGroup(group);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while editing!", e);
        }
    }

    @Override
    public void deleteGroup(long id) {
        groupDAO.deleteGroupById(id);
    }

    private static List<GroupDTO> getGroupDTOs(List<Group> groups) {
        List<GroupDTO> groupsDTO = new ArrayList<>();
        for (Group group : groups) {
            groupsDTO.add(getGroupDTO(group));
        }
        return groupsDTO;
    }

    public static GroupDTO getGroupDTO(Group group) {
        return new GroupDTO(group.getGroupId(), group.getGroupName(),
                StudentsServiceImpl.getListStudentDTO(group.getGroupStudents()));
    }

}
