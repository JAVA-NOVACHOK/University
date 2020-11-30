package ua.com.nikiforov.services.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.GroupDTO;
import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDAO groupDAO;
    private StudentDAO studentDAO;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, StudentDAO studentDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }

    @Override
    public boolean addGroup(String groupName) {
        return groupDAO.addGroup(groupName);

    }

    @Override
    public Group getGroupById(long groupId) {
        Group group = groupDAO.getGroupById(groupId);
        List<Student> students = getStudentsByGroupId(groupId);
        group.setGroupStudents(students);
        return group;
    }

    @Override
    public Group getGroupByName(String groupName) {
        return groupDAO.getGroupByName(groupName);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupDAO.getAllGroups();
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
    public List<Student> getStudentsByGroupId(long groupId) {
        return studentDAO.getStudentsByGroupId(groupId);
    }

    @Override
    public Group getGroupByStudentId(long studentId) {
        Group group = groupDAO.getGroupByStudentId(studentId);
        group.setGroupStudents(getStudentsByGroupId(group.getGroupId()));
        return group;
    }

}
