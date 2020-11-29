package ua.com.nikiforov.services.group;

import java.util.List;

import ua.com.nikiforov.controllers.dto.GroupDTO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

public interface GroupService {
    
    public boolean addGroup(String groupName);
    
    public Group getGroupById(long id);
    
    public Group getGroupByName(String groupName);
    
    public List<Group> getAllGroups();
    
    public boolean updateGroup(GroupDTO group);
    
    public boolean deleteGroup(long id);
    
    public List<Student> getStudentsByGroupId(long groupId);
    
    public Group getGroupByStudentId(long studentId);
}

