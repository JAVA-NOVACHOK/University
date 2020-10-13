package ua.com.nikiforov.services.group;

import java.util.List;

import ua.com.nikiforov.models.Group;

public interface GroupService {
    
    public void createTable();
    
    public boolean addGroup(String groupName);
    
    public Group getGroupById(long id);
    
    public Group getGroupByName(String groupName);
    
    public List<Group> getAllGroups();
    
    public boolean updateGroup(long id, String groupName);
    
    public boolean deleteGroup(long id);

}
