package ua.com.nikiforov.dao.group;

import java.util.List;

import ua.com.nikiforov.models.Group;

public interface GroupDAO {
    
    public Group getGroupById(Long id);
    
    public Group getGroupByName(String groupName);

    public boolean deleteGroupById(Long id);

    public List<Group> getAllGroups();

    public boolean addGroup(String groupName);

    public boolean updateGroup(String groupName, Long id);

}
