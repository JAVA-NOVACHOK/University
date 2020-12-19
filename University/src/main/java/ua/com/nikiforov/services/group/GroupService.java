package ua.com.nikiforov.services.group;

import java.util.List;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;

public interface GroupService {
    
    public void addGroup(String groupName);
    
    public GroupDTO getGroupById(long id);
    
    public GroupDTO getGroupByName(String groupName);
    
    public List<GroupDTO> getAllGroups();
    
    public void updateGroup(GroupDTO group);
    
    public void deleteGroup(long id);
    
//    public List<StudentDTO> getStudentsByGroupId(long groupId);
    
//    public GroupDTO getGroupByStudentId(long studentId);
}

