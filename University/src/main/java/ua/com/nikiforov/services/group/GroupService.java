package ua.com.nikiforov.services.group;

import java.util.List;

import ua.com.nikiforov.controllers.dto.GroupDTO;
import ua.com.nikiforov.controllers.dto.StudentDTO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

public interface GroupService {
    
    public boolean addGroup(String groupName);
    
    public GroupDTO getGroupById(long id);
    
    public GroupDTO getGroupByName(String groupName);
    
    public List<GroupDTO> getAllGroups();
    
    public boolean updateGroup(GroupDTO group);
    
    public boolean deleteGroup(long id);
    
    public List<StudentDTO> getStudentsByGroupId(long groupId);
    
    public GroupDTO getGroupByStudentId(long studentId);
}

