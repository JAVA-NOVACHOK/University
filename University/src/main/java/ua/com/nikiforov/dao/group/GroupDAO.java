package ua.com.nikiforov.dao.group;

import java.util.List;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.models.Group;

public interface GroupDAO {
    
    public Group getGroupById(Long id);
    
    public Group getGroupByName(String groupName);

    public void deleteGroupById(Long id);

    public List<Group> getAllGroups();

    public void addGroup(String groupName);

    public void updateGroup(GroupDTO group);
    
//    public Group getGroupByStudentId(long studentId);
}
