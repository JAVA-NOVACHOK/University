package ua.com.nikiforov.dao.group;

import java.util.List;

import ua.com.nikiforov.models.Group;

public interface GroupDAO {
    
    public Group getGroupById(Long id);

    public boolean deleteGroupById(Long id);

    public List<Group> getAllGroups();

    public boolean addGroup(Long groupNumber);

    public boolean updateGroup(Long groupNumber, Long id);

}
