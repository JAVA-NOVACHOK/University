package ua.com.nikiforov.services.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.group.GroupDAOImpl;
import ua.com.nikiforov.models.Group;

@Service
public class GroupServiceImpl implements GroupService {
    
    @Autowired
    private GroupDAOImpl groupDAOImpl;
    

    @Override
    public boolean addGroup(String groupName) {
        return groupDAOImpl.addGroup(groupName);
    }

    @Override
    public Group getGroupById(long id) {
        return groupDAOImpl.getGroupById(id);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupDAOImpl.getAllGroups();
    }

    @Override
    public boolean updateGroup(long id, String groupName) {
        return groupDAOImpl.updateGroup(groupName, id);
    }

    @Override
    public boolean deleteGroup(long id) {
        return groupDAOImpl.deleteGroupById(id);
    }

}
