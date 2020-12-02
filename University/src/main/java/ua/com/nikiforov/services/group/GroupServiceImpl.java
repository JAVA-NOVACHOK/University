package ua.com.nikiforov.services.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.GroupDTO;
import ua.com.nikiforov.controllers.dto.StudentDTO;
import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.mappers.GroupMapper;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.persons.StudentsService;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDAO groupDAO;
    private StudentsService studentsService;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, StudentsService studentsService) {
        this.groupDAO = groupDAO;
        this.studentsService = studentsService;
    }

    @Override
    public boolean addGroup(String groupName) {
        return groupDAO.addGroup(groupName);

    }

    @Override
    public GroupDTO getGroupById(long groupId) {
        GroupDTO groupDTO = getGroupDTO(groupDAO.getGroupById(groupId));
        groupDTO.setStudents(getStudentsByGroupId(groupId));
        return groupDTO;
    }

    @Override
    public GroupDTO getGroupByName(String groupName) {
        return getGroupDTO(groupDAO.getGroupByName(groupName));
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        List<GroupDTO> groupsDTO = new ArrayList<>();
        for(Group group : groupDAO.getAllGroups()){
            groupsDTO.add(getGroupDTO(group));
        }
        return groupsDTO;
    }

    @Override
    public boolean updateGroup(GroupDTO group) {
        return groupDAO.updateGroup(group);
    }

    @Override
    public boolean deleteGroup(long id) {
        return groupDAO.deleteGroupById(id);
    }

    @Override
    public List<StudentDTO> getStudentsByGroupId(long groupId) {
        return studentsService.getStudentsByGroupId(groupId);
    }

    @Override
    public GroupDTO getGroupByStudentId(long studentId) {
        GroupDTO groupDTO = getGroupDTO(groupDAO.getGroupByStudentId(studentId));
        groupDTO.setStudents(getStudentsByGroupId(groupDTO.getGroupId()));
        return groupDTO;
    }
    
    private GroupDTO getGroupDTO(Group group) {
        return new GroupDTO(group.getGroupId(),group.getGroupName());
    }

}
